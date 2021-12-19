package xyz.plocki.watchparrot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.plocki.watchparrot.WatchParrot;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class JesusListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();
    private static final Map<Player, Integer> time = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(isEnabled()) {
            if(!event.getPlayer().isInWater()) {
                if(event.getTo().clone().subtract(0,1,0).getBlock().isLiquid()) {
                    time.put(event.getPlayer(), time.getOrDefault(event.getPlayer(), 0)+1);
                }
            } else {
                if(event.getTo().clone().subtract(0,1,0).getBlock().isLiquid()) {
                    if(!event.getPlayer().isSwimming()) {
                        if((event.getFrom().getY() - event.getTo().getY()) <= 0.009) {
                            time.put(event.getPlayer(), time.getOrDefault(event.getPlayer(), 0)+1);
                        }
                    }
                }
            }
        }
    }

    public static void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WatchParrot.getPlugin(), () -> {
           Bukkit.getOnlinePlayers().forEach(player -> {
                if(time.getOrDefault(player, 0) >= 5) {
                    if(violations.containsKey(player)) {
                        violations.put(player, violations.get(player)+1);
                    } else {
                        violations.put(player, 1);
                    }
                    new Violator(new JesusListener(), player).violate();
                }
                time.remove(player);
           });
        }, 0, 5);
    }

    @Override
    public String getViolationName() {
        return "Jesus";
    }

    @Override
    public int getMaxViolations() {
        return 5;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<Player, Integer> getViolations() {
        return violations;
    }
}
