package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponSniper implements Weapon {
    private static final double SNOWBALL_SPEED = 12;
    private static final int SLOWNESS_DURATION = 90000;
    private static final int SLOWNESS_AMPLIFIER = 7;

    @Override
    public String getKey() {
        return "sniper";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.IRON_HORSE_ARMOR);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Sniper"));
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    // TODO: needs damage tuning. a sniper that shoots.... snowballs? are u fr
    @Override
    public boolean leftClick(final Player player) {
        final Location eyeLocation = player.getEyeLocation();
        final Vector velocity = eyeLocation.getDirection().multiply(SNOWBALL_SPEED);

        final Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setShooter(player);
        snowball.setVelocity(velocity);

        final World world = player.getWorld();
        final float volume = 1.0F;
        final float pitch = 63.0F;

        world.playSound(eyeLocation, Sound.BLOCK_PISTON_CONTRACT, volume, pitch);
        return true;
    }

    @Override
    public boolean rightClick(final Player player) {
        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
        } else {
            final PotionEffect effect = new PotionEffect(
                    PotionEffectType.SLOWNESS,
                    SLOWNESS_DURATION,
                    SLOWNESS_AMPLIFIER
            );
            player.addPotionEffect(effect);
        }
        return true;
    }
}
