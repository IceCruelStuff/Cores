package nycuro.kits.data.clasic;


import cn.nukkit.block.BlockIds;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemIds;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.player.Player;
import nycuro.api.API;
import nycuro.database.Database;
import nycuro.database.objects.ProfileSkyblock;
import nycuro.kits.CommonKit;
import nycuro.kits.type.*;

import static nycuro.api.API.mainAPI;
import static nycuro.api.API.messageAPI;

/**
 * author: NycuRO
 * RoleplayCore Project
 * API 1.0.0
 */
public class KnightKit extends CommonKit {

    @Override
    public NameKit getKit() {
        return NameKit.KNIGHT;
    }

    @Override
    public TypeKit getType() {
        return TypeKit.CLASSIC;
    }

    @Override
    public double getPrice() {
        return 5000d;
    }

    @Override
    public StatusKit getStatus(Player player) {
        return StatusKit.UNLOCKED;
    }

    @Override
    public Item getHelmet() {
        Item item = Item.get(ItemIds.DIAMOND_HELMET);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.HELMET.getType());
        return item;
    }

    @Override
    public Item getArmor() {
        Item item = Item.get(ItemIds.DIAMOND_CHESTPLATE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.ARMOR.getType());
        return item;
    }

    @Override
    public Item getPants() {
        Item item = Item.get(ItemIds.DIAMOND_LEGGINGS);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.PANTS.getType());
        return item;
    }

    @Override
    public Item getBoots() {
        Item item = Item.get(ItemIds.DIAMOND_BOOTS);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.BOOTS.getType());
        return item;
    }

    @Override
    public Item getSword() {
        Item item = Item.get(ItemIds.STONE_SWORD);
        item.addEnchantment(Enchantment.get(Enchantment.ID_DAMAGE_ALL).setLevel(1));
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SWORD.getType());
        return item;
    }

    @Override
    public Item getPickaxe() {
        Item item = Item.get(ItemIds.STONE_PICKAXE);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.PICKAXE.getType());
        return item;
    }

    @Override
    public Item getAxe() {
        Item item = Item.get(ItemIds.STONE_AXE);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.AXE.getType());
        return item;
    }

    @Override
    public Item getShovel() {
        Item item = Item.get(ItemIds.STONE_SHOVEL);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SHOVEL.getType());
        return item;
    }

    @Override
    public Item[] getOtherItems() {
        Item obsidian = Item.get(BlockIds.OBSIDIAN, 0, 32);
        Item tnt = Item.get(BlockIds.TNT, 0, 16);
        Item bread = Item.get(ItemIds.BREAD, 0, 32);
        bread.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Bread");
        tnt.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "TNT");
        obsidian.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Obsidian");
        return new Item[] {
                obsidian,
                tnt,
                bread
        };
    }

    @Override
    public Item[] getArmorContents() {
        return new Item[] {
                this.getHelmet(),
                this.getArmor(),
                this.getPants(),
                this.getBoots()

        };
    }

    @Override
    public Item[] getInventoryContents() {
        return new Item[] {
                this.getSword(),
                this.getPickaxe(),
                this.getAxe(),
                this.getShovel()

        };
    }

    @Override
    public boolean hasEnoughDollars(Player player) {
        ProfileSkyblock profileSkyblock = Database.profileSkyblock.get(player.getName());
        double dollars = profileSkyblock.getDollars();
        return getPrice() < dollars;
    }

    @Override
    public boolean canAddKit(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return (getArmorContents().length + getInventoryContents().length + getOtherItems().length) < 36 - playerInventory.getContents().size();
    }

    @Override
    public long getTimer() {
        return 1000 * 60 * 60 * 24;
    }

    @Override
    public boolean passTimer(Player player) {
        ProfileSkyblock profileSkyblock = Database.profileSkyblock.get(player.getName());
        long time = profileSkyblock.getCooldown();
        return (getTimer() - (System.currentTimeMillis() - time)) <= 0;
    }

    @Override
    public void sendKit(Player player) {
        ProfileSkyblock profileSkyblock = Database.profileSkyblock.get(player.getName());
        if (passTimer(player)) {
            if (canAddKit(player)) {
                if (hasEnoughDollars(player)) {
                    player.getInventory().setArmorContents(getArmorContents());
                    player.getInventory().addItem(getInventoryContents());
                    player.getInventory().addItem(getOtherItems());
                    profileSkyblock.setCooldown(System.currentTimeMillis());
                    profileSkyblock.setDollars(profileSkyblock.getDollars() - getPrice());
                    player.sendMessage(messageAPI.messagesObject.translateMessage("kits.receive", getKit().getName()));
                } else {
                    double dollars = profileSkyblock.getDollars();
                    player.sendMessage(messageAPI.messagesObject.translateMessage("generic.money.enough", mainAPI.emptyNoSpace + dollars,
                            mainAPI.emptyNoSpace + (getPrice() - dollars)));
                }
            } else {
                player.sendMessage(messageAPI.messagesObject.translateMessage("generic.inventory.get.error"));
            }
        } else {
            long time = profileSkyblock.getCooldown();
            player.sendMessage(messageAPI.messagesObject.translateMessage("generic.timegone",
                    API.time(System.currentTimeMillis() - time), mainAPI.emptyNoSpace + getTimer()));
        }
    }
}