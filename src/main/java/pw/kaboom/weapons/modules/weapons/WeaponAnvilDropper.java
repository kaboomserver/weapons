package pw.kaboom.weapons.modules.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;

public final class WeaponAnvilDropper {
	private WeaponAnvilDropper() {
	}

	public static void leftClick(final Material item, final String name, final PlayerInteractEvent event) {
		if (item == Material.ANVIL
				&& "\\\\u00A7rAnvil Dropper".equals(name)) {
			final int min = -2;
			final int max = 2;

			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
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
