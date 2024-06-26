package pw.kaboom.weapons.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponSniper implements Weapon {
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.IRON_HORSE_ARMOR)
                .name("Sniper")
                .build();
    }

    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) return false;
        if (event.action() == WeaponUseEvent.Action.RIGHT_CLICK) {
            handleZoom(event);
            return true;
        }

        final Player player = event.source();
        final Location eyeLocation = player.getEyeLocation();
        final Vector velocity = eyeLocation.getDirection().multiply(12);

        final Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setShooter(player);
        snowball.setVelocity(velocity);

        final World world = player.getWorld();
        final float volume = 1.0F;
        final float pitch = 63.0F;

        world.playSound(
                eyeLocation,
                Sound.BLOCK_PISTON_CONTRACT,
                volume,
                pitch
        );
        return true;
    }

    public void handleZoom(final @NotNull WeaponUseEvent info) {
        final Player player = info.source();
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
            return;
        }

        final int duration = 90000;
        final int amplifier = 7;

        final PotionEffect effect = new PotionEffect(
                PotionEffectType.SLOW,
                duration,
                amplifier
        );
        player.addPotionEffect(effect);
    }
}
