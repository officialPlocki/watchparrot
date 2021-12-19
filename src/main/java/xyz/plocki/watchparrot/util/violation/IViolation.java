package xyz.plocki.watchparrot.util.violation;

import org.bukkit.entity.Player;

import java.util.Map;

public interface IViolation {

    String getViolationName();

    int getMaxViolations();

    boolean isEnabled();

    Map<Player, Integer> getViolations();

}
