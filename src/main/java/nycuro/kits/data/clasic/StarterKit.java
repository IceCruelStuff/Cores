package nycuro.kits.data.clasic;


import cn.nukkit.item.Item;
import cn.nukkit.item.ItemIds;
import cn.nukkit.player.Player;
import nycuro.kits.CommonKit;
import nycuro.kits.type.*;

import static nycuro.api.API.mainAPI;

/**
 * author: NycuRO
 * RoleplayCore Project
 * API 1.0.0
 */
public class StarterKit extends CommonKit {

    @Override
    public NameKit getKit() {
        return NameKit.STARTER;
    }

    @Override
    public TypeKit getType() {
        return TypeKit.CLASSIC;
    }

    @Override
    public StatusKit getStatus(Player player) {
        return StatusKit.UNLOCKED;
    }

    @Override
    public Item getHelmet() {
        Item item = Item.get(ItemIds.LEATHER_HELMET);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.HELMET.getType());
        return item;
    }

    @Override
    public Item getArmor() {
        Item item = Item.get(ItemIds.LEATHER_CHESTPLATE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.ARMOR.getType());
        return item;
    }

    @Override
    public Item getPants() {
        Item item = Item.get(ItemIds.LEATHER_LEGGINGS);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.PANTS.getType());
        return item;
    }

    @Override
    public Item getBoots() {
        Item item = Item.get(ItemIds.LEATHER_BOOTS);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeClothes.BOOTS.getType());
        return item;
    }

    @Override
    public Item getSword() {
        Item item = Item.get(ItemIds.STONE_SWORD);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SWORD.getType());
        return item;
    }

    @Override
    public Item getPickaxe() {
        Item item = Item.get(ItemIds.STONE_PICKAXE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.PICKAXE.getType());
        return item;
    }

    @Override
    public Item getAxe() {
        Item item = Item.get(ItemIds.STONE_AXE);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.AXE.getType());
        return item;
    }

    @Override
    public Item getShovel() {
        Item item = Item.get(ItemIds.STONE_SHOVEL);
        item.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + TypeItems.SHOVEL.getType());
        return item;
    }

    @Override
    public Item[] getOtherItems() {
        Item bread = Item.get(ItemIds.BREAD, 0, 32);
        bread.setCustomName(mainAPI.symbol + getKit().getName() + mainAPI.empty + "Bread");
        return new Item[] {
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
        return true;
    }

    @Override
    public boolean passTimer(Player player) {
        return true;
    }

    @Override
    public boolean canAddKit(Player player) {
        return true;
    }

    @Override
    public void sendKit(Player player) {
        player.getInventory().setArmorContents(getArmorContents());
        player.getInventory().addItem(getInventoryContents());
        player.getInventory().addItem(getOtherItems());
    }
}

