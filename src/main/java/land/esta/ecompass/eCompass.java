package land.esta.ecompass;

import land.esta.ecompass.listener.CompassListener;
import land.esta.ecompass.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class eCompass extends JavaPlugin implements Listener {
    eCompass plugin = this;
    private static eCompass instance;

    public void onEnable() {
        instance = this;
        new CompassListener(this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("§aeCompass has successfully enabled.");
    }

    public void onDisable () {
        Bukkit.getLogger().info("§aeCompass has successfully disabled");
    }

    public static eCompass getInstance() {
        return instance;
    }

    // GPL v3 Public License
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().trim().equalsIgnoreCase("/ecompass")) {
            e.getPlayer().sendMessage(CC.translate("&4&m--*--------------------------------*--"));
            e.getPlayer().sendMessage(CC.translate("&c&l Info:"));
            e.getPlayer().sendMessage(CC.translate("&c  ● &fName: &ceCompass "));
            e.getPlayer().sendMessage(CC.translate("&c  ● &fAuthor: "));
            e.getPlayer().sendMessage(CC.translate("&c    &4▶ &ctdvne"));
            e.getPlayer().sendMessage(CC.translate("&c  ● &fVersion: &cv" + plugin.getDescription().getVersion()));
            e.getPlayer().sendMessage(CC.translate("&c  ● &fWebsite: &c" + plugin.getDescription().getWebsite()));
            e.getPlayer().sendMessage(CC.translate("&c  ● &fGithub: &cGithub.com/Esta-Network/eCompass"));
            e.getPlayer().sendMessage(CC.translate("&4&m--*--------------------------------*--"));
            e.setCancelled(true);
        }
    }
}
