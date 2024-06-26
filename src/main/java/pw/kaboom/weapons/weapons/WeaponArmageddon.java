package pw.kaboom.weapons.weapons;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapon.AbstractProjectileWeapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponArmageddon extends AbstractProjectileWeapon {
    public WeaponArmageddon(final ProjectileManager projectileManager,
                            final NamespacedKey projectileKey) {
        super(projectileManager, projectileKey);
    }

    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.FIRE_CHARGE)
                .name("Armageddon")
                .build();
    }

    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) {
            return false;
        }

        final Player player = event.source();
        final World world = player.getWorld();

        final int min = -12;
        final int max = 12;
        final int iteratorSpacing = 4;

        for (int i = min; i <= max; i += iteratorSpacing) {
            final Location eyeLocation = player.getEyeLocation();

            final double x = i * Math.cos(Math.toRadians(eyeLocation.getYaw()));
            final double z = i * Math.sin(Math.toRadians(eyeLocation.getYaw()));

            this.spawnProjectile(eyeLocation.add(x, 0, z), player);
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

        return true;
    }
}
