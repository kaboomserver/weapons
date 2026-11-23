package pw.kaboom.weapons.modules.weapons;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import pw.kaboom.weapons.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class WeaponExplosiveCrossbow implements Weapon, Listener {
    private static final int PARTICLE_OFFSET = 2;
    private static final float EXPLOSION_POWER = 4.9f;

    private final List<Projectile> explosiveProjectiles = new ArrayList<>();

    @Override
    public String getKey() {
        return "explosivecrossbow";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.CROSSBOW);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Explosive Crossbow"));
            meta.addEnchant(Enchantment.MULTISHOT, 1, true);
            meta.addEnchant(Enchantment.QUICK_CHARGE, 5, true);
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    private boolean isThisWeapon(final ItemStack item) {
        if (item.getType() != Material.CROSSBOW || !item.hasItemMeta()) {
            return false;
        }

        final List<String> keys = item.getItemMeta().getPersistentDataContainer()
                .get(Main.getPdcKey(), PersistentDataType.LIST.strings());

        return keys != null && keys.contains(getKey());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onShootArrow(final ProjectileLaunchEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Projectile projectile = event.getEntity();
        final ProjectileSource source = projectile.getShooter();

        if (!(source instanceof final HumanEntity humanEntity)) {
            return;
        }

        final Player player = (Player) humanEntity;
        final PlayerInventory playerInventory = player.getInventory();
        final ItemStack mainHandItem = playerInventory.getItemInMainHand();
        final ItemStack offHandItem = playerInventory.getItemInOffHand();

        if (!isThisWeapon(mainHandItem) && !isThisWeapon(offHandItem)) {
            return;
        }

        projectile.customName(Component.text("WeaponExplosiveArrow"));
        explosiveProjectiles.add(projectile);
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();
        final Component customName = projectile.customName();

        if (!Component.text("WeaponExplosiveArrow").equals(customName)) {
            return;
        }

        final Entity hitEntity = event.getHitEntity();
        final Block hitBlock = event.getHitBlock();
        final Location explosionLocation;

        if (hitBlock != null) {
            explosionLocation = hitBlock.getLocation();
        } else {
            explosionLocation = Objects.requireNonNullElse(hitEntity, projectile).getLocation();
        }

        final World world = projectile.getWorld();

        world.createExplosion(explosionLocation, EXPLOSION_POWER, true);
        projectile.remove();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEntityDestroy(final EntityRemoveFromWorldEvent event) {
        final Entity entity = event.getEntity();

        if (!(entity instanceof final Projectile projectile)) {
            return;
        }

        explosiveProjectiles.remove(projectile);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTickStart(final ServerTickStartEvent event) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        for (final Projectile projectile : explosiveProjectiles) {
            final World world = projectile.getWorld();
            final Location location = projectile.getLocation()
                    .add(random.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
                            random.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
                            random.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET));

            world.spawnParticle(Particle.FLAME, location, 4);
        }
    }
}
