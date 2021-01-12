package us.simplecraft.VanishPlaceholders;

import com.earth2me.essentials.Essentials;
import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

import java.util.UUID;

public class VanishPlaceholders extends PlaceholderExpansion {

    static String[] plugins = {"SuperVanish", "PremiumVanish", "VanishNoPacket", "Essentials"};
    String pluginName;
    // Bukkit.getPluginManager().isPluginEnabled("Essentials") || Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish") || Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")
    private boolean ess, sv, vnp;
    Essentials essPlugin;
    VanishManager vanishManager;

    @Override
    public boolean canRegister() {
        for(String e : plugins ) {
            if(Bukkit.getPluginManager().getPlugin(e) != null) {
                pluginName = e;
                if(pluginName.equals("VanishNoPacket")) {
                    if(((VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket")).getManager() != null) {
                        return init();
                    }
                    else return false;
                }
                return init();
            }
        }
        Bukkit.getLogger().warning("Vanish Placeholders - Could not find a supported plugin!");
        return false;
    }

    /*@Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }
        /*new BukkitRunnable() {
            @Override
            public void run() {
                init();
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("PlaceholderAPI"), 1);*/
        /*init();
        if(sv || vnp || ess) {
            return super.register();
        }*/
        /*return init();
        //PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
    }*/

    @Override
    public String getIdentifier() {
        return "vanish";
    }

    @Override
    public String getAuthor() {
        return "cookieman768";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if(identifier.equals("count")) {
            return "" + vanishedPlayers(p);
        }
        if(identifier.equals("playercount")) {
            return "" + (Bukkit.getOnlinePlayers().size() - vanishedPlayers(p));
        }
        if(identifier.equals("vanished")) {
            return "" + isVanished(p);
        }
        return null;
    }

    private boolean isVanished(Player p) {
        if(ess)
            return essPlugin.getVanishedPlayers().contains(p.getName());
        if(sv) {
            return VanishAPI.isInvisible(p);
        }
        if(vnp) {
            return vanishManager.isVanished(p);
        }
        return false;
    }

    private int vanishedPlayers(Player p) {
        if(ess) {
            return essPlugin.getVanishedPlayers().size();
        }
        if(sv) {
            int i = 0;
            for(UUID e : VanishAPI.getInvisiblePlayers()) {
                if(VanishAPI.canSee(p, Bukkit.getPlayer(e))) {
                    i++;
                }
            }
            return i;
        }
        if(vnp) {
            return vanishManager.getVanishedPlayers().size();
        }
        return -1;
    }

    /*private boolean init() {
        if(Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
            sv = true;
            return super.register();
        }
        if(Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")) {
            vnp = true;
            vanishManager = ((VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket")).getManager();
            return super.register();
        }
        if(Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            ess = true;
            essPlugin = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            return super.register();
        }
        return false;
    }*/

    private boolean init() {
        if(pluginName.equals("SuperVanish") || pluginName.equals("PremiumVanish")) {
            sv = true;
            Bukkit.getLogger().info("Vanish Placeholders - Hooked into SuperVanish or PremiumVanish");
            return true;
        }
        if(pluginName.equals("VanishNoPacket")) {
            vnp = true;
            vanishManager = ((VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket")).getManager();
            Bukkit.getLogger().info("Vanish Placeholders - Hooked into VanishNoPacket");
            return true;
        }
        if(pluginName.equals("Essentials")) {
            ess = true;
            essPlugin = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            Bukkit.getLogger().info("Vanish Placeholders - Hooked into Essentials");
            return true;
        }
        Bukkit.getLogger().warning("Vanish Placeholders - Unable to Hook!");
        return false;
    }
}

