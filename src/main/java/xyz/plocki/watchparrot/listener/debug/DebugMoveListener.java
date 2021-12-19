package xyz.plocki.watchparrot.listener.debug;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class DebugMoveListener implements Listener {

    private static final Map<Player, Double> move = new HashMap<>();

    @EventHandler
    public void onDebugMove(PlayerMoveEvent event) {
        double m = event.getFrom().distance(event.getTo());
        if(move.getOrDefault(event.getPlayer(), 0.0) < m) {
            event.getPlayer().sendMessage(""+m);
            move.put(event.getPlayer(), m);
        }
        event.getPlayer().sendMessage("in water: " + event.getPlayer().isInWater());
        event.getPlayer().sendMessage("in over liquid: " + event.getPlayer().getLocation().clone().subtract(0,1,0).getBlock().isLiquid());
        Bukkit.getConsoleSender().sendMessage(event.getPlayer().getName() + ": " + m);
        Bukkit.getConsoleSender().sendMessage(event.getTo().toString());
    }

}
