package ch.patrickjurt.rlnavigation;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("RLNavigation has been enabled!");

        for (World world : getServer().getWorlds()) {
            applyReducedDebugInfo(world);
            applyLocatorBarGamerule(world);
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        applyReducedDebugInfo(event.getWorld());
        applyLocatorBarGamerule(event.getWorld());
    }

    private void applyReducedDebugInfo(World world) {
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        getLogger().info("Disabled F3 debug info for world: " + world.getName());
    }

    private void applyLocatorBarGamerule(World world) {
        // The locator bar gamerule is named 'locator_bar'
        GameRule<?> raw = GameRule.getByName("locator_bar");
        if (raw == null) {
            getLogger().info("Locator-bar gamerule 'locator_bar' not present on this server for world: " + world.getName());
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            GameRule<Boolean> rule = (GameRule<Boolean>) raw;
            world.setGameRule(rule, false);
            getLogger().info("Disabled locator bar gamerule 'locator_bar' for world: " + world.getName());
        } catch (ClassCastException e) {
            getLogger().warning("Locator-bar gamerule 'locator_bar' is not a boolean for world: " + world.getName());
        }
    }

    // Map-tracking removal logic removed per user request

    @Override
    public void onDisable() {
        getLogger().info("RLNavigation has been disabled!");
    }
}
