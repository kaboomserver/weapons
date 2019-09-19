package pw.kaboom.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.event.player.PlayerInteractEvent;

class WeaponAnvilDropper {
	static void leftClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.ANVIL &&
			"§rAnvil Dropper".equals(name)) {
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					final Location blockLocation = event.getPlayer().getLocation().subtract(x, 0, z);
					final World world = event.getPlayer().getWorld();

					world.spawnFallingBlock(
						blockLocation,
						Material.ANVIL.createBlockData()
					);
				}
			}
			event.setCancelled(true);
		}
	}
}