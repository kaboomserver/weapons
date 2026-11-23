package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponArmageddon implements Weapon, Listener {
    private static final int MIN = -12;
    private static final int MAX = 12;
    private static final int SPACING = 4;
    private static final int YIELD = 5;
    private static final double FIREBALL_SPEED = 12;

    @Override
    public String getKey() {
        return "armageddon";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.FIRE_CHARGE);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Armageddon"));
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    @Override
    public boolean leftClick(final Player player) {
        final World world = player.getWorld();

        for (int i = MIN; i <= MAX; i += SPACING) {
            final Location eyeLocation = player.getEyeLocation();

            final double x = i * Math.cos(Math.toRadians(eyeLocation.getYaw()));
            final double z = i * Math.sin(Math.toRadians(eyeLocation.getYaw()));

            final Vector velocity = eyeLocation.getDirection().multiply(FIREBALL_SPEED);

            final Fireball fireball = world.spawn(
                    eyeLocation.add(x, 0, z),
                    Fireball.class,
                    entity -> {
                        entity.setDirection(velocity);
                        entity.setShooter(player);
                        entity.setYield(YIELD);
                        entity.getPersistentDataContainer()
                                .set(Main.getPdcKey(), PersistentDataType.STRING, getKey());
                    }
            );

            fireball.customName(Component.text("WeaponArmageddonCharge"));
        }

        final Location eyeLocation = player.getEyeLocation();
        final float volume = 0.9F;
        final float pitch = 1.5F;

        world.playSound(eyeLocation, Sound.ENTITY_GHAST_SHOOT, volume, pitch);
        return true;
    }

    /* Make shooter invulnerable to weapon projectiles, and prevent charges from
    colliding with each other */
    @EventHandler
    void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.FIREBALL) {
            return;
        }

        final Projectile projectile = event.getEntity();
        final String maybeWeapon = projectile.getPersistentDataContainer().get(Main.getPdcKey(),
                PersistentDataType.STRING);
        if (maybeWeapon != null && maybeWeapon.equals(getKey())) {
            return;
        }

        final Entity collidedWith = event.getHitEntity();

        if (collidedWith == null) {
            return;
        }

        if ((collidedWith.getType() == EntityType.PLAYER
                && projectile.getShooter() instanceof Player
                && ((Player) projectile.getShooter()).getUniqueId().equals(
                collidedWith.getUniqueId()
        )) || collidedWith.getType() == EntityType.FIREBALL) {
            event.setCancelled(true);
        }
    }
}
