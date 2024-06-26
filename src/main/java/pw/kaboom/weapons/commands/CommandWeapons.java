package pw.kaboom.weapons.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.WeaponManager;

public final class CommandWeapons implements CommandExecutor {
    private final WeaponManager weaponManager;

    public CommandWeapons(final WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd,
                             final @NotNull String label, final String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage(Component.text("Command has to be run by a player"));
            return true;
        }

        player.openInventory(this.weaponManager.inventory());
        return true;
    }
}
