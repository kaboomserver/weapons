package pw.kaboom.weapons.projectiles;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.projectile.WeaponProjectile;

public final class ArcherArrow implements WeaponProjectile {
    @Override
    public @NotNull EntityType entityType() {
        return EntityType.SPECTRAL_ARROW;
    }

    @Override
    public boolean hitEntity(@NotNull Projectile self, @NotNull Entity other) {
        if (other.getType() != EntityType.PLAYER) {
            return false;
        }

        if (!(self.getShooter() instanceof final Player shooter)) {
            return false;
        }

        return other.getUniqueId() == shooter.getUniqueId();
    }

    @Override
    public void populate(@NotNull Entity self, @Nullable Player source) {
        final Vector randomDirection = new Vector(
                Main.RANDOM.nextDouble(-15, 15),
                Main.RANDOM.nextDouble(-15, 15),
                Main.RANDOM.nextDouble(-15, 15)
        ).normalize().multiply(8);
        self.setVelocity(randomDirection);

        if (!(self instanceof final Projectile projectile)) {
            return;
        }

        projectile.setShooter(source);
    }
}
