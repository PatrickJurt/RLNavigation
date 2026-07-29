package ch.patrickjurt.rlnavigation;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class Main extends JavaPlugin implements Listener {
    private Team hideNameTeam;

    @Override
    public void onEnable() {
        getLogger().info("RLNavigation has been enabled!");

        for (World world : getServer().getWorlds()) {
            applyReducedDebugInfo(world);
            applyLocatorBarGamerule(world);
        }
        getServer().getPluginManager().registerEvents(this, this);

        // Prepare scoreboard team to hide name tags above players (keeps tab names)
        try {
            Scoreboard board = getServer().getScoreboardManager() != null
                    ? getServer().getScoreboardManager().getMainScoreboard()
                    : null;
            if (board != null) {
                hideNameTeam = board.getTeam("rl_no_tag");
                if (hideNameTeam == null) hideNameTeam = board.registerNewTeam("rl_no_tag");
                hideNameTeam.setNameTagVisibility(NameTagVisibility.NEVER);
                // add existing online players
                for (org.bukkit.entity.Player p : getServer().getOnlinePlayers()) {
                    try {
                        hideNameTeam.addEntry(p.getName());
                    } catch (Throwable ignored) {
                    }
                }
                getLogger().info("Name tags above players will be hidden via team 'rl_no_tag'.");
            } else {
                getLogger().warning("Scoreboard manager unavailable; cannot hide name tags.");
            }
        } catch (Throwable t) {
            getLogger().warning("Failed to prepare name-tag-hiding team: " + t.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (hideNameTeam != null) {
            try {
                hideNameTeam.addEntry(event.getPlayer().getName());
            } catch (Throwable ignored) {
            }
        }
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
