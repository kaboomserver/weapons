package pw.kaboom.weapons.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

public final class WeaponAnvilDropper implements Weapon {
    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.ANVIL)
                .name("Anvil Dropper")
                .build();
    }

    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (!event.action().isClick()) {
            return false;
        }

        final int min = -2;
        final int max = 2;

        for (int x = min; x <= max; x++) {
            for (int z = min; z <= max; z++) {
                final Location blockLocation = event.source().getLocation()
                        .subtract(x, 0, z);
                final World world = event.source().getWorld();

                world.spawnFallingBlock(
                        blockLocation,
                        Material.ANVIL.createBlockData()
                );
            }
        }

        return true;
    }
}
