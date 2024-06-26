package pw.kaboom.weapons.weapon;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.projectile.WeaponProjectile;
import pw.kaboom.weapons.weapon.event.WeaponShootEvent;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public abstract class AbstractProjectileWeapon implements Weapon {
    protected final ProjectileManager projectileManager;
    protected final NamespacedKey projectileKey;

    protected AbstractProjectileWeapon(final ProjectileManager projectileManager,
                                       final NamespacedKey projectileKey) {
        this.projectileManager = projectileManager;
        this.projectileKey = projectileKey;
    }

    @Override
    public boolean use(@NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) {
            return false;
        }

        this.launchProjectile(event.source());
        return true;
    }

    @Override
    public @Nullable Entity shoot(@NotNull WeaponShootEvent event) {
        final WeaponProjectile projectile = this.projectile();
        if (projectile == null) {
            return null;
        }

        Player player = null;
        if (event.source() instanceof Player) {
            player = (Player) event.source();
        }

        final Entity projectileEntity = event.projectile();
        projectile.populate(projectileEntity, player);
        this.projectileManager.tag(projectileEntity, this.projectileKey);

        return projectileEntity;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected @Nullable Entity spawnProjectile(final @NotNull Location location,
                                               final @Nullable Player source) {
        final WeaponProjectile projectile = this.projectile();
        if (projectile == null) {
            return null;
        }

        final Class<? extends Entity> entityClass = projectile.entityType().getEntityClass();
        if (entityClass == null) {
            return null;
        }

        final World world = location.getWorld();
        final Entity projectileEntity = world.createEntity(location, entityClass);
        projectile.populate(projectileEntity, source);
        this.projectileManager.tag(projectileEntity, this.projectileKey);

        return world.addEntity(projectileEntity);
    }

    @SuppressWarnings({"UnusedReturnValue"})
    protected @Nullable Projectile launchProjectile(final @NotNull Player source) {
        final WeaponProjectile projectile = this.projectile();
        if (projectile == null) {
            return null;
        }

        final Class<? extends Entity> entityClass = projectile.entityType().getEntityClass();
        if (entityClass == null) {
            return null;
        }

        final Class<? extends Projectile> projectileClass =
                entityClass.asSubclass(Projectile.class);

        return source.launchProjectile(projectileClass, null, e -> {
            projectile.populate(e, source);
            this.projectileManager.tag(e, this.projectileKey);
        });
    }

    protected @Nullable WeaponProjectile projectile() {
        return this.projectileManager.get(this.projectileKey);
    }
}
