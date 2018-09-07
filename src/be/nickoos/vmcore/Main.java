package be.nickoos.vmcore;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import be.nickoos.vmcore.AnnounceTask;


@SuppressWarnings("unused")

public class Main extends JavaPlugin implements Listener {
	static Main plugin;
	static ConfigManager cm;
	static FileConfiguration players;
	static FileConfiguration arret;
	PluginManager pm;
	static int res;
	
	private static final int TICKS_PER_SEC = 20;
	private static final int SECS_PER_MIN = 60;

	public BukkitTask task = null;

	public List<String> messages = null;

	public boolean random = false;
	public int delay = 0;
	public String prefix = null;
	
	@Override
	public void onEnable(){
		plugin = this;
		this.pm = Bukkit.getPluginManager();
		cm = new ConfigManager(this);
		ConfigManager localConfigManager = new ConfigManager(this);
		
		cm.saveDefaultConfig("players.yml");
		players = cm.getConfig("players.yml");
		
		getCommand("spawn").setExecutor(new Spawn());
		getCommand("setspawn").setExecutor(new Spawn());
		
		
	    this.pm.registerEvents(new Spawn(), plugin);
	    this.pm.registerEvents(new HomeBed(), plugin);
	    
	    Spawn.loadConfig();
	    HomeBed.load();
	    
	    saveDefaultConfig();
	    
		random = getConfig().getBoolean("random-order");
		delay = getConfig().getInt("message-delay");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message-prefix"));
		messages = getConfig().getStringList("messages");
		
		if (messages.isEmpty()) {
			getLogger().info("There are no valid messages defined in config.yml! Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {

			for (int i = 0; i < messages.size(); i++) {
				messages.set(i, ChatColor.translateAlternateColorCodes('&', messages.get(i)));
			}
		}

		task = new AnnounceTask(this).runTaskTimer(this, 0, delay * TICKS_PER_SEC * SECS_PER_MIN);

			    
	    getServer().getConsoleSender().sendMessage("§a[§bVanillaCore§a] §eChargé !");
	}
	public static Main getPlugin() {
	    return plugin;
	}
	public void onDisable(){
		if (task != null) {
			task.cancel();
		}
		getServer().getConsoleSender().sendMessage("§a[§bVanillaCore§a] §eDéchargé !");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		// TODO Auto-generated method stub
		boolean bool = sender instanceof Player;
		
		Object localObject2;
		if (msg.equalsIgnoreCase("reboot")) {
			if (hasPerm(sender, "vmcore.reboot")) {
				int i = 60;
				res = i;
	       
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				
					public void run() {
						if ((Main.res == 60) || (Main.res == 45) || (Main.res == 30) || (Main.res <= 15)) {
							Bukkit.broadcastMessage("§9[§eVanillaCore§9] §4Redémarrage du serveur dans §f" + Main.res + " §4secondes !");
							for (Player localPlayer : Bukkit.getOnlinePlayers()) {
								for (int k = Main.res * 2; k > 0; k--) {
									localPlayer.playSound(localPlayer.getLocation(), org.bukkit.Sound.ENTITY_COW_AMBIENT, 100.0F, new Random(2L).nextFloat());
								}
							}
						}
						if (Main.res <= 0) {
							Bukkit.shutdown();
				        }
				        
						Main.res -= 1;}}, 0L, 20L);
				       
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run() {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { public void run() {} }, 5L); } }, i * 20L - 5L);
						return true; 
					}
				return true;
			}
			return false;
		}
	public static boolean hasPerm(CommandSender sender, String string) {
		// TODO Auto-generated method stub
		if (sender.hasPermission(string)) { return true;
		}
		if ((sender instanceof Player))
			sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission (" + ChatColor.DARK_RED + string + ChatColor.RED + ") pour exécuter cette commande."); else {
				sender.sendMessage("Tu n'as pas la permission (" + string + ") pour executer cette commande.");
		}
		return false;
	}
}