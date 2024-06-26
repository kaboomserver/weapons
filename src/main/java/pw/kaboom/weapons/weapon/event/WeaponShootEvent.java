package pw.kaboom.weapons.weapon.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WeaponShootEvent(@NotNull LivingEntity source, @NotNull Entity projectile,
                               @NotNull ItemStack item, float force) {
    public static @Nullable WeaponShootEvent fromEntityShootBowEvent(
            final @NotNull EntityShootBowEvent event) {
        final ItemStack item = event.getBow();
        if (item == null) {
            return null;
        }

        return new WeaponShootEvent(event.getEntity(), event.getProjectile(), item,
                event.getForce());
    }
}
