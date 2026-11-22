package pw.kaboom.weapons.modules.weapons;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class WeaponMachineGun implements Weapon, Listener {
    private static final int MAX_BULLET_COUNT = 20;
    private static final int POTION_DURATION = 90000;
    private static final int POTION_AMPLIFIER = 3;
    private static final double BULLET_SPEED = 12;

    private final Map<UUID, Integer> activeFiring = new HashMap<>();

    @Override
    public String getKey() {
        return "machinegun";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.GOLDEN_HORSE_ARMOR);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Machine Gun"));
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    @Override
    public boolean rightClick(final Player player) {
        activeFiring.putIfAbsent(player.getUniqueId(), 0);
        return true;
    }

    /* Make shooter invulnerable to weapon projectiles */
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
            final boolean ambient = true;
            final boolean particles = false;

            final PotionEffect harm = new PotionEffect(
                    PotionEffectType.INSTANT_DAMAGE,
                    POTION_DURATION,
                    POTION_AMPLIFIER,
                    ambient,
                    particles
            );

            ((LivingEntity) collidedWith).addPotionEffect(harm);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTickStart(final ServerTickStartEvent event) {
        if (activeFiring.isEmpty()) {
            return;
        }

        final Iterator<Map.Entry<UUID, Integer>> iterator = activeFiring.entrySet().iterator();

        while (iterator.hasNext()) {
            final Map.Entry<UUID, Integer> entry = iterator.next();
            final UUID playerUUID = entry.getKey();
            final Player player = Bukkit.getPlayer(playerUUID);
            int numFiredBullets = entry.getValue();

            if (player == null) {
                iterator.remove();
                continue;
            }

            final Location eyeLocation = player.getEyeLocation();
            final World world = player.getWorld();
            final Vector velocity = eyeLocation.getDirection().multiply(BULLET_SPEED);

            world.getChunkAtAsync(eyeLocation).thenAccept(_ -> {
                final Arrow arrow = player.launchProjectile(Arrow.class);
                final float volume = 1.0F;
                final float pitch = 63.0F;

                arrow.customName(Component.text("WeaponMachineGunBullet"));
                arrow.setShooter(player);
                arrow.setVelocity(velocity);

                world.playSound(eyeLocation, Sound.ENTITY_GENERIC_EXPLODE, volume, pitch);
            });

            numFiredBullets++;
            entry.setValue(numFiredBullets);

            if (numFiredBullets >= MAX_BULLET_COUNT) {
                iterator.remove();
            }
        }
    }
}
