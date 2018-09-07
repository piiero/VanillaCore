package be.nickoos.vmcore;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class HomeBed implements CommandExecutor, Listener {
	static ConfigManager cM;
	static FileConfiguration bed;
   
	static boolean load() {
		cM = new ConfigManager(Main.getPlugin());
     
		cM.saveDefaultConfig("beds.yml");
     
		bed = cM.getConfig("beds.yml");
     
		bed.options().copyDefaults(true);
		cM.saveConfig("beds.yml");
     
		return true;
	}
	
	static Location getBed(Player paramPlayer) {
		return getBed(paramPlayer.getUniqueId());
	}
   
	static Location getBed(UUID paramUUID) {
		String str = paramUUID + ".";
    
		if (bed.contains(str + "world")) {
			return new Location(org.bukkit.Bukkit.getWorld(bed.getString(str + "world")), bed.getDouble(str + "x"), bed.getDouble(str + "y"), bed.getDouble(str + "z"));
		}
     
		return null;
	}
   
	@EventHandler
	public void PlayerEnterBed(PlayerBedEnterEvent paramPlayerBedEnterEvent) {
		Player localPlayer = paramPlayerBedEnterEvent.getPlayer();
		Location localLocation = localPlayer.getLocation();
     
		String str = localPlayer.getUniqueId() + ".";
     
		bed.set(str + "world", localLocation.getWorld().getName());
		bed.set(str + "x", Double.valueOf(localLocation.getX()));
		bed.set(str + "y", Double.valueOf(localLocation.getY()));
		bed.set(str + "z", Double.valueOf(localLocation.getZ()));
		cM.saveConfig("beds.yml");
		localPlayer.sendMessage(ChatColor.AQUA + "Votre home à été définie.");
	}
   
 
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if ((paramCommandSender instanceof Player)) {
			Player localPlayer = (Player)paramCommandSender;
			Location localLocation = getBed(localPlayer);
			
			if (paramString.equalsIgnoreCase("home")) {
				if (localLocation != null) {
					localPlayer.sendMessage(ChatColor.AQUA + "Coordonnées de votre home : X " + ChatColor.DARK_AQUA + localLocation.getBlockX() + ChatColor.AQUA + ", Z " + ChatColor.DARK_AQUA + localLocation.getBlockZ());
					
				}
				else {
					localPlayer.sendMessage(ChatColor.AQUA + "Vous n'avez pas de home.");
				}
				return true; }
			if (paramString.equalsIgnoreCase("sethome")) {
				localPlayer.sendMessage(ChatColor.AQUA + "Pour définir votre home, il vous suffit de dormir dans un lit.");
				return true;
			}
			localPlayer.sendMessage(ChatColor.RED + "Commande inconnue.");
		}
		else {
			paramCommandSender.sendMessage("Seul un joueur peut faire cette commande.");
		}
		return false;
	}
}