package pw.kaboom.weapons.projectile;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WeaponProjectile {
    default void spawn(final @NotNull Entity self) {
    }

    default boolean hitEntity(final @NotNull Projectile self, final @NotNull Entity other) {
        return false;
    }

    default boolean hitBlock(final @NotNull Projectile self,
                             final @NotNull Block block, final @Nullable BlockFace blockFace) {
        return false;
    }

    default void remove(final @NotNull Entity self) {
    }

    @NotNull EntityType entityType();
    default void populate(final @NotNull Entity self, final @Nullable Player source) {}
}
