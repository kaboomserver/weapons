package pw.kaboom.weapons;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import org.bukkit.block.BlockFace;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	HashSet<BlockFace> faces = new HashSet<>();
	HashSet<UUID> machineGunActive = new HashSet<>();

	List<Material> colors = Arrays.asList(
		Material.BLACK_WOOL,
		Material.BLUE_WOOL,
		Material.BROWN_WOOL,
		Material.CYAN_WOOL,
		Material.GRAY_WOOL,
		Material.GREEN_WOOL,
		Material.LIGHT_BLUE_WOOL,
		Material.LIGHT_GRAY_WOOL,
		Material.LIME_WOOL,
		Material.MAGENTA_WOOL,
		Material.ORANGE_WOOL,
		Material.PINK_WOOL,
		Material.PURPLE_WOOL,
		Material.RED_WOOL,
		Material.WHITE_WOOL,
		Material.YELLOW_WOOL
	);

	public void onEnable() {
		Collections.addAll(
			faces,
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.WEST,
			BlockFace.EAST,
			BlockFace.UP,
			BlockFace.DOWN
		);

		this.getCommand("weapons").setExecutor(new CommandWeapons());
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
	}
}
