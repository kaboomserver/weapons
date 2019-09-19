package pw.kaboom.weapons;

import java.util.ArrayList;
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
	List<Material> colors = new ArrayList<>();

	public void onLoad() {
		/* Fill lists */
		Collections.addAll(
			faces,
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.WEST,
			BlockFace.EAST,
			BlockFace.UP,
			BlockFace.DOWN
		);

		Collections.addAll(
			colors,
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
	}

	public void onEnable() {
		/* Commands */
		this.getCommand("weapons").setExecutor(new CommandWeapons());

		/* Player Events */
		this.getServer().getPluginManager().registerEvents(new PlayerReceiveWeapon(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerUseWeapon(), this);

		/* Weapon Events */
		this.getServer().getPluginManager().registerEvents(new WeaponArcher(), this);
		this.getServer().getPluginManager().registerEvents(new WeaponArmageddon(), this);
		this.getServer().getPluginManager().registerEvents(new WeaponBlobinator(this), this);
		this.getServer().getPluginManager().registerEvents(new WeaponGrenade(), this);
		this.getServer().getPluginManager().registerEvents(new WeaponMachineGun(this), this);
	}
}
