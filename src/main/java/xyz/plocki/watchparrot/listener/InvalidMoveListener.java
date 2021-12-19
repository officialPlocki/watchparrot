package xyz.plocki.watchparrot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import xyz.plocki.watchparrot.WatchParrot;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class InvalidMoveListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();
    private static final Map<Player, Integer> sneaks = new HashMap<>();

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if(isEnabled()) {
            if(event.isSneaking()) {
                sneaks.put(event.getPlayer(), sneaks.getOrDefault(event.getPlayer(), 0)+1);
            }
        }
    }

    public static void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WatchParrot.getPlugin(), () -> {
            sneaks.forEach((player, integer) -> {
                if(sneaks.getOrDefault(player, 0) >= 3) {
                    if(violations.containsKey(player)) {
                        violations.put(player, violations.get(player)+1);
                    } else {
                        violations.put(player, 1);
                    }
                    new Violator(new InvalidMoveListener(), player).violate();
                }
                sneaks.remove(player);
            });
        }, 0, 5);
    }

    @Override
    public String getViolationName() {
        return "InvalidMove";
    }

    @Override
    public int getMaxViolations() {
        return 3;
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
