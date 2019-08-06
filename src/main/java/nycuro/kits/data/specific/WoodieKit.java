package nycuro.kits.data.specific;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import nycuro.database.Database;
import nycuro.database.objects.ProfileSkyblock;
import nycuro.kits.CommonKit;
import nycuro.kits.type.NameKit;
import nycuro.kits.type.StatusKit;
import nycuro.kits.type.TypeItems;
import nycuro.kits.type.TypeKit;

import java.util.ArrayList;

import static nycuro.api.API.mainAPI;
import static nycuro.api.API.messageAPI;

/**
 * author: NycuRO
 * SkyblockCore Project
 * API 1.0.0
 */
public class WoodieKit extends CommonKit {

    @Override
    public NameKit getKit() {
        return NameKit.WOODIE;
    }

    @Override
    public TypeKit getType() {
        return TypeKit.SPECIFIC;
    }

    @Override
    public double getPrice() {
        return 1000d;
    }

    @Override
    public StatusKit getStatus(Player player) {
        return StatusKit.UNLOCKED;
    }

    @Override
    public Item getSword() {
        Item item = Item.get(Item.STONE_SWORD);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SWORD.getType());
        return item;
    }

    @Override
    public Item getPickaxe() {
        Item item = Item.get(Item.STONE_PICKAXE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.PICKAXE.getType());
        return item;
    }

    @Override
    public Item getAxe() {
        Item item = Item.get(Item.STONE_AXE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.AXE.getType());
        return item;
    }

    @Override
    public Item getShovel() {
        Item item = Item.get(Item.STONE_SHOVEL);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SHOVEL.getType());
        return item;
    }

    @Override
    public Item[] getOtherItems() {
        Item steak = Item.get(Item.STEAK, 0, 32);
        steak.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Steak");
        ArrayList<Item> itemslog1 = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            Item item = Item.get(Item.WOOD, i, 16);
            item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Wood Type " + i);
            itemslog1.add(item);
        }
        ArrayList<Item> itemslog2 = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            Item item = Item.get(Item.WOOD2, i, 16);
            item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Wood2 Type " + i);
            itemslog2.add(item);
        }
        Item[] item = new Item[7];
        for (int i = 0; i <= 3; i++) {
            item[i] = itemslog1.get(i);
        }
        item[4] = itemslog2.get(0);
        item[5] = itemslog2.get(1);
        item[6] = steak;
        return item;
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
        for (Item item : playerInventory.getArmorContents()) {
            if (item.getId() != 0) {
                return false;
            }
        }
        return (getInventoryContents().length + getOtherItems().length) < 36 - playerInventory.getContents().size();
    }

    @Override
    public long getTimer() {
        return 1000 * 60 * 20;
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
                    player.getInventory().addItem(getInventoryContents());
                    player.getInventory().addItem(getOtherItems());
                    profileSkyblock.setCooldown(System.currentTimeMillis());
                    profileSkyblock.setDollars(profileSkyblock.getDollars() - getPrice());
                    messageAPI.sendReceiveKitMessage(player, getKit());
                } else {
                    double dollars = profileSkyblock.getDollars();
                    messageAPI.sendUnsuficientMoneyMessage(player, getPrice() - dollars);
                }
            } else {
                messageAPI.sendFullInventoryMessage(player);
            }
        } else {
            long time = profileSkyblock.getCooldown();
            messageAPI.sendCooldownMessage(player, System.currentTimeMillis() - time, getTimer());
        }
    }
}
