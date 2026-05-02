package ch.patrickjurt.rlnavigation;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("RLNavigation has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RLNavigation has been disabled!");
    }
}
