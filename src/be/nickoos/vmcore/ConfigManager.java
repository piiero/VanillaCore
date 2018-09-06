package be.nickoos.vmcore;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	private JavaPlugin plugin;
	private FileConfiguration customConfig;
	private File customConfigFile;
	public ConfigManager(JavaPlugin paramJavaPlugin){
		this.customConfig = null;
	    this.customConfigFile = null;
	    this.plugin = paramJavaPlugin;
	}
	   
	public void reloadConfig(String paramString) {
		if (this.customConfigFile == null) { this.customConfigFile = new File(this.plugin.getDataFolder(), paramString);
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);
		InputStreamReader localInputStreamReader;
		try{
			try{
				localInputStreamReader = new InputStreamReader(this.plugin.getResource(paramString), "UTF-8");
			}
			catch (NullPointerException localNullPointerException) {
				this.plugin.saveResource(paramString, true);
				return;
			}
		}
		catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			localInputStreamReader = null;
		}
	     
		if (localInputStreamReader != null){
			YamlConfiguration localYamlConfiguration = YamlConfiguration.loadConfiguration(localInputStreamReader);
			this.customConfig.setDefaults(localYamlConfiguration);
		}
	}
	   
	public FileConfiguration getConfig(String paramString){
		if (this.customConfig == null) reloadConfig(paramString);
	    return this.customConfig;
	}
	   
	public void saveConfig(String paramString){
		if ((this.customConfig == null) || (this.customConfigFile == null)) { return;
		}
		try{
			getConfig(paramString).save(this.customConfigFile);
	    }
		catch (IOException|NullPointerException localIOException) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, localIOException);
		}
	}
	   
	public void saveDefaultConfig(String paramString) {
		if (this.customConfigFile == null) { this.customConfigFile = new File(this.plugin.getDataFolder(), paramString);
	    }
	    if (!this.customConfigFile.exists()) { this.plugin.saveResource(paramString, false);
	    }
	    saveConfig(paramString);
	}
}

