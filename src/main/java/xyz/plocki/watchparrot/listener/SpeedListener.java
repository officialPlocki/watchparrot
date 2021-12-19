package xyz.plocki.watchparrot.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class SpeedListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(isEnabled()) {
            if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                return;
            }
            if(((int)event.getFrom().getY()) == ((int)event.getTo().getY())) {
                if(event.getFrom().distance(event.getTo()) > 1.05) {
                    event.setCancelled(true);
                    if (violations.containsKey(event.getPlayer())) {
                        violations.put(event.getPlayer(), violations.get(event.getPlayer()) + 1);
                    } else {
                        violations.put(event.getPlayer(), 1);
                    }
                    new Violator(new SpeedListener(), event.getPlayer()).violate();
                }
            }
            if(event.getTo().getY() == event.getFrom().getY()) {
                if (event.getFrom().distance(event.getTo()) > 0.475) {
                    event.setCancelled(true);
                    if (violations.containsKey(event.getPlayer())) {
                        violations.put(event.getPlayer(), violations.get(event.getPlayer()) + 1);
                    } else {
                        violations.put(event.getPlayer(), 1);
                    }
                    new Violator(new SpeedListener(), event.getPlayer()).violate();
                }
            }

            if(event.getPlayer().isOnGround()) {
                if(event.getFrom().distance(event.getTo()) >= 0.7) {
                    event.setCancelled(true);
                    if(violations.containsKey(event.getPlayer())) {
                        violations.put(event.getPlayer(), violations.get(event.getPlayer())+1);
                    } else {
                        violations.put(event.getPlayer(), 1);
                    }
                    new Violator(new SpeedListener(), event.getPlayer()).violate();
                }
            }
        }
    }

    @Override
    public String getViolationName() {
        return "Speed/Jump";
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
