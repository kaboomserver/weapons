package pw.kaboom.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class WeaponMenuHolder implements InventoryHolder {
    public static final Component TITLE = Component.text("Weapons");
    private static final int SIZE = 18;

    private final Inventory inventory;

    public WeaponMenuHolder() {
        this.inventory = Bukkit.createInventory(this, SIZE, TITLE);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
