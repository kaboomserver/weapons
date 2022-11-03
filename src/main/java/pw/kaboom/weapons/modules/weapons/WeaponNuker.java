package pw.kaboom.weapons.modules.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public final class WeaponNuker {
    private WeaponNuker() {
    }

    public static void leftClick(final Material item, final Component name,
                                 final PlayerInteractEvent event) {
        if (item == Material.BLAZE_ROD && Component.text("Nuker")
                .decoration(TextDecoration.ITALIC, false).equals(name)) {
            final Player player = event.getPlayer();
            final Location eyeLocation = player.getEyeLocation();
            final Vector velocity = eyeLocation.getDirection().multiply(10);
            final int yield = 8;

            final Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setShooter(player);
            fireball.setVelocity(velocity);
            fireball.setYield(yield);

            final World world = player.getWorld();
            final float volume = 0.9F;
            final float pitch = 1.5F;

            world.playSound(
                eyeLocation,
                Sound.ENTITY_GHAST_SHOOT,
                volume,
                pitch
            );

            final float volume2 = 0.8F;
            final float pitch2 = 2.0F;

            world.playSound(
                eyeLocation,
                Sound.ENTITY_BAT_TAKEOFF,
                volume2,
                pitch2
            );
            event.setCancelled(true);
        }
    }
}
