package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponGrenade extends WeaponDupingGrenade implements Weapon, Listener {
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

    @Override
    @EventHandler
    void onProjectileHit(final ProjectileHitEvent event) {
        super.onProjectileHit(event);
        event.getEntity().remove();
    }
}
