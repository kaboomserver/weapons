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
import net.kyori.adventure.text.Component;

public final class CommandWeapons implements CommandExecutor {
    private void addWeapon(final Inventory inventory, final Material material,
                                         final Component name) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.displayName(name);
        item.setItemMeta(itemMeta);
        inventory.addItem(item);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label,
                             final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Component.text("Command has to be run by a player"));
        } else {
            final Player player = (Player) sender;
            final Inventory inventory = Bukkit.createInventory(null, 18, Component.text("Weapons"));

            addWeapon(inventory, Material.ANVIL, Component.text("Anvil Dropper"));
            addWeapon(inventory, Material.SPECTRAL_ARROW, Component.text("Archer"));
            addWeapon(inventory, Material.FIRE_CHARGE, Component.text("Armageddon"));
            addWeapon(inventory, Material.MAGMA_CREAM, Component.text("Blobinator"));
            addWeapon(inventory, Material.EGG, Component.text("Grenade"));
            addWeapon(inventory, Material.BLAZE_POWDER, Component.text("Laser"));
            addWeapon(inventory, Material.STICK, Component.text("Lightning Stick"));
            addWeapon(inventory, Material.GOLDEN_HORSE_ARMOR, Component.text("Machine Gun"));
            addWeapon(inventory, Material.BLAZE_ROD, Component.text("Nuker"));
            addWeapon(inventory, Material.IRON_HORSE_ARMOR, Component.text("Sniper"));
            player.openInventory(inventory);
        }
        return true;
    }
}
