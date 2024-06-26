package pw.kaboom.weapons.weapons;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapon.AbstractProjectileWeapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponGrenade extends AbstractProjectileWeapon {
    public WeaponGrenade(final ProjectileManager projectileManager,
                         final NamespacedKey projectileKey) {
        super(projectileManager, projectileKey);
    }

    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.EGG)
                .name("Grenade")
                .build();
    }

    @Override
    public boolean use(@NotNull WeaponUseEvent event) {
        if (!super.use(event)) {
            return false;
        }

        final Player player = event.source();
        final float volume = 1.0F;
        final float pitch = 1.0F;

        player.getWorld().playSound(
                player.getEyeLocation(),
                Sound.ENTITY_EGG_THROW,
                volume,
                pitch
        );

        return true;
    }
}
