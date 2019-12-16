package pw.kaboom.weapons;

import java.util.Random;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.Player;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

class WeaponLaser {
	static void rightClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.BLAZE_POWDER &&
			"§rLaser".equals(name)) {
			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();
			final Vector direction = eyeLocation.getDirection();
			final int distance = 100;
			final Location particleLocation = player.getEyeLocation();
			final World world = player.getWorld();

			for (int i = 0; i <= distance; i++) {
				particleLocation.add(direction);

				final int count = 1;
				final double offsetX = 0;
				final double offsetY = 0;
				final double offsetZ = 0;

				final Random random = new Random();
				final int size = 1;
				final Particle.DustOptions dustOptions = new Particle.DustOptions(
					random.nextBoolean() ? Color.TEAL : Color.RED,
					size
				);

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

			final Vector start = eyeLocation.toVector();
			final int yOffset = 0;

			final BlockIterator blockIterator = new BlockIterator(
				world,
				start,
				direction,
				yOffset,
				distance
			);
	
			while (blockIterator.hasNext()) {
				blockIterator.next().breakNaturally();
			}

			float volume = 0.8F;
			float pitch = 63.0F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR,
				volume,
				pitch
			);

			volume = 1.0F;
			pitch = 20.0F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_FIREWORK_ROCKET_BLAST,
				volume,
				pitch
			);
			event.setCancelled(true);
		}
	}
}
