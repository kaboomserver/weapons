package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;

import java.util.List;

public final class WeaponLightningStick implements Weapon {
    private static final int MAX_DISTANCE = 100;

    @Override
    public String getKey() {
        return "lightningstick";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.STICK);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Lightning Stick"));
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
        final Location lookLocation = player.getTargetBlock(null, MAX_DISTANCE)
                .getLocation();
        final World world = player.getWorld();

        world.strikeLightning(lookLocation);
        return true;
    }
}
