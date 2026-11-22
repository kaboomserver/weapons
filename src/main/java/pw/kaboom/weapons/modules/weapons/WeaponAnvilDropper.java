package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.utils.EntityUtils;

import java.util.List;

public final class WeaponAnvilDropper implements Weapon {
    private static final int MIN = -2;
    private static final int MAX = 2;

    @Override
    public String getKey() {
        return "anvildropper";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.ANVIL);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Anvil Dropper"));
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
        for (int x = MIN; x <= MAX; x++) {
            for (int z = MIN; z <= MAX; z++) {
                final Location blockLocation = player.getLocation()
                        .subtract(x, 0, z);
                EntityUtils.spawnFallingBlock(blockLocation, Material.ANVIL);
            }
        }
        return true;
    }
}
