package pw.kaboom.weapons.weapon;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.weapon.event.WeaponShootEvent;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public interface Weapon {
    default boolean use(final @NotNull WeaponUseEvent event) {
        return false;
    }
    default @Nullable Entity shoot(final @NotNull WeaponShootEvent event) {
        return event.projectile();
    }

    @Contract("-> new")
    @NotNull ItemStack item();
}
