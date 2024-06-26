package pw.kaboom.weapons.projectiles;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.projectile.WeaponProjectile;

public final class Grenade implements WeaponProjectile {
    private static final float EXPLOSION_POWER = 6f;

    @Override
    public @NotNull EntityType entityType() {
        return EntityType.EGG;
    }

    @Override
    public boolean hitEntity(@NotNull Projectile self, @NotNull Entity other) {
        self.getWorld().createExplosion(self, EXPLOSION_POWER, true);
        return true;
    }

    @Override
    public boolean hitBlock(@NotNull Projectile self, @NotNull Block block,
                            @Nullable BlockFace blockFace) {
        self.getWorld().createExplosion(self, EXPLOSION_POWER, true);
        return true;
    }

    @Override
    public void populate(@NotNull Entity self, @Nullable Player source) {
        if (!(self instanceof final Projectile projectile)) {
            return;
        }

        projectile.setShooter(source);
    }
}
