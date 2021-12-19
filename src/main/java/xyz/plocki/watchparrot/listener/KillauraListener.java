package xyz.plocki.watchparrot.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.*;

public class KillauraListener implements Listener, IViolation {

    private static final Map<Player, Integer> violations = new HashMap<>();

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if(isEnabled()) {
            if(event.getDamager() instanceof Player) {
                Player target = getTargetPlayer((Player) event.getDamager());
                if(!(target == event.getEntity())) {
                    if(violations.containsKey((Player) event.getDamager())) {
                        violations.put((Player) event.getDamager(), violations.getOrDefault((Player) event.getDamager(), 0)+1);
                    } else {
                        violations.put((Player) event.getDamager(), 1);
                    }
                    new Violator(new KillauraListener(), (Player) event.getDamager()).violate();
                }
            }
        }
    }

    private Player getTargetPlayer(final Player player) {
        return getTarget(player, player.getWorld().getPlayers());
    }

    private <T extends Entity> T getTarget(final Entity entity, final Iterable<T> entities) {
        if (entity == null)
            return null;
        T target = null;
        final double threshold = 2.5;
        for (final T other : entities) {
            final Vector n = other.getLocation().toVector()
                    .subtract(entity.getLocation().toVector());
            if (entity.getLocation().getDirection().normalize().crossProduct(n)
                    .lengthSquared() < threshold
                    && n.normalize().dot(
                    entity.getLocation().getDirection().normalize()) >= 0.15) {
                if (target == null
                        || target.getLocation().distanceSquared(
                        entity.getLocation()) > other.getLocation()
                        .distanceSquared(entity.getLocation()))
                    target = other;
            }
        }
        return target;
    }
    @Override
    public String getViolationName() {
        return "Killaura";
    }

    @Override
    public int getMaxViolations() {
        return 10;
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
