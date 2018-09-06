package be.nickoos.vmcore;

/*    */ import java.util.UUID;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerBedEnterEvent;
/*    */ 
/*    */ public class HomeBed implements CommandExecutor, Listener
/*    */ {
/*    */   static ConfigManager cM;
/*    */   static FileConfiguration bed;
/*    */   
/*    */   static boolean load()
/*    */   {
/* 22 */     cM = new ConfigManager(Main.getPlugin());
/*    */     
/* 24 */     cM.saveDefaultConfig("beds.yml");
/*    */     
/* 26 */     bed = cM.getConfig("beds.yml");
/*    */     
/* 28 */     bed.options().copyDefaults(true);
/* 29 */     cM.saveConfig("beds.yml");
/*    */     
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   static Location getBed(Player paramPlayer)
/*    */   {
/* 36 */     return getBed(paramPlayer.getUniqueId());
/*    */   }
/*    */   
/*    */   static Location getBed(UUID paramUUID)
/*    */   {
/* 41 */     String str = paramUUID + ".";
/*    */     
/* 43 */     if (bed.contains(str + "world")) {
/* 44 */       return new Location(org.bukkit.Bukkit.getWorld(bed.getString(str + "world")), bed.getDouble(str + "x"), bed.getDouble(str + "y"), bed.getDouble(str + "z"));
/*    */     }
/*    */     
/* 47 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   @EventHandler
/*    */   public void PlayerEnterBed(PlayerBedEnterEvent paramPlayerBedEnterEvent)
/*    */   {
/* 54 */     Player localPlayer = paramPlayerBedEnterEvent.getPlayer();
/* 55 */     Location localLocation = localPlayer.getLocation();
/*    */     
/* 57 */     String str = localPlayer.getUniqueId() + ".";
/*    */     
/* 59 */     bed.set(str + "world", localLocation.getWorld().getName());
/* 60 */     bed.set(str + "x", Double.valueOf(localLocation.getX()));
/* 61 */     bed.set(str + "y", Double.valueOf(localLocation.getY()));
/* 62 */     bed.set(str + "z", Double.valueOf(localLocation.getZ()));
/* 63 */     cM.saveConfig("beds.yml");
/* 64 */     localPlayer.sendMessage(ChatColor.AQUA + "Votre home à été définie.");
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString)
/*    */   {
/* 70 */     if ((paramCommandSender instanceof Player)) {
/* 71 */       Player localPlayer = (Player)paramCommandSender;
/* 72 */       Location localLocation = getBed(localPlayer);
/*    */       
/* 74 */       if (paramString.equalsIgnoreCase("home")) {
/* 75 */         if (localLocation != null) {
/* 76 */           localPlayer.sendMessage(ChatColor.AQUA + "Coordonnées de votre home : X " + ChatColor.DARK_AQUA + localLocation.getBlockX() + ChatColor.AQUA + ", Z " + ChatColor.DARK_AQUA + localLocation.getBlockZ());
/*    */ 
/*    */         }
/*    */         else
/*    */         {
/* 81 */           localPlayer.sendMessage(ChatColor.AQUA + "Vous n'avez pas de home.");
/*    */         }
/* 83 */         return true; }
/* 84 */       if (paramString.equalsIgnoreCase("sethome")) {
/* 85 */         localPlayer.sendMessage(ChatColor.AQUA + "Pour définir votre home, il vous suffit de dormir dans un lit.");
/* 86 */         return true;
/*    */       }
/* 88 */       localPlayer.sendMessage(ChatColor.RED + "Commande inconnue.");
/*    */     }
/*    */     else {
/* 91 */       paramCommandSender.sendMessage("Seul un joueur peut faire cette commande.");
/*    */     }
/* 93 */     return false;
/*    */   }
/*    */ }