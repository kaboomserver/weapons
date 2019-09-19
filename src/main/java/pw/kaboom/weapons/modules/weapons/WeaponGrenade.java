package pw.kaboom.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.util.Vector;

class WeaponGrenade implements Listener {
	static void rightClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.EGG &&
			"§rGrenade".equals(name)) {
			event.setCancelled(true);

			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();

			final Egg egg = player.launchProjectile(Egg.class);
			egg.setCustomName("WeaponGrenade");
			egg.setShooter(player);
			
			final World world = player.getWorld();
			final float volume = 1.0F;
			final float pitch = 1.0F;

			world.playSound(
				eyeLocation,
				Sound.ENTITY_EGG_THROW,
				volume,
				pitch
			);
		}
	}

	@EventHandler
	void onPlayerEggThrow(PlayerEggThrowEvent event) {
		if ("WeaponGrenade".equals(event.getEgg().getCustomName())) {
			event.setHatching(false);
		}
	}
	
	@EventHandler
	void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.EGG) {
			final Projectile projectile = event.getEntity();

			if ("WeaponGrenade".equals(projectile.getCustomName())) {
				final Location location = projectile.getLocation();
				final World world = location.getWorld();
				final float power = 6;

				world.createExplosion(location, power);
			}
		}
	}
}
