package pw.kaboom.weapons.modules.weapons;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;

import net.kyori.adventure.text.Component;

public final class WeaponLightningStick {
    private WeaponLightningStick() {
    }

    public static void leftClick(final Material item, final Component name,
                                 final PlayerInteractEvent event) {
        if (item == Material.STICK && Component.text("Lightning Stick").equals(name)) {
            final int maxDistance = 100;
            final Location lookLocation = event.getPlayer().getTargetBlock(
                (Set<Material>) null, maxDistance).getLocation();
            final World world = event.getPlayer().getWorld();

            world.strikeLightning(lookLocation);
            event.setCancelled(true);
        }
    }
}
