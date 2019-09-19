package pw.kaboom.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.Vector;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;

class WeaponArmageddon implements Listener {
	static void leftClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.FIRE_CHARGE &&
			"§rArmageddon".equals(name)) {
			final Player player = event.getPlayer();
			final World world = player.getWorld();

			for (int i = -12; i <= 12; i += 4) {
				final Location eyeLocation = player.getEyeLocation();

				final double x = i * Math.cos(Math.toRadians(eyeLocation.getYaw()));
				final double z = i * Math.sin(Math.toRadians(eyeLocation.getYaw()));

				final Vector velocity = eyeLocation.getDirection().multiply(12);

				final Fireball fireball = (Fireball) world.spawnEntity(
					eyeLocation.add(x, 0, z),
					EntityType.FIREBALL
				);

				fireball.setBounce(false);
				fireball.setCustomName("WeaponArmegaddonCharge");
				fireball.setDirection(velocity);
				fireball.setShooter(player);
				fireball.setYield(5);
			}

			final Location eyeLocation = player.getEyeLocation();
			final float volume = 0.9F;
			final float pitch = 1.5F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_GHAST_SHOOT,
				volume,
				pitch
			);
			event.setCancelled(true);
		}
	}

	/* Make shooter invulnerable to weapon projectiles, and prevent charges from colliding with each other */
	@EventHandler
	void onProjectileCollide(ProjectileCollideEvent event) {
		if (event.getEntityType() == EntityType.FIREBALL) {
			final Projectile projectile = event.getEntity();

			if ("WeaponArmegaddonCharge".equals(projectile.getCustomName())) {
				final Entity collidedWith = event.getCollidedWith();

				if ((collidedWith.getType() == EntityType.PLAYER &&
					projectile.getShooter() instanceof Player &&
					((Player) projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) ||
					collidedWith.getType() == EntityType.FIREBALL) {
					event.setCancelled(true);
				}
			}
		}
	}
}
