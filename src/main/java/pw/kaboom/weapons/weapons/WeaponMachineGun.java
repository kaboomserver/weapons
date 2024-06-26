package pw.kaboom.weapons.weapons;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;

public final class WeaponMachineGun implements Weapon {
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.GOLDEN_HORSE_ARMOR)
                .name("Machine Gun")
                .build();
    }

    private static final int MAX_BULLET_COUNT = 20;
    private static HashMap<UUID, Integer> machineGunActive = new HashMap<UUID, Integer>();

    public static void rightClick(final Material item, final Component name,
                                  final PlayerInteractEvent event) {
        if (item == Material.GOLDEN_HORSE_ARMOR && Component.text("Machine Gun")
                .decoration(TextDecoration.ITALIC, false).equals(name)) {
            final UUID playerUUID = event.getPlayer().getUniqueId();

            if (!machineGunActive.containsKey(playerUUID)) {
                final int numFiredBullets = 0;
                machineGunActive.put(playerUUID, numFiredBullets);
            }
            event.setCancelled(true);
        }
    }

    /* Make shooter invulnerable to weapon projectiles
    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW) {
            return;
        }

        final Projectile projectile = event.getEntity();

        if (!Component.text("WeaponMachineGunBullet").equals(projectile.customName())) {
            return;
        }

        final Entity collidedWith = event.getHitEntity();
        projectile.remove();

        if (collidedWith == null) {
            return;
        }

        if (collidedWith.getType() == EntityType.PLAYER
                && projectile.getShooter() instanceof Player
                && ((Player) projectile.getShooter()).getUniqueId().equals(
                    collidedWith.getUniqueId())) {
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

            ((LivingEntity) collidedWith).addPotionEffect(harm);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTickStart(ServerTickStartEvent event) {
        if (machineGunActive.isEmpty()) {
            return;
        }

        Iterator<Entry<UUID, Integer>> iterator = machineGunActive.entrySet().iterator();

        while (iterator.hasNext()) {
            final Entry<UUID, Integer> entry = iterator.next();
            final UUID playerUUID = entry.getKey();
            final Player player = Bukkit.getPlayer(playerUUID);
            int numFiredBullets = entry.getValue();

            if (player == null) {
                iterator.remove();
                continue;
            }

            final Location eyeLocation = player.getEyeLocation();
            final World world = player.getWorld();
            final Vector velocity = eyeLocation.getDirection().multiply(12);

            world.getChunkAtAsync(eyeLocation).thenAccept(chunk -> {
                final Arrow arrow = player.launchProjectile(Arrow.class);
                final float volume = 1.0F;
                final float pitch = 63.0F;

                arrow.customName(Component.text("WeaponMachineGunBullet"));
                arrow.setShooter(player);
                arrow.setVelocity(velocity);

                world.playSound(
                    eyeLocation,
                    Sound.ENTITY_GENERIC_EXPLODE,
                    volume,
                    pitch
                );
            });

            numFiredBullets++;
            machineGunActive.put(playerUUID, numFiredBullets);

            if (numFiredBullets >= MAX_BULLET_COUNT) {
                iterator.remove();
            }
        }
    }*/
}
