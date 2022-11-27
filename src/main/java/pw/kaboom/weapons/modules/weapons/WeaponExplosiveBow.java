package pw.kaboom.weapons.modules.weapons;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

public class WeaponExplosiveBow implements Listener {

	private static final int PARTICLE_OFFSET = 2;
	private final List<Projectile> explosiveProjectiles = new ArrayList<>();
	private final SecureRandom secureRandom = new SecureRandom();

	@EventHandler
	public void onShootArrow(ProjectileLaunchEvent event) {
		final Projectile projectile = event.getEntity();
		final ProjectileSource source = projectile.getShooter();

		if (!(source instanceof final HumanEntity humanEntity)) {
			return;
		}

		final Player player = (Player) humanEntity;
		final PlayerInventory playerInventory = player.getInventory();
		final ItemStack offHandItem = playerInventory.getItemInOffHand();
		final ItemStack mainHandItem = playerInventory.getItemInMainHand();
		final Material offHandItemMaterial = offHandItem.getType();
		final Material mainHandItemMaterial = mainHandItem.getType();

		if (!mainHandItemMaterial.equals(Material.BOW) && !offHandItemMaterial.equals(
			Material.BOW)) {
			return;
		}

		final Component itemName = Component.text("Explosive Bow")
			.decoration(TextDecoration.ITALIC, false);
		final ItemStack bow = mainHandItemMaterial == Material.BOW ?
			mainHandItem : offHandItem;
		final ItemMeta bowMeta = bow.getItemMeta();
		final Component displayName = bowMeta.displayName();

		if (displayName == null) {
			return;
		}

		if (!(displayName.equals(itemName))) {
			return;
		}

		projectile.customName(Component.text("WeaponExplosiveBow"));
		explosiveProjectiles.add(projectile);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		final Projectile projectile = event.getEntity();
		final Component customName = projectile.customName();

		if (customName == null) {
			return;
		}

		if (!customName.equals(Component.text("WeaponExplosiveBow"))) {
			return;
		}

		final Entity hitEntity = event.getHitEntity();
		final Block hitBlock = event.getHitBlock();

		final Location explosionLocation =
			(hitBlock != null || hitEntity != null) ?
				(hitEntity != null
					? hitEntity.getLocation() : hitBlock.getLocation())
				: projectile.getLocation();

		final World world = projectile.getWorld();

		world.createExplosion(explosionLocation, 4.8f, true);
		projectile.remove();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDestroy(EntityRemoveFromWorldEvent event) {
		final Entity entity = event.getEntity();

		if (!(entity instanceof final Projectile projectile)) {
			return;
		}

		explosiveProjectiles.remove(projectile);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTickEnd(ServerTickEndEvent event) {
		for (Projectile projectile : explosiveProjectiles) {
			final World world = projectile.getWorld();
			final Location location = projectile.getLocation()
				.add(secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
					secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
					secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET));

			world.spawnParticle(Particle.FLAME, location, 4);
		}
	}
}
