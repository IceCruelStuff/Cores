package nycuro.economy.commands.data.tops;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import nycuro.database.Database;
import nycuro.economy.commands.CommandBaseEconomy;

import static nycuro.api.API.messageAPI;

/**
 * author: NycuRO
 * FactionsCore Project
 * API 1.0.0
 */
public class TopCoinsCommand extends CommandBaseEconomy {

    public TopCoinsCommand() {
        super("home");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length == 0) {
            player.sendMessage("§7» §6Top Coins §7«");
            player.sendMessage("§c1) §a" + Database.scoreboardcoinsName.get(1) + " -> " + Database.scoreboardcoinsValue.get(1));
            player.sendMessage("§c2) §a" + Database.scoreboardcoinsName.get(2) + " -> " + Database.scoreboardcoinsValue.get(2));
            player.sendMessage("§c3) §a" + Database.scoreboardcoinsName.get(3) + " -> " + Database.scoreboardcoinsValue.get(3));
            player.sendMessage("§c4) §a" + Database.scoreboardcoinsName.get(4) + " -> " + Database.scoreboardcoinsValue.get(4));
            player.sendMessage("§c5) §a" + Database.scoreboardcoinsName.get(5) + " -> " + Database.scoreboardcoinsValue.get(5));
            player.sendMessage("§c6) §a" + Database.scoreboardcoinsName.get(6) + " -> " + Database.scoreboardcoinsValue.get(6));
            player.sendMessage("§c7) §a" + Database.scoreboardcoinsName.get(7) + " -> " + Database.scoreboardcoinsValue.get(7));
            player.sendMessage("§c8) §a" + Database.scoreboardcoinsName.get(8) + " -> " + Database.scoreboardcoinsValue.get(8));
            player.sendMessage("§c9) §a" + Database.scoreboardcoinsName.get(9) + " -> " + Database.scoreboardcoinsValue.get(9));
            player.sendMessage("§c10) §a" + Database.scoreboardcoinsName.get(10) + " -> " + Database.scoreboardcoinsValue.get(10));
        } else {
            messageAPI.topMoneyExceptionMessage(player);
            return true;
        }
        return true;
    }
}