package nycuro.protection.handlers;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import nycuro.API;
import nycuro.Loader;

/**
 * author: NycuRO
 * HubCore Project
 * API 1.0.0
 */
public class ProtectionHandlers implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (API.getMechanicAPI().isOnSpawn(player)) {
            event.setCancelled(true);
            API.getMessageAPI().sendBreakMessage(player);
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        // todo: creeper (AI GRIJA!)
        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }
        Player player = (Player) event.getEntity();
        if (Loader.isOnSpawn.getBoolean(player.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        Player player = (event.getEntity() instanceof Player) ? (Player) event.getEntity() : null;
        if (player == null) return;
        Player damager = null;
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (ev instanceof EntityDamageByChildEntityEvent) {
                EntityDamageByChildEntityEvent evc = (EntityDamageByChildEntityEvent) ev;
                if (evc.getDamager() instanceof Player) damager = (Player) evc.getDamager();
            } else if (ev.getDamager() instanceof Player) damager = (Player) ev.getDamager();

            if (damager == null) return;
            if (API.getMechanicAPI().isOnSpawn(player)) {
                event.setCancelled(true);
                API.getMessageAPI().sendPvPOffMessage(damager);
            }
        }
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.FALL ||
                cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                cause == EntityDamageEvent.DamageCause.FIRE ||
                cause == EntityDamageEvent.DamageCause.PROJECTILE ||
                cause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                cause == EntityDamageEvent.DamageCause.LAVA ||
                cause == EntityDamageEvent.DamageCause.DROWNING ||
                cause == EntityDamageEvent.DamageCause.SUICIDE ||
                cause == EntityDamageEvent.DamageCause.MAGIC ||
                cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                cause == EntityDamageEvent.DamageCause.CONTACT) {
            if (API.getMechanicAPI().isOnSpawn(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void emptyBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Item inHand = inventory.getItemInHand();
        if (API.getMechanicAPI().isOnSpawn(player)) {
            event.setCancelled(true);
            inventory.removeItem(inHand);
            API.getMessageAPI().sendSmecherieMessage(player);
        }
    }

    @EventHandler
    public void fillBucket(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Item inHand = inventory.getItemInHand();
        if (API.getMechanicAPI().isOnSpawn(player)) {
            event.setCancelled(true);
            inventory.removeItem(inHand);
            API.getMessageAPI().sendSmecherieMessage(player);
        }
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clearAll();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Item itemHand = inventory.getItemInHand();
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            switch (itemHand.getId()) {
                case Item.DIAMOND_HOE:
                case Item.GOLD_HOE:
                case Item.IRON_HOE:
                case Item.STONE_HOE:
                case Item.WOODEN_HOE:
                case Item.FLINT_AND_STEEL:
                    if (API.getMechanicAPI().isOnSpawn(player)) {
                        API.getMessageAPI().sendAbuseMessage(player);
                        event.setCancelled(true);
                        return;
                    }
                default:
                    event.setCancelled(false);
                    break;
            }
        }
    }

    @EventHandler
    public void onDropItemFrameItem(ItemFrameDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (API.getMechanicAPI().isOnSpawn(player)) {
            event.setCancelled(true);
            API.getMessageAPI().sendPlaceMessage(player);
        }
        if (player.getLevel().getName().equalsIgnoreCase("pvp")) {
            event.setCancelled(true);
            API.getMessageAPI().sendPlaceMessage(player);
        }
    }
}
