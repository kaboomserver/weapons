package pw.kaboom.weapons;

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

class CommandWeapons implements CommandExecutor {
	private void addWeapon(Inventory inventory, Material material, String name) {
		final ItemStack item = new ItemStack​(material, 1);
		final ItemMeta itemMeta = item.getItemMeta();

		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		inventory.addItem(item);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Command has to be run by a player");
		} else {
			final Player player = (Player) sender;
			final Inventory inventory = Bukkit.createInventory(null, 18, "Weapons");
	
			addWeapon(inventory, Material.ANVIL, "§rAnvil Dropper");
			addWeapon(inventory, Material.SPECTRAL_ARROW, "§rArcher");
			addWeapon(inventory, Material.FIRE_CHARGE, "§rArmageddon");
			addWeapon(inventory, Material.MAGMA_CREAM, "§rBlobinator");
			addWeapon(inventory, Material.EGG, "§rGrenade");
			addWeapon(inventory, Material.BLAZE_POWDER, "§rLaser");
			addWeapon(inventory, Material.STICK, "§rLightning Stick");
			addWeapon(inventory, Material.GOLDEN_HORSE_ARMOR, "§rMachine Gun");
			addWeapon(inventory, Material.BLAZE_ROD, "§rNuker");
			addWeapon(inventory, Material.IRON_HORSE_ARMOR, "§rSniper");
			player.openInventory(inventory);
		}
		return true;
	}
}
