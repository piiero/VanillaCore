package be.nickoos.vmcore;

import java.util.HashMap;
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

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener {
	static Main plugin;
	static ConfigManager cm;
	static FileConfiguration players;
	static FileConfiguration arret;
	PluginManager pm;
	static int res;
	
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
	    
		getServer().getConsoleSender().sendMessage("�a[�bVanillaCore�a] �eCharg� !");
	}
	public static Main getPlugin() {
	    return plugin;
	}
	public void onDisable(){
		getServer().getConsoleSender().sendMessage("�a[�bVanillaCore�a] �eD�charg� !");
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
							Bukkit.broadcastMessage("�9[�eVanillaCore�9] �4Red�marrage du serveur dans �f" + Main.res + " �4secondes !");
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
			sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission (" + ChatColor.DARK_RED + string + ChatColor.RED + ") pour ex�cuter cette commande."); else {
				sender.sendMessage("Tu n'as pas la permission (" + string + ") pour executer cette commande.");
		}
		return false;
	}
}