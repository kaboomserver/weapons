package pw.kaboom.weapons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import pw.kaboom.weapons.commands.CommandWeapons;
import pw.kaboom.weapons.modules.player.PlayerFireWeapon;
import pw.kaboom.weapons.modules.player.PlayerReceiveWeapon;
import pw.kaboom.weapons.modules.weapons.*;

import java.util.*;

public final class Main extends JavaPlugin {
    private static final Set<BlockFace> blockFaces = new HashSet<>();
    private static final List<Material> colors = new ArrayList<>();
    private static final Map<String, Weapon> weapons = new LinkedHashMap<>();
    private static @MonotonicNonNull NamespacedKey pdcKey;

    public static Map<String, Weapon> getWeapons() {
        return Collections.unmodifiableMap(weapons);
    }

    public static NamespacedKey getPdcKey() {
        if (pdcKey == null) {
            throw new IllegalStateException("tried to get a null Main.key");
        }
        return pdcKey;
    }

    public static Set<BlockFace> getBlockFaces() {
        return Collections.unmodifiableSet(blockFaces);
    }

    public static List<Material> getColors() {
        return Collections.unmodifiableList(colors);
    }

    @Override
    @EnsuresNonNull("key")
    public void onLoad() {
        pdcKey = new NamespacedKey(this, "weapon");

        /* Fill lists */
        Collections.addAll(
                blockFaces,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.WEST,
                BlockFace.EAST,
                BlockFace.UP,
                BlockFace.DOWN
        );

        Collections.addAll(
                colors,
                Material.BLACK_WOOL,
                Material.BLUE_WOOL,
                Material.BROWN_WOOL,
                Material.CYAN_WOOL,
                Material.GRAY_WOOL,
                Material.GREEN_WOOL,
                Material.LIGHT_BLUE_WOOL,
                Material.LIGHT_GRAY_WOOL,
                Material.LIME_WOOL,
                Material.MAGENTA_WOOL,
                Material.ORANGE_WOOL,
                Material.PINK_WOOL,
                Material.PURPLE_WOOL,
                Material.RED_WOOL,
                Material.WHITE_WOOL,
                Material.YELLOW_WOOL
        );
    }

    @Override
    public void onEnable() {
        /* Weapon Events */
        registerWeapon(new WeaponAnvilDropper());
        registerWeapon(new WeaponArcher());
        registerWeapon(new WeaponArmageddon());
        registerWeapon(new WeaponBlobinator());
        registerWeapon(new WeaponExplosiveCrossbow());
        registerWeapon(new WeaponGrenade());
        registerWeapon(new WeaponLaser());
        registerWeapon(new WeaponLightningStick());
        registerWeapon(new WeaponMachineGun());
        registerWeapon(new WeaponNuker());
        registerWeapon(new WeaponSniper());

        /* Commands */
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registrar = event.registrar();
            final CommandWeapons command = new CommandWeapons();
            final LiteralArgumentBuilder<CommandSourceStack> builder
                    = Commands.literal(command.getLabel());
            command.build(builder);
            registrar.register(
                    builder.build(), command.getDescription()
            );
        });

        /* Player Events */
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerFireWeapon(), this);
        pm.registerEvents(new PlayerReceiveWeapon(), this);
    }

    private void registerWeapon(final Weapon weapon) {
        weapons.put(weapon.getKey(), weapon);

        if (weapon instanceof final Listener listener) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
