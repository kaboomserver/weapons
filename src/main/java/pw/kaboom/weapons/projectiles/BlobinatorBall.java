package pw.kaboom.weapons.projectiles;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.projectile.WeaponProjectile;
import pw.kaboom.weapons.util.BlockUtil;

public final class BlobinatorBall implements WeaponProjectile {
    @Override
    public @NotNull EntityType entityType() {
        return EntityType.SNOWBALL;
    }

    @Override
    public boolean hitEntity(@NotNull Projectile self, @NotNull Entity other) {
        return true;
    }

    @Override
    public boolean hitBlock(@NotNull Projectile self, @NotNull Block block,
                            @Nullable BlockFace blockFace) {
        final int radius = Main.RANDOM.nextInt(3, 6);
        final Material color = BlockUtil.randomWool();

        BlockUtil.sphereFill(block, color, radius);
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
