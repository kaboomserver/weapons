package pw.kaboom.weapons.modules.weapons;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;

import pw.kaboom.weapons.Main;

public final class WeaponMachineGun implements Listener {
    private static HashSet<UUID> machineGunActive = new HashSet<UUID>();

    public static void rightClick(final Material item, final String name, final PlayerInteractEvent event) {
        if (item == Material.GOLDEN_HORSE_ARMOR
                && ("§rMachine Gun".equals(name) || "Machine Gun".equals(name))) {
            final UUID playerUUID = event.getPlayer().getUniqueId();

            if (!machineGunActive.contains(playerUUID)) {
                machineGunActive.add(playerUUID);
                final int maxBulletCount = 20;

                new BukkitRunnable() {
                    private int i;

                    @Override
                    public void run() {
                        final Player player = event.getPlayer();
                        final Location eyeLocation = player.getEyeLocation();
                        final World world = player.getWorld();
                        final Vector velocity = eyeLocation.getDirection().multiply(12);

                        final Arrow arrow = player.launchProjectile(Arrow.class);

                        arrow.setCustomName("WeaponMachineGunBullet");
                        arrow.setShooter(player);
                        arrow.setVelocity(velocity);

                        final float volume = 1.0F;
                        final float pitch = 63.0F;

                        world.playSound(
                            eyeLocation,
                            Sound.ENTITY_GENERIC_EXPLODE,
                            volume,
                            pitch
                        );

                        i++;

                        if (i >= maxBulletCount) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0, 1);

                machineGunActive.remove(playerUUID);
            }
            event.setCancelled(true);
        }
    }

    /* Make shooter invulnerable to weapon projectiles */
    @EventHandler
    private void onProjectileCollide(final ProjectileCollideEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            final Projectile projectile = event.getEntity();

            if ("WeaponMachineGunBullet".equals(projectile.getCustomName())) {
                final Entity collidedWith = event.getCollidedWith();

                if (collidedWith.getType() == EntityType.PLAYER
                        && projectile.getShooter() instanceof Player
                        && ((Player) projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) {
                    event.setCancelled(true);
                } else if (collidedWith instanceof LivingEntity) {
                    final int duration = 90000;
                    final int amplifier = 3;
                    final boolean ambient = true;
                    final boolean particles = false;

                    final PotionEffect harm = new PotionEffect(
                        PotionEffectType.HARM,
                        duration,
                        amplifier,
                        ambient,
                        particles
                    );

                    ((LivingEntity) collidedWith).addPotionEffect(harm, true);
                }
            }
        }
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            final Projectile projectile = event.getEntity();

            if ("WeaponMachineGunBullet".equals(projectile.getCustomName())) {
                projectile.remove();
            }
        }
    }
}
