package pw.kaboom.weapons;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TippedArrow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.util.Vector;

class WeaponMachineGun implements Listener {
	static Main main;
	WeaponMachineGun(Main main) {
		this.main = main;
	}

	static void rightClick(Material item, String name, PlayerInteractEvent event) {
		if (item == Material.GOLDEN_HORSE_ARMOR &&
			"§rMachine Gun".equals(name)) {
			final UUID playerUUID = event.getPlayer().getUniqueId();

			if (!main.machineGunActive.contains(playerUUID)) {
				main.machineGunActive.add(playerUUID);
				final PlayerInteractEvent eventMachine = event;

				new BukkitRunnable() {
					int i = 0;
					public void run() {
						i++;

						final Player player = eventMachine.getPlayer();
						final Location eyeLocation = player.getEyeLocation();
						final World world = player.getWorld();
						final Vector velocity = eyeLocation.getDirection().multiply(12);

						final TippedArrow arrow = player.launchProjectile(TippedArrow.class);

						final int duration = 90000;
						final int amplifier = 3;
						final boolean ambient = true;
						final boolean particles = false;

						final PotionEffect harm = new PotionEffect(
							PotionEffectType.HARM,
							duration,
							amplifier,
							ambient,
							particles
						);

						arrow.setCustomName("WeaponMachineGunBullet");
						arrow.addCustomEffect(harm, true);
						arrow.setShooter(player);
						arrow.setVelocity(velocity);

						final float volume = 1.0F;
						final float pitch = 63.0F;

						world.playSound(
							eyeLocation,
							Sound.ENTITY_GENERIC_EXPLODE,
							volume,
							pitch
						);

						if (i == 20) {
							main.machineGunActive.remove(playerUUID);
							cancel();
						}
					}
				}.runTaskTimer(main, 0L, 1L);
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.TIPPED_ARROW) {
			final Projectile projectile = event.getEntity();

			if ("WeaponMachineGunBullet".equals(projectile.getCustomName())) {
				projectile.remove();
			}
		}
	}
}
