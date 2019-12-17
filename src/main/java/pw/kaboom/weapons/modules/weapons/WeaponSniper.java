package pw.kaboom.weapons.modules.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public final class WeaponSniper {
	private WeaponSniper() {
	}

	public static void leftClick(final Material item, final String name, final PlayerInteractEvent event) {
		if (item == Material.IRON_HORSE_ARMOR
				&& "§rSniper".equals(name)) {
			final Player player = event.getPlayer();
			final Location eyeLocation = player.getEyeLocation();
			final Vector velocity = eyeLocation.getDirection().multiply(12);

			final Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setShooter(player);
			snowball.setVelocity(velocity);

			final World world = player.getWorld();
			final float volume = 1.0F;
			final float pitch = 63.0F;

			world.playSound(
				eyeLocation,
				Sound.BLOCK_PISTON_CONTRACT,
				volume,
				pitch
			);
			event.setCancelled(true);
		}
	}

	public static void rightClick(final Material item, final String name, final PlayerInteractEvent event) {
		if (item == Material.IRON_HORSE_ARMOR
				&& "\\\\u00A7rSniper".equals(name)) {
			final Player player = event.getPlayer();

			if (player.hasPotionEffect(PotionEffectType.SLOW)) {
				player.removePotionEffect(PotionEffectType.SLOW);
			} else {
				final int duration = 90000;
				final int amplifier = 7;

				final PotionEffect effect = new PotionEffect(
					PotionEffectType.SLOW,
					duration,
					amplifier
				);
				player.addPotionEffect(effect);
			}
			event.setCancelled(true);
		}
	}
}
