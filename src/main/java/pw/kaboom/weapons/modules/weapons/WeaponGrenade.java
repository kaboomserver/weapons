package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponGrenade implements Weapon, Listener {
    private static final float EXPLOSION_POWER = 6;

    @Override
    public String getKey() {
        return "grenade";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.EGG);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Grenade"));
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
        final Location eyeLocation = player.getEyeLocation();

        final Egg egg = player.launchProjectile(Egg.class);
        egg.customName(Component.text("WeaponGrenade"));
        egg.setShooter(player);

        final World world = player.getWorld();
        final float volume = 1.0F;
        final float pitch = 1.0F;

        world.playSound(eyeLocation, Sound.ENTITY_EGG_THROW, volume, pitch);
        return true;
    }

    @EventHandler
    private void onPlayerEggThrow(final PlayerEggThrowEvent event) {
        if (Component.text("WeaponGrenade").equals(event.getEgg().customName())) {
            event.setHatching(false);
        }
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.EGG) {
            return;
        }

        final Projectile projectile = event.getEntity();

        if (Component.text("WeaponGrenade").equals(projectile.customName())) {
            final Location location = projectile.getLocation();
            final World world = location.getWorld();

            world.createExplosion(location, EXPLOSION_POWER);
            // seems our eggs don't despawn on hit? just kill it explicitly for now
            projectile.remove();
        }
    }
}
