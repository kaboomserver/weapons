package pw.kaboom.weapons.modules;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponManager;

public final class PlayerReceiveWeapon implements Listener {
    private final WeaponManager weaponManager;

    public PlayerReceiveWeapon(final WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    @EventHandler(ignoreCancelled = true)
    void onInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() != this.weaponManager.inventory()) {
            return;
        }

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();
        final Weapon weapon = this.weaponManager.get(item);
        if (weapon == null) {
            return;
        }

        final HumanEntity player = event.getWhoClicked();
        player.getInventory().addItem(item);
        player.closeInventory();
        player.sendMessage(Component.empty()
                .append(Component.text("You have received the "))
                .append(item.displayName())
                .append(Component.text("!"))
        );
    }
}

