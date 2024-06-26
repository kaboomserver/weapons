package pw.kaboom.weapons.weapons;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapon.AbstractProjectileWeapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponBlobinator extends AbstractProjectileWeapon {
    public WeaponBlobinator(final ProjectileManager projectileManager,
                            final NamespacedKey projectileKey) {
        super(projectileManager, projectileKey);
    }

    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.MAGMA_CREAM)
                .name("Blobinator")
                .build();
    }

    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!super.use(event)) {
            return false;
        }

        final Player player = event.source();
        final float volume = 1.0F;
        final float pitch = 0.8F;

        player.getWorld().playSound(
                player.getEyeLocation(),
                Sound.ITEM_BOTTLE_EMPTY,
                volume,
                pitch
        );

        return true;
    }
}
