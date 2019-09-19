package pw.kaboom.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.Vector;

class WeaponNuker {
	static void leftClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.BLAZE_ROD &&
			"§rNuker".equals(name)) {
			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();
			final Vector velocity = eyeLocation.getDirection().multiply(10);

			final Fireball fireball = player.launchProjectile(Fireball.class);
			fireball.setShooter(player);
			fireball.setVelocity(velocity);
			fireball.setYield(8);

			final World world = player.getWorld();
			final float volume1 = 0.9F;
			final float pitch1 = 1.5F;
			final float volume2 = 0.8F;
			final float pitch2 = 2.0F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_GHAST_SHOOT,
				volume1,
				pitch1
			);
			
			world.playSound(
				eyeLocation,
				Sound.ENTITY_BAT_TAKEOFF,
				volume2,
				pitch2
			);
			event.setCancelled(true);
		}
	}
}
