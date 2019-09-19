package pw.kaboom.weapons;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;

class PlayerReceiveWeapon implements Listener {
	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		if ("Weapons".equals(event.getView().getTitle())) {
			final ItemStack item = event.getCurrentItem();

			if (item.getItemMeta().hasDisplayName() == true) {
				final Player player = (Player) event.getWhoClicked();

				player.getInventory().addItem(item);
				player.closeInventory();
				player.sendMessage("You have received the " + item.getItemMeta().getDisplayName().toLowerCase() + "!");
			}
		}
	}
}
