package xyz.plocki.watchparrot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.plocki.watchparrot.listener.*;
import xyz.plocki.watchparrot.listener.debug.DebugMoveListener;
import xyz.plocki.watchparrot.util.violation.Violator;

public final class WatchParrot extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        //check registry
        Bukkit.getPluginManager().registerEvents(new JumpListener(), this);
        Bukkit.getPluginManager().registerEvents(new FlyListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpeedListener(), this);
        Bukkit.getPluginManager().registerEvents(new ScaffoldListener(), this);
        Bukkit.getPluginManager().registerEvents(new InvalidMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new JesusListener(), this);
        Bukkit.getPluginManager().registerEvents(new AutoclickerListener(), this);
        Bukkit.getPluginManager().registerEvents(new KillauraListener(), this);
        Bukkit.getPluginManager().registerEvents(new ReachListener(), this);

        //debug registry
        //Bukkit.getPluginManager().registerEvents(new DebugMoveListener(), this);


        //scheduler registry
        if(new JesusListener().isEnabled()) {
            JesusListener.init();
        }
        if(new FlyListener().isEnabled()) {
            FlyListener.init();
        }
        if(new InvalidMoveListener().isEnabled()) {
            InvalidMoveListener.init();
        }
        if(new AutoclickerListener().isEnabled()) {
            AutoclickerListener.init();
        }
        Violator.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
