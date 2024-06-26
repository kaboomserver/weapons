package pw.kaboom.weapons.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponLightningStick implements Weapon {
    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.STICK)
                .name("Lightning Stick")
                .build();
    }


    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) return false;

        final Player player = event.source();
        final int maxDistance = 100;
        final Location lookLocation = player.getTargetBlock(null, maxDistance).getLocation();
        final World world = player.getWorld();

        world.strikeLightning(lookLocation);
        return true;
    }
}
