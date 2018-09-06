package be.nickoos.vmcore;

import org.bukkit.event.Listener;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
 
public class Spawn implements CommandExecutor, org.bukkit.event.Listener {
	static ConfigManager cm;
	static FileConfiguration config;
	static HashMap<World, Location> Spawn = new HashMap();
   
	public static boolean loadConfig() {
		cm = new ConfigManager(Main.getPlugin());
    
		cm.saveDefaultConfig("spawns.yml");
     
		config = cm.getConfig("spawns.yml");
     
		for (World localWorld : org.bukkit.Bukkit.getWorlds()){
			String str = localWorld.getName() + ".";
    
			Location localLocation;
			if (config.contains(str + "x")) {
				localLocation = new Location(localWorld, config.getInt(str + "x"), config.getInt(str + "y"), config.getInt(str + "z"), (float)config.getDouble(str + "yaw"), (float)config.getDouble(str + "pitch"));
			} else {
				localLocation = localWorld.getSpawnLocation();
			}
			localWorld.setSpawnLocation(localLocation.getBlockX(), localLocation.getBlockY(), localLocation.getBlockZ());
			Spawn.put(localWorld, localLocation);
		}
     
		return true;
	}
   
 
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if ((paramCommandSender instanceof Player)) {
			Player localPlayer = (Player)paramCommandSender;
    
			if (paramString.equalsIgnoreCase("spawn")) {
				if (!paramCommandSender.hasPermission("vmcore.spawn")) {
					localPlayer.sendMessage(ChatColor.AQUA + "Coordonnée du spawn : X " + ChatColor.DARK_AQUA + localPlayer.getWorld().getSpawnLocation().getBlockX() + ChatColor.AQUA + ", Z " + ChatColor.DARK_AQUA + localPlayer.getWorld().getSpawnLocation().getBlockZ());
 
					return true;
				}
				for (World localWorld : org.bukkit.Bukkit.getWorlds()){
					Location localLocation;
					String str = localWorld.getName() + ".";
					localLocation = localWorld.getSpawnLocation();
					Location spawn = new Location(localPlayer.getWorld(), localPlayer.getWorld().getSpawnLocation().getBlockX(), localPlayer.getWorld().getSpawnLocation().getBlockY(), localPlayer.getWorld().getSpawnLocation().getBlockZ(), (float)config.getDouble(str + "yaw"), (float)config.getDouble(str + "pitch"));	
					localPlayer.teleport(spawn);
				}
									
				localPlayer.getWorld().playSound((Location)Spawn.get(localPlayer.getWorld()), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				localPlayer.sendMessage(ChatColor.GREEN + "Tu viens de te tp au spawn du monde.");
				
							
				
			} else if (paramString.equalsIgnoreCase("setspawn")) {
				if (!Main.hasPerm(localPlayer, "vmcore.setspawn")) { return true; }
				String str = localPlayer.getWorld().getName() + ".";
				Location localLocation = localPlayer.getLocation();
  
				config.set(str + "x", Double.valueOf(localLocation.getX()));
				config.set(str + "y", Double.valueOf(localLocation.getY()));
				config.set(str + "z", Double.valueOf(localLocation.getZ()));
				config.set(str + "yaw", Float.valueOf(localLocation.getYaw()));
				config.set(str + "pitch", Float.valueOf(localLocation.getPitch()));
				cm.saveConfig("spawns.yml");
  
				Spawn.put(localPlayer.getWorld(), localLocation);
				localPlayer.getWorld().setSpawnLocation(localLocation.getBlockX(), localLocation.getBlockY(), localLocation.getBlockZ());
				localPlayer.sendMessage(ChatColor.GREEN + "Tu viens de définir le spawn du monde " + ChatColor.DARK_GREEN + localPlayer.getWorld().getName());
			} else { localPlayer.sendMessage(ChatColor.RED + "Commande inconnue.");
			}
			return true; }
		paramCommandSender.sendMessage("Seul un joueur peut faire cette commande.");
     
		return false;
	}
   
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent paramPlayerRespawnEvent) {
		if (HomeBed.getBed(paramPlayerRespawnEvent.getPlayer()) == null) paramPlayerRespawnEvent.setRespawnLocation(paramPlayerRespawnEvent.getPlayer().getWorld().getSpawnLocation()); else {
			paramPlayerRespawnEvent.setRespawnLocation(HomeBed.getBed(paramPlayerRespawnEvent.getPlayer()));
		}
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent paramPlayerJoinEvent) {
		if (!paramPlayerJoinEvent.getPlayer().hasPlayedBefore()) {
			paramPlayerJoinEvent.getPlayer().teleport((Location)Spawn.get(paramPlayerJoinEvent.getPlayer().getWorld()));
		}
	}
}
