package pw.kaboom.weapons.projectile;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.manager.Manager;

public final class ProjectileManager extends Manager<WeaponProjectile, Entity> {
    public ProjectileManager(final @NotNull NamespacedKey containerKey) {
        super(containerKey);
    }

    private void onAddEntity(final Entity entity) {
        final WeaponProjectile projectile = this.get(entity);
        if (projectile == null) {
            return;
        }

        projectile.spawn(entity);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEntitiesLoad(final EntitiesLoadEvent event) {
        for (final Entity entity: event.getEntities()) {
            this.onAddEntity(entity);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntitySpawn(final EntitySpawnEvent event) {
        this.onAddEntity(event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onProjectileHit(final ProjectileHitEvent event) {
        final Projectile entity = event.getEntity();
        final WeaponProjectile weapon = this.get(entity);
        if (weapon == null) {
            return;
        }

        if (event.getHitBlock() != null) {
            if (weapon.hitBlock(entity, event.getHitBlock(), event.getHitBlockFace())) {
                event.setCancelled(true);
            }
        } else if (event.getHitEntity() != null) {
            if (weapon.hitEntity(entity, event.getHitEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEntityRemove(final EntityRemoveFromWorldEvent event) {
        final Entity entity = event.getEntity();
        final WeaponProjectile weapon = this.get(entity);
        if (weapon == null) {
            return;
        }

        weapon.remove(entity);
    }
}
