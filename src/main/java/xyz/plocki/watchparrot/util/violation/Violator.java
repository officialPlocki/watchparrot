package xyz.plocki.watchparrot.util.violation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.plocki.watchparrot.WatchParrot;

import java.util.ArrayList;
import java.util.List;

public class Violator {

    private final IViolation violation;
    private final Player player;

    private static final List<IViolation> checks = new ArrayList<>();

    public Violator(IViolation violation, Player player) {
        this.violation = violation;
        this.player = player;
        if(!checks.contains(violation)) {
            checks.add(violation);
        }
    }

    public void violate() {
        Bukkit.getOnlinePlayers().forEach(all -> {
            if(violation.isEnabled()) {
                if(all.hasPermission("wp.see")) {
                    if(violation.getViolations().getOrDefault(player, 0) == (violation.getMaxViolations()-1)) {
                        all.sendMessage("§c§lWatch§9§lParrot §8» §e" + player.getName() + "§7 failed §b" + violation.getViolationName() + " §8(§a" + violation.getViolations().getOrDefault(player, 1) + "§8, §cprobably a cheater!§8)");
                    } else if(violation.getViolations().getOrDefault(player, 0) == 1) {
                        all.sendMessage("§c§lWatch§9§lParrot §8» §e" + player.getName() + "§7 failed §b" + violation.getViolationName() + " §8(§a" + violation.getViolations().getOrDefault(player, 1) + "§8, §cprobably a false-flag§8)");
                    } else {
                        all.sendMessage("§c§lWatch§9§lParrot §8» §e" + player.getName() + "§7 failed §b" + violation.getViolationName() + " §8(§a" + violation.getViolations().getOrDefault(player, 1) + "§8)");
                    }
                    if(violation.getViolations().getOrDefault(player, 1) >= violation.getMaxViolations()) {
                        all.sendMessage("§c§lWatch§9§lParrot §8» §e" + player.getName() + "§7 was kicked for Cheating.");
                        violation.getViolations().remove(player);
                        if(!player.hasPermission("wp.bypass.kick")) {
                            player.kickPlayer("§8» §eYou was kicked for Cheating. §8«\n\n\n§7This server is §6protected §7by §c§lWatch§9§lParrot§7.\n§bhttps://plocki.xyz");
                        }
                    }
                }
            }
        });
    }

    public static void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WatchParrot.getPlugin(), () -> {
            checks.forEach(checks2 -> {
                checks2.getViolations().forEach((player, integer) -> {
                    checks2.getViolations().put(player, 0);
                });
            });
        }, 0, 20*15);
    }

}
