package pw.kaboom.weapons.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pw.kaboom.weapons.WeaponItemBuilder;

public final class CommandWeapons implements CommandExecutor {
    private void addWeapon(final Inventory inventory, final ItemStack item) {
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

            addWeapon(inventory, WeaponItemBuilder.builder(Material.ANVIL)
                .name("Anvil Dropper")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.SPECTRAL_ARROW)
                .name("Archer")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.FIRE_CHARGE)
                .name("Armageddon")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.MAGMA_CREAM)
                .name("Blobinator")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.EGG)
                .name("Grenade")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.BLAZE_POWDER)
                .name("Laser")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.STICK)
                .name("Lightning Stick")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.GOLDEN_HORSE_ARMOR)
                .name("Machine Gun")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.BLAZE_ROD)
                .name("Nuker")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.IRON_HORSE_ARMOR)
                .name("Sniper")
                .build());
            addWeapon(inventory, WeaponItemBuilder.builder(Material.CROSSBOW)
                .name("Explosive Crossbow")
                .build());

            player.openInventory(inventory);
        }
        return true;
    }
}
