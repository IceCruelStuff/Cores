package nycuro.teleport.commands.data;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import nycuro.teleport.commands.CommandBaseTeleportation;
import nycuro.teleport.objects.TPRequest;

import static nycuro.api.API.mainAPI;
import static nycuro.api.API.messageAPI;
import static nycuro.api.API.teleportationAPI;

public class TPAcceptCommand extends CommandBaseTeleportation {

    public TPAcceptCommand() {
        super("tpaccept");
        this.setAliases(new String[]{"tpyes"});

        // command parameters
        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player to = (Player) sender;
        if (teleportationAPI.getLatestTPRequestTo(to) == null) {
            sender.sendMessage(messageAPI.messagesObject.getMessages().get("commands.tpaccept.noRequest"));
            return false;
        }
        TPRequest request;
        Player from;
        if (args.length == 0) {
            if ((request = teleportationAPI.getLatestTPRequestTo(to)) == null) {
                sender.sendMessage(messageAPI.messagesObject.getMessages().get("commands.tpaccept.unavailable"));
                return false;
            }
            from = request.getFrom();
        } else {
            from = mainAPI.getServer().getPlayer(args[0]);
            if (from == null) {
                sender.sendMessage(messageAPI.messagesObject.translateMessage("commands.generic.player.notfound", args[0]));
                return false;
            }
            if ((request = teleportationAPI.getTPRequestBetween(from, to)) != null) {
                sender.sendMessage(messageAPI.messagesObject.translateMessage("commands.tpaccept.noRequestFrom", from.getName()));
                return false;
            }
        }
        if (request == null) {
            sender.sendMessage(messageAPI.messagesObject.getMessages().get("commands.tpaccept.noRequest"));
            return false;
        }
        from.sendMessage(messageAPI.messagesObject.translateMessage("commands.tpaccept.accepted", to.getName()));
        sender.sendMessage(messageAPI.messagesObject.getMessages().get("commands.generic.teleporting"));
        if (request.isTo()) {
            teleportationAPI.onTP(from, request.getLocation(), messageAPI.messagesObject.getMessages().get("commands.generic.teleporting"));
        } else {
            teleportationAPI.onTP(to, request.getLocation(), messageAPI.messagesObject.getMessages().get("commands.generic.teleporting"));
        }
        teleportationAPI.removeTPRequestBetween(from, to);
        return true;
    }
}
