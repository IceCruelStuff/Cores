package nycuro.tasks;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.scheduler.Task;
import nycuro.Loader;
import nycuro.ai.entity.BossEntity;
import nycuro.database.Database;
import nycuro.database.objects.ProfileProxy;
import nycuro.database.objects.ProfileSkyblock;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static nycuro.api.API.mainAPI;
import static nycuro.api.API.messageAPI;
import static nycuro.api.API.mechanicAPI;

/**
 * author: NycuRO
 * SkyblockCore Project
 * API 1.0.0
 */
public class CheckerTask extends Task {

    private AtomicBoolean randomBool = new AtomicBoolean(true);

    @Override
    public void onRun(int i) {
        for (Player player : mainAPI.getServer().getOnlinePlayers().values()) {
            ProfileSkyblock profileSkyblock = Database.profileSkyblock.get(player.getName());
            double[] d1 = new double[2];
            double[] d2 = new double[2];
            double[] d3 = new double[2];

            Location loc = player.getLocation();

            // PVP and Arena and Spawn only if is in default level, not skyblock
            if (loc.getLevel().equals(mainAPI.getServer().getDefaultLevel())) {
                // Vector3 from = new Vector3(1153, 31, 1187);
                // Vector3 to = new Vector3(1059, 0, 1280);
                // Arena Check
                d1[0] = 89; // x from
                d1[1] = -75; // x to
                d2[0] = 81; // y from
                d2[1] = 256; // y to
                d3[0] = 76; // z from
                d3[1] = -77; // z to
                Arrays.sort(d1);
                Arrays.sort(d2);
                Arrays.sort(d3);
                if (mechanicAPI.isPlayerInsideOfArea(player, d1, d2, d3)) {
                    mainAPI.isOnArena.replace(player.getUniqueId(), true);
                } else {
                    mainAPI.isOnArena.replace(player.getUniqueId(), false);
                }

                // Spawn Check
                //Vector3 vectorRA = new Vector3(1057, 5, 1175);
                //Vector3 vectorLA = new Vector3(1154, 29, 1120);
                d1[0] = 107; // x from
                d1[1] = 198; // x to
                d2[0] = 86; // y from
                d2[1] = 183; // y to
                d3[0] = 47; // z from
                d3[1] = -44; // z to
                Arrays.sort(d1);
                Arrays.sort(d2);
                Arrays.sort(d3);
                if (mechanicAPI.isPlayerInsideOfArea(player, d1, d2, d3)) {
                    mainAPI.isOnSpawn.replace(player.getUniqueId(), true);
                } else {
                    mainAPI.isOnSpawn.replace(player.getUniqueId(), false);
                }

                // Area Check
                //Vector3 vectorRA = new Vector3(1057, 5, 1175);
                //Vector3 vectorLA = new Vector3(1154, 29, 1120);
                d1[0] = 48; // x from
                d1[1] = -52; // x to
                d2[0] = 115; // y from
                d2[1] = 196; // y to
                d3[0] = -114; // z from
                d3[1] = -203; // z to
                Arrays.sort(d1);
                Arrays.sort(d2);
                Arrays.sort(d3);
                if (mechanicAPI.isPlayerInsideOfArea(player, d1, d2, d3)) {
                    mainAPI.isOnArea.replace(player.getUniqueId(), true);
                } else {
                    mainAPI.isOnArea.replace(player.getUniqueId(), false);
                }
            } else {
                // Skyblock World
                mainAPI.isOnArena.replace(player.getUniqueId(), false);
                mainAPI.isOnSpawn.replace(player.getUniqueId(), false);
                mainAPI.isOnArea.replace(player.getUniqueId(), false);
            }

            if (mechanicAPI.isOnArena(player)) {
                if (profileSkyblock.getLevel() < 10) {
                    player.sendMessage(messageAPI.sendArenaWarningMessage(player));
                    player.teleport(mainAPI.getServer().getDefaultLevel().getSpawnLocation());
                    messageAPI.sendCommandSpawnMessage(player);
                }
            }

            Instant instant = Instant.now() ;  // Capture current moment in UTC.

            ZoneId zoneId = ZoneId.of("Europe/Bucharest");
            ZonedDateTime timeZone = instant.atZone(zoneId);
            if (timeZone.getHour() == 21 && timeZone.getMinute() == 0 && timeZone.getSecond() == 0) {
                if (mechanicAPI.getBossHealth() == 0) {
                    messageAPI.sendBossSpawnedMessage(player);
                    new BossEntity();
                }
            }

            // Drop Party
            if ((1000 * 60 * 60 * 24 - (System.currentTimeMillis() - Loader.dropPartyTime)) <= 0) {
                Loader.dropPartyTime = System.currentTimeMillis();
                Loader.dropPartyVotes = 0;
            }
            if (Loader.dropPartyVotes >= 50) {
                mechanicAPI.sendDropPartyMessageBroadcast(player);
                Loader.dropPartyTime = System.currentTimeMillis();
                Loader.dropPartyVotes = 0;
                mainAPI.getServer().getScheduler().scheduleDelayedTask(new Task() {
                    @Override
                    public void onRun(int i) {
                        mechanicAPI.spawnDropParty();
                        Random r = new Random();
                        int low = 200;
                        int high = 250;
                        int result = r.nextInt(high-low) + low;
                        if (randomBool.get()) profileSkyblock.setExperience(profileSkyblock.getExperience() + result);
                        int lowGem = 1;
                        int maxGem = 3;
                        int resultGem = r.nextInt(maxGem - lowGem) + lowGem;
                        ProfileProxy profileProxy = Database.profileProxy.get(player.getName());
                        if (!randomBool.get()) profileProxy.setGems(profileProxy.getGems() + resultGem);
                        int lowCoins = 100;
                        int maxCoins = 500;
                        int resultCoins = r.nextInt(maxCoins - lowCoins) + lowCoins;
                        if (randomBool.get()) profileSkyblock.setDollars(profileSkyblock.getDollars() + resultCoins);
                        if (!randomBool.get()) profileProxy.setTime(profileProxy.getTime() + 1000 * 60 * 15);
                        if (randomBool.get()) {
                            player.sendPopup("§3+" + resultCoins + " DOLLARS" + "\n" +
                                    "+" + result + " EXP");
                        } else {
                            player.sendPopup("§3+" + resultGem + " GEMS" + "\n" +
                                    "+15min");
                        }
                        if (randomBool.get()) {
                            randomBool.set(false);
                        } else if (!randomBool.get()) {
                            randomBool.set(true);
                        }
                    }
                }, 20 * 60, true);
            }
        }
    }
}
