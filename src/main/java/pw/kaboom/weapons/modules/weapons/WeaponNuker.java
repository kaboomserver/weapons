package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponNuker implements Weapon {
    private static final double FIREBALL_SPEED = 10;
    private static final int YIELD = 8;

    @Override
    public String getKey() {
        return "nuker";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.BLAZE_ROD);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Nuker"));
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
        final Location eyeLocation = player.getEyeLocation();
        final Vector velocity = eyeLocation.getDirection().multiply(FIREBALL_SPEED);

        final Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setShooter(player);
        fireball.setVelocity(velocity);
        fireball.setYield(YIELD);

        final World world = player.getWorld();
        final float volume = 0.9F;
        final float pitch = 1.5F;

        world.playSound(eyeLocation, Sound.ENTITY_GHAST_SHOOT, volume, pitch);

        final float volume2 = 0.8F;
        final float pitch2 = 2.0F;

        world.playSound(eyeLocation, Sound.ENTITY_BAT_TAKEOFF, volume2, pitch2);
        return true;
    }
}
