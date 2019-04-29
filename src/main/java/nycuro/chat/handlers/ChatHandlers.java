package nycuro.chat.handlers;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.utils.TextFormat;
import me.lucko.luckperms.api.LuckPermsApi;
import nycuro.API;
import nycuro.chat.ChatFormat;

import java.util.Objects;

/**
 * author: uselesswaifu
 * HubCore Project
 * API 1.0.0
 */
public class ChatHandlers implements Listener {

    private LuckPermsApi api;
    private int count = 0;

    public ChatHandlers() {
        RegisteredServiceProvider<LuckPermsApi> provider = API.getMainAPI().getServer().getServiceManager().getProvider(LuckPermsApi.class);
        if (provider != null) {
            api = provider.getProvider();
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        count++;
        String group = Objects.requireNonNull(api.getUser(player.getName())).getPrimaryGroup().toUpperCase();
        String s = ChatFormat.valueOf(group).toString();
        s = s.replace("%name", player.getName());
        s = s.replace("%msg", event.getMessage());
        if (count % 2 == 0)
            s = s.replace("%slash", "\\");
        else
            s = s.replace("%slash", "/");
        s = s.replace("%rank", API.getMessageAPI().getRankMessage(player));
        s = TextFormat.colorize(s);
        event.setFormat(s);
    }
}
