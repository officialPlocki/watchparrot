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

public class JumpListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }
        if(event.getTo().getY() > event.getFrom().getY()) {
            if(event.getFrom().distance(event.getTo()) > 1.35D) {
                event.getPlayer().teleport(event.getFrom().clone().add(0,1,0));
                if(violations.containsKey(event.getPlayer())) {
                    violations.put(event.getPlayer(), violations.get(event.getPlayer())+1);
                } else {
                    violations.put(event.getPlayer(), 1);
                }
                new Violator(new JumpListener(), event.getPlayer()).violate();
            }
        } else {
            if(event.getFrom().distance(event.getTo()) > 2.315D) {
                event.getPlayer().teleport(event.getFrom());
                if(violations.containsKey(event.getPlayer())) {
                    violations.put(event.getPlayer(), violations.get(event.getPlayer())+1);
                } else {
                    violations.put(event.getPlayer(), 1);
                }
                new Violator(new JumpListener(), event.getPlayer()).violate();
            }
        }
    }

    @Override
    public String getViolationName() {
        return "Jump";
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
