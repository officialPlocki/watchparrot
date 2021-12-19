package xyz.plocki.watchparrot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.plocki.watchparrot.WatchParrot;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class AutoclickerListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();
    private final static Map<Player, Integer> cps = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if(isEnabled()) {
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                cps.put(event.getPlayer(), cps.getOrDefault(event.getPlayer(), 0)+1);
            }
        }
    }

    public static void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WatchParrot.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(cps.getOrDefault(player, 0) >= 3) {
                    if(violations.containsKey(player)) {
                        violations.put(player, violations.get(player)+1);
                    } else {
                        violations.put(player, 1);
                    }
                    new Violator(new AutoclickerListener(), player);
                }
                cps.remove(player);
            });
        }, 0, 5);
    }

    @Override
    public String getViolationName() {
        return "AutoClicker";
    }

    @Override
    public int getMaxViolations() {
        return 2;
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
