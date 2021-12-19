package xyz.plocki.watchparrot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScaffoldListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        if(isEnabled()) {
            if(Objects.requireNonNull(event.getPlayer().getTargetBlock(5)).getLocation().clone().add(0,1,0).getNearbyEntities(1,1,1).contains(event.getPlayer())) {
                if(Objects.requireNonNull(event.getPlayer().getTargetBlock(5)).getLocation().getBlock().getLocation().distance(event.getBlockPlaced().getLocation()) > 0.2) {
                    event.setCancelled(true);
                    if(violations.containsKey(event.getPlayer())) {
                        violations.put(event.getPlayer(), violations.get(event.getPlayer())+1);
                    } else {
                        violations.put(event.getPlayer(), 1);
                    }
                    new Violator(new ScaffoldListener(), event.getPlayer()).violate();
                }
            }
        }
    }

    @Override
    public String getViolationName() {
        return "Scaffold";
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
