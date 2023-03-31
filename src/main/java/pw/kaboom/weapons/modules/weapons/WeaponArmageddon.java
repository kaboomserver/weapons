package pw.kaboom.weapons.modules.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public final class WeaponArmageddon implements Listener {
    public static void leftClick(final Material item, final Component name,
                                 final PlayerInteractEvent event) {
        if (item == Material.FIRE_CHARGE && Component.text("Armageddon")
                .decoration(TextDecoration.ITALIC, false).equals(name)) {
            final Player player = event.getPlayer();
            final World world = player.getWorld();

            final int min = -12;
            final int max = 12;
            final int iteratorSpacing = 4;

            for (int i = min; i <= max; i += iteratorSpacing) {
                final Location eyeLocation = player.getEyeLocation();

                final double x = i * Math.cos(Math.toRadians(eyeLocation.getYaw()));
                final double z = i * Math.sin(Math.toRadians(eyeLocation.getYaw()));

                final Vector velocity = eyeLocation.getDirection().multiply(12);
                final int yield = 5;

                final Fireball fireball = (Fireball) world.spawnEntity(
                    eyeLocation.add(x, 0, z),
                    EntityType.FIREBALL
                );

                fireball.customName(Component.text("WeaponArmegaddonCharge"));
                fireball.setDirection(velocity);
                fireball.setShooter(player);
                fireball.setYield(yield);
            }

            final Location eyeLocation = player.getEyeLocation();
            final float volume = 0.9F;
            final float pitch = 1.5F;

            world.playSound(
                eyeLocation,
                Sound.ENTITY_GHAST_SHOOT,
                volume,
                pitch
            );
            event.setCancelled(true);
        }
    }

    /* Make shooter invulnerable to weapon projectiles, and prevent charges from
    colliding with each other */
    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.FIREBALL) {
            return;
        }

        final Projectile projectile = event.getEntity();

        if (!Component.text("WeaponArmegaddonCharge").equals(projectile.customName())) {
            return;
        }

        final Entity collidedWith = event.getHitEntity();

        if (collidedWith == null) {
            return;
        }

        if ((collidedWith.getType() == EntityType.PLAYER
                && projectile.getShooter() instanceof Player
                && ((Player) projectile.getShooter()).getUniqueId().equals(
                    collidedWith.getUniqueId()))
                || collidedWith.getType() == EntityType.FIREBALL) {
            event.setCancelled(true);
        }
    }
}
