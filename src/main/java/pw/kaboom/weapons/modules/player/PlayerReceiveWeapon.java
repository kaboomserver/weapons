package pw.kaboom.weapons.modules.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public final class PlayerReceiveWeapon implements Listener {
    @EventHandler
    void onInventoryClick(final InventoryClickEvent event) {
        final Component weaponViewName = Component.text("Weapons");
        final InventoryView view = event.getView();
        final Component viewTitle = view.title();

        if (event.isCancelled() || !viewTitle.equals(weaponViewName)) {
            return;
        }

        final Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (!clickedInventory.equals(view.getTopInventory())) {
            return;
        }

        final ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        final Component weaponName = item.getItemMeta().displayName();

        if (weaponName == null) {
            return;
        }

        final Player player = (Player) event.getWhoClicked();

        player.getInventory().addItem(item);
        player.closeInventory();
        player.sendMessage(
            Component.text("You have received the ")
                .append(weaponName)
                .append(Component.text("!"))
        );
    }
}

