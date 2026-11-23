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
import java.util.concurrent.ThreadLocalRandom;

public final class WeaponArcher implements Weapon, Listener {
    private static final int ARROW_COUNT = 20;
    private static final int SPREAD = 15;
    private static final double ARROW_SPEED = 8;
    private static final Component ARROW_NAME = Component.text("WeaponArcherArrow");

    @Override
    public String getKey() {
        return "archer";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.SPECTRAL_ARROW);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Archer"));
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
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i <= ARROW_COUNT; i++) {
            final double randomX = random.nextDouble(-SPREAD, SPREAD);
            final double randomY = random.nextDouble(-SPREAD, SPREAD);
            final double randomZ = random.nextDouble(-SPREAD, SPREAD);

            final Vector randomDirection = new Vector(randomX, randomY, randomZ)
                    .normalize().multiply(ARROW_SPEED);

            world.spawn(
                    player.getLocation(),
                    SpectralArrow.class,
                    entity -> {
                        entity.getPersistentDataContainer()
                                .set(Main.getPdcKey(), PersistentDataType.STRING, getKey());
                        entity.setShooter(player);
                        entity.setVelocity(randomDirection);
                    }
            );
        }

        final Location eyeLocation = player.getEyeLocation();
        final float volume = 1.0F;
        final float pitch = 1.5F;

        world.playSound(eyeLocation, Sound.BLOCK_ANVIL_DESTROY, volume, pitch);
        return true;
    }

    /* Make shooter invulnerable to weapon projectiles */
    @EventHandler
    void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.SPECTRAL_ARROW) {
            return;
        }

        final Projectile projectile = event.getEntity();

        final String maybeWeapon = projectile.getPersistentDataContainer()
                .get(Main.getPdcKey(),PersistentDataType.STRING);
        if (maybeWeapon!=null&&!maybeWeapon.equals(getKey())) {
            return;
        }

        final Entity collidedWith = event.getHitEntity();

        if (collidedWith != null
                && collidedWith.getType() == EntityType.PLAYER
                && projectile.getShooter() instanceof Player
                && ((Player) projectile.getShooter()).getUniqueId().equals(
                collidedWith.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
