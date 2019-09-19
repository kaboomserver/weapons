package pw.kaboom.weapons;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.Vector;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;

class WeaponBlobinator implements Listener {
	Main main;
	WeaponBlobinator(Main main) {
		this.main = main;
	}

	static void leftClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.MAGMA_CREAM &&
			"§rBlobinator".equals(name)) {
			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();
			final Vector velocity = eyeLocation.getDirection().multiply(8);

			final Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setCustomName("WeaponBlobinatorBall");
			snowball.setShooter(player);
			snowball.setVelocity(velocity);

			final World world = player.getWorld();
			final float volume = 1.0F;
			final float pitch = 0.8F;

			world.playSound(
				eyeLocation,
				Sound.ITEM_BOTTLE_EMPTY,
				volume,
				pitch
			);
			event.setCancelled(true);
		}
	}

	private void createBlobSplash(World world, int x, int y, int z, int radius, Block hitBlock, Material color) {
		final Location blockLocation = hitBlock.getLocation().add(x, y, z);

		if (blockLocation.distance(hitBlock.getLocation()) <= radius) {
			final Block block = world.getBlockAt(blockLocation);

			if (block.getType() != Material.AIR &&
				block.getType() != Material.CAVE_AIR) {
				for (BlockFace face : main.faces) {
					if (block.getRelative(face).getType() == Material.AIR ||
						block.getRelative(face).getType() == Material.CAVE_AIR) {
						block.setType(color);
					}
				}
			}
		}
	}

	@EventHandler
	void onProjectileCollide(ProjectileCollideEvent event) {
		if (event.getEntityType() == EntityType.SNOWBALL) {
			final Projectile projectile = event.getEntity();

			if ("WeaponBlobinatorBall".equals(projectile.getCustomName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.SNOWBALL) {
			final Block hitBlock = event.getHitBlock();
			final Projectile projectile = event.getEntity();

			if (hitBlock != null &&
				"WeaponBlobinatorBall".equals(projectile.getCustomName())) {
				final int radius = 4;
				final World world = projectile.getWorld();
				final Random random = new Random();
				final Material color = main.colors.get(random.nextInt(main.colors.size()));

				for (int x = -radius; x < radius; x++) {
					for (int y = -radius; y < radius; y++) {
						for (int z = -radius; z < radius; z++) {
							createBlobSplash(world, x, y, z, radius, hitBlock, color);
						}
					}
				}
			}
		}
	}
}
