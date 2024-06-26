package pw.kaboom.weapons.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapon.AbstractProjectileWeapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;

public final class WeaponExplosiveCrossbow extends AbstractProjectileWeapon {
    public WeaponExplosiveCrossbow(final ProjectileManager projectileManager,
                                   final NamespacedKey projectileKey) {
        super(projectileManager, projectileKey);
    }

    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.CROSSBOW)
                .name("Explosive Crossbow")
                .enchantment(Enchantment.MULTISHOT)
                .enchantment(Enchantment.QUICK_CHARGE, 5)
                .build();
    }
}
