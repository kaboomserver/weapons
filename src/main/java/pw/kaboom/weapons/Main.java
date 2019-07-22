package pw.kaboom.weapons;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	HashSet<UUID> machineGunActive = new HashSet<UUID>();

	public void onEnable() {
		this.getCommand("weapons").setExecutor(new CommandWeapons());
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
	}
}
