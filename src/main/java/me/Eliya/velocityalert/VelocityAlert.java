package me.Eliya.velocityalert;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.Eliya.velocityalert.Commands.AlertCMD;
import me.Eliya.velocityalert.Utilities.ConfigProperties;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "velocityalert",
        name = "VelocityAlert",
        version = "1.0",
        authors = {"Eliya2035"}
)

public class VelocityAlert {

    private static ProxyServer proxyServer;
    private Logger logger;
    private final ConfigProperties configProperties;

    @Inject
    public VelocityAlert(ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        VelocityAlert.proxyServer = proxyServer;
        this.configProperties = new ConfigProperties(dataDirectory.resolve("Config.yml"));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getCommandManager().register("alert", new AlertCMD(this.configProperties));
        configProperties.loadConfig();
    }

    public static ProxyServer getProxyServer() {
        return proxyServer;
    }
}
