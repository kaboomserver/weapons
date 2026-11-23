package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Egg;
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

public sealed class WeaponDupingGrenade implements Weapon, Listener permits WeaponGrenade {
    private static final float EXPLOSION_POWER = 6;

    @Override
    public String getKey() {
        return "dupinggrenade";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.EGG);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Duplicating Grenade"));
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

        final Egg egg = player.launchProjectile(Egg.class, null, projectile -> {
            projectile.getPersistentDataContainer()
                    .set(Main.getPdcKey(), PersistentDataType.LIST.strings(), List.of(getKey()));
            projectile.setShooter(player);
        });

        final World world = player.getWorld();
        final float volume = 1.0F;
        final float pitch = 1.0F;

        world.playSound(eyeLocation, Sound.ENTITY_EGG_THROW, volume, pitch);
        return true;
    }

    @EventHandler
    void onPlayerEggThrow(final PlayerEggThrowEvent event) {
        if (isGrenade(event.getEgg())) {
            event.setHatching(false);
        }
    }

    @EventHandler
    void onProjectileHit(final ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();
        if (isGrenade(projectile)) {
            final Location location = projectile.getLocation();
            final World world = location.getWorld();

            world.createExplosion(location, EXPLOSION_POWER);
        }
    }

    private boolean isGrenade(final Projectile projectile) {
        final List<String> maybeWeapon = projectile.getPersistentDataContainer()
                .get(Main.getPdcKey(), PersistentDataType.LIST.strings());
        return maybeWeapon != null && maybeWeapon.contains(getKey());
    }
}
