package pw.kaboom.weapons;

import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.Player;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.Vector;

class WeaponLaser {
	static void rightClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.BLAZE_POWDER &&
			"§rLaser".equals(name)) {
			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();
			final Vector direction = eyeLocation.getDirection();
			final Location lookLocation = player.getTargetBlock(
				(Set<Material>) null,
				100).getLocation();
			double distance = eyeLocation.distance(lookLocation);
			final Location particleLocation = player.getEyeLocation();
			final World world = player.getWorld();

			if (lookLocation == null) {
				distance = 50;
			}

			for (double i = 0; i <= distance; i++) {
				particleLocation.add(direction);

				final int count = 1;
				final double offsetX = 0;
				final double offsetY = 0;
				final double offsetZ = 0;
				final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);

				world.spawnParticle(
					Particle.REDSTONE,
					particleLocation,
					count,
					offsetX,
					offsetY,
					offsetZ,
					dustOptions
				);
			}

			world.getBlockAt(lookLocation).breakNaturally();

			final float volume1 = 0.8F;
			final float pitch1 = 63.0F;
			final float volume2 = 1.0F;
			final float pitch2 = 20.0F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR,
				volume1,
				pitch1
			);
			world.playSound(
				eyeLocation,
				Sound.ENTITY_FIREWORK_ROCKET_BLAST,
				volume2,
				pitch2
			);
			event.setCancelled(true);
		}
	}
}
