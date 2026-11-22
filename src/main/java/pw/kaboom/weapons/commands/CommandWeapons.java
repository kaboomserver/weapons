package pw.kaboom.weapons.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.WeaponMenuHolder;
import pw.kaboom.weapons.modules.weapons.Weapon;

import java.util.Map;

import static io.papermc.paper.command.brigadier.Commands.literal;

public final class CommandWeapons {
    private final Main plugin;

    public CommandWeapons(final Main plugin) {
        this.plugin = plugin;
    }

    public String getLabel() {
        return "weapons";
    }

    public String getDescription() {
        return "Gives you a weapon";
    }

    public void build(final LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder
                .requires(src -> src.getSender().hasPermission("weapons.command"))
                .executes(this::handleCommand);

        for (final Map.Entry<String, Weapon> weapon : plugin.getWeapons().entrySet()) {
            builder.then(literal(weapon.getKey())
                    .executes(ctx -> {
                        final Player sender = ctx.getSource().getPlayerOrThrow();
                        sender.give(weapon.getValue().getItemStack());
                        return Command.SINGLE_SUCCESS;
                    })
            );
        }
    }

    private int handleCommand(final CommandContext<CommandSourceStack> ctx)
            throws CommandSyntaxException {
        final Player sender = ctx.getSource().getPlayerOrThrow();

        final Inventory inventory = new WeaponMenuHolder().getInventory();
        for (final Weapon weapon : plugin.getWeapons().values()) {
            inventory.addItem(weapon.getItemStack());
        }
        sender.openInventory(inventory);

        return Command.SINGLE_SUCCESS;
    }
}
