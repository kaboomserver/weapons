package pw.kaboom.weapons.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapon.AbstractProjectileWeapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponArcher extends AbstractProjectileWeapon {
    public WeaponArcher(final ProjectileManager projectileManager,
                        final NamespacedKey projectileKey) {
        super(projectileManager, projectileKey);
    }

    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.SPECTRAL_ARROW)
                .name("Archer")
                .build();
    }

    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) {
            return false;
        }

        final Player player = event.source();
        final int maxArrowCount = 20;
        for (int i = 0; i <= maxArrowCount; i++) {
            this.spawnProjectile(player.getLocation(), player);
        }

        final Location eyeLocation = player.getEyeLocation();
        final float volume = 1.0F;
        final float pitch = 1.5F;

        player.getWorld().playSound(
                eyeLocation,
                Sound.BLOCK_ANVIL_DESTROY,
                volume,
                pitch
        );

        return true;
    }
}
