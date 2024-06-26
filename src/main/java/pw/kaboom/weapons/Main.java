package pw.kaboom.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.kaboom.weapons.commands.CommandWeapons;
import pw.kaboom.weapons.modules.PlayerReceiveWeapon;
import pw.kaboom.weapons.projectiles.*;
import pw.kaboom.weapons.weapon.WeaponManager;
import pw.kaboom.weapons.projectile.ProjectileManager;
import pw.kaboom.weapons.weapons.*;

import java.util.Random;

public final class Main extends JavaPlugin {
    public static final Random RANDOM = new Random();
    private final ProjectileManager projectileManager =
            new ProjectileManager(key("projectile"));
    private final WeaponManager weaponManager =
            new WeaponManager(key("weapon"), Component.text("Weapons"));

    @Override
    public void onLoad() {
        // Projectiles
        projectileManager.add(key("archer_arrow"), new ArcherArrow());
        projectileManager.add(key("armageddon_charge"), new ArmageddonCharge());
        projectileManager.add(key("blobinator_ball"), new BlobinatorBall());
        projectileManager.add(key("explosive_arrow"), new ExplosiveArrow());
        projectileManager.add(key("grenade"), new Grenade());

        // Weapons
        weaponManager.add(key("anvil_dropper"), new WeaponAnvilDropper());
        weaponManager.add(key("archer"), new WeaponArcher(projectileManager, key("archer_arrow")));
        weaponManager.add(key("armageddon"),
                new WeaponArmageddon(projectileManager, key("armageddon_charge")));
        weaponManager.add(key("blobinator"),
                new WeaponBlobinator(projectileManager, key("blobinator_ball")));
        weaponManager.add(key("explosive_crossbow"),
                new WeaponExplosiveCrossbow(projectileManager, key("explosive_arrow")));
        weaponManager.add(key("grenade"),
                new WeaponGrenade(projectileManager, key("grenade")));
        weaponManager.add(key("laser"), new WeaponLaser());
        weaponManager.add(key("lightning_stick"), new WeaponLightningStick());
        weaponManager.add(key("sniper"), new WeaponSniper());
    }

    @Override
    public void onEnable() {
        // Modules
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(projectileManager, this);
        pluginManager.registerEvents(weaponManager, this);
        pluginManager.registerEvents(new PlayerReceiveWeapon(weaponManager), this);

        // Commands
        //noinspection DataFlowIssue
        this.getCommand("weapons").setExecutor(new CommandWeapons(weaponManager));
    }

    private static NamespacedKey key(final String key) {
        return new NamespacedKey("kaboom", key);
    }
}
