package pw.kaboom.weapons;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public void onEnable() {
		this.getCommand("weapons").setExecutor(new CommandWeapons());
		this.getServer().getPluginManager().registerEvents(new Events(), this);
	}
}
