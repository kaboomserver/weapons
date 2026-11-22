package pw.kaboom.weapons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.kaboom.weapons.commands.CommandWeapons;
import pw.kaboom.weapons.modules.player.PlayerFireWeapon;
import pw.kaboom.weapons.modules.player.PlayerReceiveWeapon;
import pw.kaboom.weapons.modules.weapons.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin {
    private static NamespacedKey pdcKey;
    private final Map<String, Weapon> weapons = new LinkedHashMap<>(12);

    public static NamespacedKey getPdcKey() {
        if (pdcKey == null) {
            throw new IllegalStateException("tried to get a null Main.key");
        }
        return pdcKey;
    }

    public Map<String, Weapon> getWeapons() {
        return Collections.unmodifiableMap(weapons);
    }

    @Override
    public void onLoad() {
        pdcKey = new NamespacedKey(this, "weapon");
    }

    @Override
    public void onEnable() {
        /* Weapon Events */
        for (final Weapon weapon : List.of(
                new WeaponAnvilDropper(),
                new WeaponArcher(),
                new WeaponArmageddon(),
                new WeaponBlobinator(),
                new WeaponDupingGrenade(),
                new WeaponExplosiveCrossbow(),
                new WeaponGrenade(),
                new WeaponLaser(),
                new WeaponLightningStick(),
                new WeaponMachineGun(),
                new WeaponNuker(),
                new WeaponSniper()
        )) {
            registerWeapon(weapon);
        }

        /* Commands */
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registrar = event.registrar();
            final CommandWeapons command = new CommandWeapons(this);
            final LiteralArgumentBuilder<CommandSourceStack> builder
                    = Commands.literal(command.getLabel());
            command.build(builder);
            registrar.register(
                    builder.build(), command.getDescription(), command.getAliases()
            );
        });

        /* Player Events */
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerFireWeapon(this), this);
        pm.registerEvents(new PlayerReceiveWeapon(this), this);
    }

    private void registerWeapon(final Weapon weapon) {
        weapons.put(weapon.getKey(), weapon);

        if (weapon instanceof final Listener listener) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
