package nycuro.jobs.data;


import cn.nukkit.block.BlockIds;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemIds;
import cn.nukkit.player.Player;
import nycuro.database.Database;
import nycuro.jobs.CommonJob;
import nycuro.jobs.NameJob;
import nycuro.jobs.StatusJobs;
import nycuro.jobs.TypeJob;
import nycuro.utils.typo.FastRandom;

import java.util.function.Consumer;

import static nycuro.api.API.messageAPI;

/**
 * Project: SkyblockCore
 * Author: NycuRO
 */
public class MinerJob extends CommonJob {

    @Override
    public NameJob getName() {
        return NameJob.MINER;
    }

    @Override
    public int getLevelNeeded(TypeJob typeJob) {
        switch (typeJob) {
            case EASY:
                return 0;
            case MEDIUM:
                return 35;
            case HARD:
                return 55;
            case EXTREME:
                return 75;
        }
        return 0;
    }

    @Override
    public boolean isLocked(Player player, TypeJob typeJob) {
        int level = Database.profileSkyblock.get(player.getName()).getLevel();
        return level < getLevelNeeded(typeJob);
    }

    @Override
    public StatusJobs getStatus(Player player, TypeJob typeJob) {
        if (isLocked(player, typeJob)) return StatusJobs.LOCKED;
        else return StatusJobs.UNLOCKED;
    }

    @Override
    public void getReward(TypeJob typeJob, Consumer<Double> consumer) {
        if (typeJob.equals(TypeJob.EASY)) {
            FastRandom.current().doubles(1, 350, 500).findFirst().ifPresent( (j) -> {
                consumer.accept(j);
            });
        } else if (typeJob.equals(TypeJob.MEDIUM)) {
            FastRandom.current().doubles(1, 500, 800).findFirst().ifPresent( (j) -> {
                consumer.accept(j);
            });
        } else if (typeJob.equals(TypeJob.HARD)) {
            FastRandom.current().doubles(1, 800, 1200).findFirst().ifPresent( (j) -> {
                consumer.accept(j);
            });
        } else {
            FastRandom.current().doubles(1, 1200, 1500).findFirst().ifPresent( (j) -> {
                consumer.accept(j);
            });
        }
    }

    @Override
    public void processMission(Player player, TypeJob typeJob, Consumer<Object> consumer) {
        if (getStatus(player, typeJob).equals(StatusJobs.LOCKED)) {
            player.sendMessage(messageAPI.messagesObject.translateMessage("jobs.locked"));
        } else {
            if (typeJob.equals(TypeJob.EASY)) {
                Item[] itemsMap = new Item[4];
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[0] = Item.get(BlockIds.COBBLESTONE, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[1] = Item.get(BlockIds.DIRT, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[2] = Item.get(BlockIds.IRON_ORE, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[3] = Item.get(ItemIds.COAL, 0, j);
                });
                consumer.accept(itemsMap);
            } else if (typeJob.equals(TypeJob.MEDIUM)) {
                Item[] itemsMap = new Item[4];
                FastRandom.current().ints(1, 12, 36).findFirst().ifPresent((j) -> {
                    itemsMap[0] = Item.get(BlockIds.DIRT, 0, j);
                });
                FastRandom.current().ints(1, 12, 36).findFirst().ifPresent((j) -> {
                    itemsMap[1] = Item.get(BlockIds.GOLD_ORE, 0, j);
                });
                FastRandom.current().ints(1, 12, 36).findFirst().ifPresent((j) -> {
                    itemsMap[2] = Item.get(BlockIds.IRON_ORE, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[3] = Item.get(ItemIds.COAL, 0, j);
                });
                consumer.accept(itemsMap);
            } else if (typeJob.equals(TypeJob.HARD)) {
                Item[] itemsMap = new Item[6];
                FastRandom.current().ints(1, 24, 36).findFirst().ifPresent((j) -> {
                    itemsMap[0] = Item.get(BlockIds.DIRT, 0, j);
                });
                FastRandom.current().ints(1, 24, 36).findFirst().ifPresent((j) -> {
                    itemsMap[1] = Item.get(BlockIds.GOLD_ORE, 0, j);
                });
                FastRandom.current().ints(1, 24, 36).findFirst().ifPresent((j) -> {
                    itemsMap[2] = Item.get(BlockIds.IRON_ORE, 0, j);
                });
                FastRandom.current().ints(1, 5, 10).findFirst().ifPresent((j) -> {
                    itemsMap[3] = Item.get(ItemIds.DIAMOND, 0, j);
                });
                FastRandom.current().ints(1, 3, 5).findFirst().ifPresent((j) -> {
                    itemsMap[4] = Item.get(ItemIds.EMERALD, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[5] = Item.get(ItemIds.COAL, 0, j);
                });
                consumer.accept(itemsMap);
            } else {
                Item[] itemsMap = new Item[9];
                FastRandom.current().ints(1, 36, 64).findFirst().ifPresent((j) -> {
                    itemsMap[0] = Item.get(BlockIds.DIRT, 0, j);
                });
                FastRandom.current().ints(1, 36, 64).findFirst().ifPresent((j) -> {
                    itemsMap[1] = Item.get(BlockIds.COBBLESTONE, 0, j);
                });
                FastRandom.current().ints(1, 24, 36).findFirst().ifPresent((j) -> {
                    itemsMap[2] = Item.get(ItemIds.GOLD_INGOT, 0, j);
                });
                FastRandom.current().ints(1, 24, 36).findFirst().ifPresent((j) -> {
                    itemsMap[3] = Item.get(ItemIds.IRON_INGOT, 0, j);
                });
                FastRandom.current().ints(1, 5, 10).findFirst().ifPresent((j) -> {
                    itemsMap[4] = Item.get(ItemIds.DIAMOND, 0, j);
                });
                FastRandom.current().ints(1, 3, 5).findFirst().ifPresent((j) -> {
                    itemsMap[5] = Item.get(ItemIds.EMERALD, 0, j);
                });
                FastRandom.current().ints(1, 8, 24).findFirst().ifPresent((j) -> {
                    itemsMap[6] = Item.get(ItemIds.COAL, 0, j);
                });
                FastRandom.current().ints(1, 5, 8).findFirst().ifPresent((j) -> {
                    itemsMap[7] = Item.get(BlockIds.REDSTONE_BLOCK, 0, j);
                });
                FastRandom.current().ints(1, 36, 64).findFirst().ifPresent((j) -> {
                    itemsMap[8] = Item.get(ItemIds.REDSTONE, 0, j);
                });
                consumer.accept(itemsMap);
            }
        }
    }
}
