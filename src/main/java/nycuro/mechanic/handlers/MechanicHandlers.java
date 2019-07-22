package nycuro.mechanic.handlers;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.LeavesDecayEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFlintSteel;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.scheduler.Task;
import io.pocketvote.event.VoteEvent;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nycuro.API;
import nycuro.Loader;
import nycuro.database.Database;
import nycuro.database.objects.ProfileSkyblock;

import java.util.Random;

/**
 * author: NycuRO
 * SkyblockCore Project
 * API 1.0.0
 */
public class MechanicHandlers implements Listener {

    @EventHandler
    public void onInitialized(DataPacketReceiveEvent event) {
        DataPacket dataPacket = event.getPacket();
        if (dataPacket instanceof SetLocalPlayerAsInitializedPacket) {
            Player player = event.getPlayer();
            API.getMainAPI().getServer().getScheduler().scheduleDelayedRepeatingTask(new Task() {
                @Override
                public void onRun(int i) {
                    String username = player.getName();
                    int playerTime = API.getMainAPI().timers.getOrDefault(username, 1);
                    switch (playerTime) {
                        case 1:
                            API.getMessageAPI().sendFirstJoinTitle(player);
                            break;
                        case 2:
                            API.getMessageAPI().sendSecondJoinTitle(player);
                            break;
                        case 3:
                            API.getMessageAPI().sendThreeJoinTitle(player);
                            break;
                        case 4:
                            if (player.hasPermission("core.reports")) {
                                API.getMessageAPI().sendReportsTitle(player, API.getDatabase().getCountOfAllPlayersReport());
                            }
                            break;
                        default:
                            API.getMainAPI().getServer().getScheduler().cancelTask(this.getTaskId());
                    }
                    API.getMainAPI().timers.put(username, playerTime + 1);
                }
            }, 20, 20 * 3, true);
        }
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Nu merge PreLoginEvent si nici Async.
        API.getMainAPI().coords.put(player.getName(), false);
        API.getMainAPI().isOnSpawn.put(player, true);
        API.getMainAPI().isOnArena.put(player, false);
        API.getMainAPI().isOnPvP.put(player, false);
        API.getMainAPI().played.put(player.getName(), System.currentTimeMillis());
        API.getDatabase().playerExist(player.getName(), bool -> {
            if (!bool) {
                API.getDatabase().addNewPlayer(player.getName());
                Database.addDatesPlayerHub(player.getName());
            } else {
                Database.addDatesPlayerHub(player.getName());
                Database.addDatesPlayerFactions(player.getName());
            }
        });
        if (Loader.startTime.getLong(player.getUniqueId()) > 0) {
            Loader.startTime.replace(player.getUniqueId(), System.currentTimeMillis());
        } else {
            Loader.startTime.put(player.getUniqueId(), System.currentTimeMillis());
        }
        API.getMainAPI().isOnMobFarm.putIfAbsent(player, false);
    }

    @EventHandler
    public void onVoteReceive(VoteEvent event) {
        Loader.dropPartyVotes++;
        IPlayer offlinePlayer = API.getMainAPI().getServer().getOfflinePlayer(event.getPlayer());
        Random r = new Random();
        int low = 200;
        int high = 250;
        int result = r.nextInt(high-low) + low;
        ProfileSkyblock profileSkyblock = Database.profileSkyblock.get(offlinePlayer.getName());
        profileSkyblock.setExperience(profileSkyblock.getExperience() + result);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Database.saveDatesPlayerFromHub(player.getName());
        Database.saveDatesPlayerFromFactions(player.getName());
        Loader.startTime.removeLong(player.getUniqueId());
        API.getMainAPI().played.removeLong(player.getName());
        API.getMainAPI().isOnSpawn.removeBoolean(player);
        API.getMainAPI().isOnArena.removeBoolean(player);
        API.getMainAPI().isOnPvP.removeBoolean(player);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        String message = event.getMessage();
        if (message.equalsIgnoreCase("జ్ఞ\u200Cా")) {
            API.getMessageAPI().sendAbuseMessage(event.getPlayer());
            event.setCancelled(true);
        }
    }

    private static int i = 80;
    /* optimise tnt */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getBlock();
        Item item = event.getItem();
        if (block instanceof BlockTNT && event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && item instanceof ItemFlintSteel) {
            long key = Level.blockHash((int) block.getX(), (int) block.getY(), (int) block.getZ());
            Long2ObjectMap<Block> map = new Long2ObjectOpenHashMap<>();
            map.put(key, block);
            searchForTNT(map, (BlockTNT) block);
            for (Long2ObjectMap.Entry<Block> it : map.long2ObjectEntrySet()) {
                ((BlockTNT) it.getValue()).prime(i);
                i = i + 5;
            }
            event.setCancelled();
        }
    }

    private void searchForTNT(Long2ObjectMap<Block> tnt, BlockTNT current) {
        for (BlockFace blockFace : BlockFace.values()) {
            Block side = current.getSide(blockFace);
            long hash = Level.blockHash((int) side.getX(), (int) side.getY(), (int) side.getZ());
            if (side instanceof BlockTNT && !tnt.containsKey(hash)) {
                tnt.put(hash, side);
                searchForTNT(tnt, (BlockTNT) side);
            } else {
                i = 80;
            }
        }
    }
}