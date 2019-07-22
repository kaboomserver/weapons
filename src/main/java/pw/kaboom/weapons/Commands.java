package pw.kaboom.weapons;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class CommandWeapons implements CommandExecutor {
	private void addWeapon(Inventory inventory, Material material, String name) {
		ItemStack item = new ItemStack​(material, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		inventory.addItem(item);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		Inventory inventory = Bukkit.createInventory(null, 18, "Weapons");
		addWeapon(inventory, Material.ANVIL, "§rAnvil Dropper");
		addWeapon(inventory, Material.SPECTRAL_ARROW, "§rArcher");
		addWeapon(inventory, Material.FIREBALL, "§rArmageddon");
		addWeapon(inventory, Material.MAGMA_CREAM, "§rBlobinator");
		addWeapon(inventory, Material.EGG, "§rGrenade");
		addWeapon(inventory, Material.BLAZE_POWDER, "§rLaser");
		addWeapon(inventory, Material.STICK, "§rLightning Stick");
		addWeapon(inventory, Material.GOLD_BARDING, "§rMachine Gun");
		addWeapon(inventory, Material.BLAZE_ROD, "§rNuker");
		addWeapon(inventory, Material.IRON_BARDING, "§rSniper");
		player.openInventory(inventory);
		return true;
	}
}
