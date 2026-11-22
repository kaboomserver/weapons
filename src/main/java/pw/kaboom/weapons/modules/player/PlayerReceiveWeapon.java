package pw.kaboom.weapons.modules.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.WeaponMenuHolder;

import java.util.List;

public final class PlayerReceiveWeapon implements Listener {
    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        final InventoryView view = event.getView();
        final Inventory clickedInventory = event.getClickedInventory();

        if (event.isCancelled()
                || clickedInventory == null
                || !(view.getTopInventory().getHolder() instanceof WeaponMenuHolder)
                || !clickedInventory.equals(view.getTopInventory())) {
            return;
        }

        final ItemStack item = event.getCurrentItem();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        final List<String> keys = item.getItemMeta().getPersistentDataContainer()
                .get(Main.getPdcKey(), PersistentDataType.LIST.strings());

        if (keys == null || keys.stream().noneMatch(Main.getWeapons()::containsKey)) {
            return;
        }

        final Component weaponName = item.getItemMeta().customName();
        final Player player = (Player) event.getWhoClicked();

        player.getInventory().addItem(item);
        player.closeInventory();

        player.sendMessage(
                Component.text("You have received the ")
                        .append(weaponName != null ? weaponName : Component.text("weapon"))
                        .append(Component.text("!"))
        );
    }
}
