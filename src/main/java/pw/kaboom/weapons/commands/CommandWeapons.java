package pw.kaboom.weapons.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CommandWeapons implements CommandExecutor {
	private void addWeapon(final Inventory inventory, final Material material, final String name) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta itemMeta = item.getItemMeta();

		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		inventory.addItem(item);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Command has to be run by a player");
		} else {
			final Player player = (Player) sender;
			final Inventory inventory = Bukkit.createInventory(null, 18, "Weapons");

			addWeapon(inventory, Material.ANVIL, "\\\\u00A7rAnvil Dropper");
			addWeapon(inventory, Material.SPECTRAL_ARROW, "\\\\u00A7rArcher");
			addWeapon(inventory, Material.FIRE_CHARGE, "\\\\u00A7rArmageddon");
			addWeapon(inventory, Material.MAGMA_CREAM, "\\\\u00A7rBlobinator");
			addWeapon(inventory, Material.EGG, "\\\\u00A7rGrenade");
			addWeapon(inventory, Material.BLAZE_POWDER, "\\\\u00A7rLaser");
			addWeapon(inventory, Material.STICK, "\\\\u00A7rLightning Stick");
			addWeapon(inventory, Material.GOLDEN_HORSE_ARMOR, "\\\\u00A7rMachine Gun");
			addWeapon(inventory, Material.BLAZE_ROD, "\\\\u00A7rNuker");
			addWeapon(inventory, Material.IRON_HORSE_ARMOR, "\\\\u00A7rSniper");
			player.openInventory(inventory);
		}
		return true;
	}
}
