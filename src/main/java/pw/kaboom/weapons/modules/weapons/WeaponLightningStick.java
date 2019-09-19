package pw.kaboom.weapons;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.event.player.PlayerInteractEvent;

class WeaponLightningStick {
	static void leftClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.STICK &&
			"§rLightning Stick".equals(name)) {
			final Location lookLocation = event.getPlayer().getTargetBlock((Set<Material>) null, 100).getLocation();
			final World world = event.getPlayer().getWorld();

			world.strikeLightning(lookLocation);
			event.setCancelled(true);
		}
	}
}
