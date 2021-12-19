package xyz.plocki.watchparrot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class ReachListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if(isEnabled()) {
            Player damager = Bukkit.getPlayer(event.getDamager().getName());
            assert damager != null;
            if(damager.getLocation().distance(event.getEntity().getLocation()) >= 4.5) {
                violations.put(damager, violations.getOrDefault(damager, 0)+1);
                new Violator(new ReachListener(), damager);
            }
        }
    }

    @Override
    public String getViolationName() {
        return "Reach";
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
