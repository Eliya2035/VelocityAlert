package me.Eliya.velocityalert.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.SoundCategory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.Sound;
import me.Eliya.velocityalert.Utilities.Color;
import me.Eliya.velocityalert.Utilities.ConfigProperties;
import me.Eliya.velocityalert.VelocityAlert;

public class AlertCMD implements SimpleCommand {
    private final ConfigProperties config;

    public AlertCMD(ConfigProperties config) {
        this.config = config;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        String soundId = (String) config.get("Sound");
        float volume = 1.0f;
        Object pitchObject = config.get("Pitch");

        float pitch = (pitchObject instanceof Number) ? ((Number) pitchObject).floatValue() : 1.0f;

        String permission = (String) config.get("Permission");
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.color((String) config.get("No-Permission")));
            return;
        }

        if (args.length > 0) {
            StringBuilder message = new StringBuilder();

            for (String text : args) {
                message.append(text).append(" ");
            }

            String alertprefix = (String) config.get("Alert-message");

            for (Player player : VelocityAlert.getProxyServer().getAllPlayers()) {

                player.sendMessage(Color.color(alertprefix.replace("{message}", message.toString())));

                if (Boolean.TRUE.equals(config.get("ActionBar"))) {
                    player.sendActionBar(Color.color(message.toString()));
                }

                if (soundId != null) {
                    try {
                        Sound sound = Sound.valueOf(soundId.toUpperCase().replace(":", "_").replace(".", "_"));
                        ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(player.getUniqueId());
                        protocolizePlayer.playSound(sound, SoundCategory.MASTER, volume, pitch);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(Color.color("&#FF3333Error: Invalid sound ID provided."));
                    }
                }

            }
            if (sender instanceof ConsoleCommandSource)
                sender.sendMessage(Color.color(alertprefix.replace("{message}", message.toString())));

        } else {
            sender.sendMessage(Color.color((String) config.get("Incorrect-Usage")));
        }
    }
}
