package xyz.plocki.watchparrot.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import xyz.plocki.watchparrot.WatchParrot;
import xyz.plocki.watchparrot.util.violation.IViolation;
import xyz.plocki.watchparrot.util.violation.Violator;

import java.util.HashMap;
import java.util.Map;

public class FlyListener implements Listener, IViolation {

    private static final Map<Player, Integer> airtime = new HashMap<>();

    private static final Map<Player, Integer> violations = new HashMap<>();

    public static void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WatchParrot.getPlugin(), () -> {
            for(Player all : Bukkit.getOnlinePlayers()) {
                //all.sendMessage(airtime.getOrDefault(all, 0)+"");
                if(all.getGameMode().equals(GameMode.CREATIVE) || all.getGameMode().equals(GameMode.SPECTATOR)) {
                    airtime.remove(all);
                }
                if(!all.isOnGround()) {
                    if(airtime.getOrDefault(all, 0) >= 6) {
                        airtime.remove(all);
                        if(violations.containsKey(all)) {
                            violations.put(all, violations.get(all)+1);
                        } else {
                            violations.put(all, 1);
                        }
                        new Violator(new FlyListener(), all).violate();
                    }
                    if(all.getLocation().clone().subtract(0,1,0).getBlock().getType().isAir() && all.getLocation().clone().subtract(0,2,0).getBlock().getType().isAir()) {
                        if(airtime.containsKey(all)) {
                            airtime.put(all, airtime.get(all)+1);
                        } else {
                            airtime.put(all, 1);
                        }
                    }
                } else {
                    airtime.remove(all);
                }
            }
        }, 0,15);
    }

    @Override
    public String getViolationName() {
        return "Fly";
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
