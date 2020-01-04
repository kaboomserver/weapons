package pw.kaboom.weapons.modules.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import pw.kaboom.weapons.modules.weapons.WeaponAnvilDropper;
import pw.kaboom.weapons.modules.weapons.WeaponArcher;
import pw.kaboom.weapons.modules.weapons.WeaponArmageddon;
import pw.kaboom.weapons.modules.weapons.WeaponBlobinator;
import pw.kaboom.weapons.modules.weapons.WeaponGrenade;
import pw.kaboom.weapons.modules.weapons.WeaponLaser;
import pw.kaboom.weapons.modules.weapons.WeaponLightningStick;
import pw.kaboom.weapons.modules.weapons.WeaponMachineGun;
import pw.kaboom.weapons.modules.weapons.WeaponNuker;
import pw.kaboom.weapons.modules.weapons.WeaponSniper;

public final class PlayerUseWeapon implements Listener {
	@EventHandler
	void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.hasItem()
				&& event.getItem().getItemMeta().hasDisplayName()) {
			final Action action = event.getAction();
			final Material item = event.getMaterial();
			String name = "";

			try {
				name = event.getItem().getItemMeta().getDisplayName();
			} catch (Exception ignored) {
			}

			if (action == Action.LEFT_CLICK_AIR
					|| action == Action.LEFT_CLICK_BLOCK) {
				WeaponAnvilDropper.leftClick(item, name, event);
				WeaponArcher.leftClick(item, name, event);
				WeaponArmageddon.leftClick(item, name, event);
				WeaponBlobinator.leftClick(item, name, event);
				WeaponLightningStick.leftClick(item, name, event);
				WeaponNuker.leftClick(item, name, event);
				WeaponSniper.leftClick(item, name, event);
			} else if (action == Action.RIGHT_CLICK_AIR
					|| action == Action.RIGHT_CLICK_BLOCK) {
				WeaponGrenade.rightClick(item, name, event);
				WeaponLaser.rightClick(item, name, event);
				WeaponMachineGun.rightClick(item, name, event);
				WeaponSniper.rightClick(item, name, event);
			}
		}
	}
}
