package pw.kaboom.weapons.modules.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pw.kaboom.weapons.Main;
import pw.kaboom.weapons.modules.weapons.Weapon;

import java.util.List;

public final class PlayerFireWeapon implements Listener {
    private final Main plugin;

    public PlayerFireWeapon(final Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if ((event.useInteractedBlock() == Event.Result.DENY
                && event.useItemInHand() == Event.Result.DENY)) {
            return;
        }

        final Action action = event.getAction();
        final ItemStack item = event.getItem();
        final Player player = event.getPlayer();

        // we do our own null check here to make IDE happy
        if (!event.hasItem() || item == null || !item.hasItemMeta()) {
            return;
        }

        final List<String> keys = item.getItemMeta().getPersistentDataContainer()
                .get(Main.getPdcKey(), PersistentDataType.LIST.strings());

        if (keys == null) {
            return;
        }

        for (final String key : keys) {
            final Weapon weapon = plugin.getWeapons().get(key);

            if (weapon == null) {
                continue;
            }

            if (action.isLeftClick()) {
                if (weapon.leftClick(player)) {
                    event.setCancelled(true);
                }
            } else if (action.isRightClick()) {
                if (weapon.rightClick(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
