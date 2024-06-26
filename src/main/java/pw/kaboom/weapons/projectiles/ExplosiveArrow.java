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

public final class ExplosiveArrow implements WeaponProjectile {
    private static final float EXPLOSION_POWER = 4.9f;

    @Override
    public @NotNull EntityType entityType() {
        return EntityType.ARROW;
    }

    @Override
    public boolean hitEntity(@NotNull Projectile self, @NotNull Entity other) {
        self.getWorld().createExplosion(self, EXPLOSION_POWER, true);
        self.remove();
        return true;
    }

    @Override
    public boolean hitBlock(@NotNull Projectile self, @NotNull Block block,
                            @Nullable BlockFace blockFace) {
        self.getWorld().createExplosion(self, EXPLOSION_POWER, true);
        self.remove();
        return true;
    }

    @Override
    public void populate(@NotNull Entity self, @Nullable Player source) {
        if (!(self instanceof final Projectile projectile)) {
            return;
        }

        projectile.setShooter(source);
    }

    /* TODO
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTickStart(ServerTickStartEvent event) {
        for (Projectile projectile : explosiveProjectiles) {
            final World world = projectile.getWorld();
            final Location location = projectile.getLocation().add(
                            secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
                            secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET),
                            secureRandom.nextDouble(-PARTICLE_OFFSET, PARTICLE_OFFSET));

            world.spawnParticle(Particle.FLAME, location, 4);
        }
     }
     */
}
