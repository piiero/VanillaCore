package be.nickoos.vmcore;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AnnounceTask extends BukkitRunnable {

	private Main plugin;

	private int count = 0;
	private Random random = null;

	public AnnounceTask(Main main) {
		plugin = main;
		count = 0;
		random = new Random(System.currentTimeMillis());
	}

	@Override
	public void run() {
		String message = plugin.prefix;
		if (plugin.random) {
			count = random.nextInt(plugin.messages.size());
			message += plugin.messages.get(count);
		} else {
			message += plugin.messages.get(count);
			count++;
			if (count == plugin.messages.size()) {
				count = 0;
			}
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("announce.recieve")) {
				p.sendMessage(message);
			}
		}
	}

}


