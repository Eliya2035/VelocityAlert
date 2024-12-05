package me.Eliya.velocityalert.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.Eliya.velocityalert.Utilities.ConfigProperties;
import me.Eliya.velocityalert.VelocityAlert;
import me.Eliya.velocityalert.Utilities.Color;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class AlertCMD implements SimpleCommand {
    private final ConfigProperties config;

    public AlertCMD(ConfigProperties config) {
        this.config = config;
    }

    @Override
    public void execute(Invocation invocation) {

        Player player = (Player) invocation.source();

        String[] args = (String[]) invocation.arguments();

        String soundId = (String) config.get("Sound");
        float volume = 1.0f;
        float pitch = (float) config.get("Pitch");

        if (!player.hasPermission((String) config.get("Permission"))) {
            player.sendMessage(Color.color((String) config.get("No-Permission")));
            return;
        }
        
        if (args.length > 0) {
            for (Player players : VelocityAlert.getProxyServer().getAllPlayers()) {
                if (soundId != null)
                    players.playSound(Sound.sound(Key.key(soundId), Sound.Source.MASTER, volume, pitch));

                StringBuilder message = new StringBuilder();

                for (int i = 0; i < args.length; i++) {
                    String text = args[i];
                    message.append(text).append(" ");
                }

                String alertprefix = (String) config.get("Alert-message");
                players.sendMessage(Color.color(alertprefix.replace("{message}", message)));

                if (config.get("ActionBar").equals(true))
                    players.sendActionBar(Color.color(""+message));

                players.sendMessage(Color.color("\n"));
            }
        }

        else {
            player.sendMessage(Color.color((String) config.get("Incorrect-Usage")));
        }
    }
}
