package pw.kaboom.weapons.modules.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;

public final class PlayerReceiveWeapon implements Listener {
    @EventHandler
    void onInventoryClick(final InventoryClickEvent event) {
        if (event.isCancelled() || !Component.text("Weapons").equals(event.getView().title())) {
            return;
        }

        final ItemStack item = event.getCurrentItem();
        final Component weaponName = item.getItemMeta().displayName();
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

