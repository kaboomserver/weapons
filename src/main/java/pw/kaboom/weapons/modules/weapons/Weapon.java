package pw.kaboom.weapons.modules.weapons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Weapon {
    String getKey();

    ItemStack getItemStack();

    default boolean leftClick(final Player player) {
        return false;
    }

    default boolean rightClick(final Player player) {
        return false;
    }
}
