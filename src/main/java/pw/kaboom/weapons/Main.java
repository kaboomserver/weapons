package pw.kaboom.weapons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import pw.kaboom.weapons.commands.CommandWeapons;
import pw.kaboom.weapons.modules.player.PlayerReceiveWeapon;
import pw.kaboom.weapons.modules.player.PlayerUseWeapon;
import pw.kaboom.weapons.modules.weapons.WeaponArcher;
import pw.kaboom.weapons.modules.weapons.WeaponArmageddon;
import pw.kaboom.weapons.modules.weapons.WeaponBlobinator;
import pw.kaboom.weapons.modules.weapons.WeaponExplosiveCrossbow;
import pw.kaboom.weapons.modules.weapons.WeaponGrenade;
import pw.kaboom.weapons.modules.weapons.WeaponMachineGun;

public final class Main extends JavaPlugin {
    private static final HashSet<BlockFace> blockFaces = new HashSet<BlockFace>();
    private static final List<Material> colors = new ArrayList<Material>();

    @Override
    public void onLoad() {
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
        /* Commands */
        this.getCommand("weapons").setExecutor(new CommandWeapons());

        /* Player Events */
        this.getServer().getPluginManager().registerEvents(new PlayerReceiveWeapon(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerUseWeapon(), this);

        /* Weapon Events */
        this.getServer().getPluginManager().registerEvents(new WeaponArcher(), this);
        this.getServer().getPluginManager().registerEvents(new WeaponArmageddon(), this);
        this.getServer().getPluginManager().registerEvents(new WeaponBlobinator(), this);
        this.getServer().getPluginManager().registerEvents(new WeaponGrenade(), this);
        this.getServer().getPluginManager().registerEvents(new WeaponMachineGun(), this);
        this.getServer().getPluginManager().registerEvents(new WeaponExplosiveCrossbow(), this);
    }

    public static HashSet<BlockFace> getBlockFaces() {
        return blockFaces;
    }

    public static List<Material> getColors() {
        return colors;
    }
}
