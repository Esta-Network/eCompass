package land.esta.ecompass.listener;

import land.esta.ecompass.eCompass;
import land.esta.ecompass.util.CC;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.event.Listener;

public class CompassListener implements Listener
{
    private eCompass plugin;
    private HashMap<Player, Player> compassTargets;

    public CompassListener(final eCompass instance) {
        this.plugin = instance;
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        this.compassTargets = new HashMap<Player, Player>();
    }

    // Target leaving task, I'll merge this with the bottom statement for it to be a bit cleaner
    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        for (final Map.Entry<Player, Player> entry : this.compassTargets.entrySet()) {
            if (entry.getKey() == e.getPlayer()) {
                this.compassTargets.remove(entry.getKey());
            }
            if (entry.getValue() == e.getPlayer()) {
                entry.getKey().sendMessage(CC.translate("&cYour target has either died or disconnected, you are no longer tracking anyone."));
                this.compassTargets.remove(entry.getKey());
            }
        }
    }

    // Tracking tasks
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player tracker = e.getPlayer();
        Player targetPlayer = null;
        if ((e.getAction().equals((Object)Action.RIGHT_CLICK_AIR) || e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) && tracker.getItemInHand().getType().equals((Object)Material.COMPASS)) {
            if (!tracker.hasPermission("ecompass.use")) {
                return;
            }
            Player[] onlinePlayers;
            for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
                final Player player = onlinePlayers[i];
                if (tracker.getWorld() == player.getWorld()) {
                    if (tracker != player) {
                        if (targetPlayer == null || tracker.getLocation().distance(player.getLocation()) < targetPlayer.getLocation().distance(tracker.getLocation())) {
                            if (player.getLocation().distance(tracker.getLocation()) > 50.0) {
                                targetPlayer = player;
                            }
                        }
                    }
                }
            }
            if (targetPlayer == null) {
                tracker.sendMessage(CC.translate("&cThere is no target nearby, try searching around."));
                return;
            }
            tracker.sendMessage(CC.translate("&aYou are now tracking &e" + targetPlayer.getName() + "&a."));
            this.compassTargets.put(tracker, targetPlayer);
        }
        else if ((e.getAction().equals((Object)Action.LEFT_CLICK_AIR) || e.getAction().equals((Object)Action.LEFT_CLICK_BLOCK)) && tracker.getItemInHand().getType().equals((Object)Material.COMPASS)) {
            if (!tracker.hasPermission("ecompass.use")) {
                return;
            }
            if (this.compassTargets.get(tracker) != null) {
                tracker.sendMessage(CC.translate("&e" + this.compassTargets.get(tracker).getName() + "&a is &e" + tracker.getLocation().distance(this.compassTargets.get(tracker).getLocation()) + " &ablocks away."));
            }
            else {
                tracker.sendMessage(CC.translate("&cYou aren't tracking anyone."));
            }
        }
    }

    // Get nearby target task
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (this.compassTargets.get(e.getPlayer()) != null) {
            e.getPlayer().setCompassTarget(this.compassTargets.get(e.getPlayer()).getLocation());
        }
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
            final Player tracker = onlinePlayers[i];
            final Player compassTarget = this.compassTargets.get(tracker);
            if (compassTarget == e.getPlayer()) {
                if (tracker.getWorld() == e.getPlayer().getWorld()) {
                    if (tracker.getLocation().distance(e.getPlayer().getLocation()) <= 15.0) {
                        tracker.sendMessage(CC.translate("&e" + compassTarget.getName() + " &ais close by, keep an eye out!"));
                        this.compassTargets.remove(tracker);
                    }
                    tracker.setCompassTarget(e.getPlayer().getLocation());
                }
                else {
                    tracker.sendMessage(CC.translate("&cYour target has either died or disconnected, you are no longer tracking anyone."));
                    this.compassTargets.remove(tracker);
                }
            }
        }
    }
}

