package pw.kaboom.weapons.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.projectile.WeaponProjectile;

public final class ArmageddonCharge implements WeaponProjectile {
    @Override
    public @NotNull EntityType entityType() {
        return EntityType.FIREBALL;
    }

    @Override
    public boolean hitEntity(@NotNull Projectile self, @NotNull Entity other) {
        if (other.getType() != EntityType.PLAYER) {
            return false;
        }

        if (other.getType() == EntityType.FIREBALL) {
            return true;
        }

        return self.getShooter() instanceof final Player shooter
                && shooter.getUniqueId() == other.getUniqueId();
    }

    @Override
    public void populate(@NotNull Entity self, @Nullable Player source) {
        if (!(self instanceof final Fireball fireball)) {
            return;
        }

        fireball.setShooter(source);
        fireball.setYield(5);
    }
}
