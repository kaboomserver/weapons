package pw.kaboom.weapons.weapon;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.kaboom.weapons.manager.Manager;
import pw.kaboom.weapons.weapon.event.WeaponShootEvent;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

import java.util.Map;

public final class WeaponManager extends Manager<Weapon, ItemMeta> {
    private final Component inventoryTitle;
    private Inventory inventory;

    public WeaponManager(final @NotNull NamespacedKey containerKey,
                         final @NotNull Component inventoryTitle) {
        super(containerKey);
        this.inventoryTitle = inventoryTitle;
        this.updateInventory();
    }

    private void updateInventory() {
        final Map<NamespacedKey, Weapon> weapons = this.entries();

        int slots = Math.max((int) Math.ceil((double) weapons.size() / 9), 1);
        final Inventory newInventory = Bukkit.createInventory(null, slots * 9, this.inventoryTitle);

        int i = 0;
        for (final Map.Entry<NamespacedKey, Weapon> entry : weapons.entrySet()) {
            final ItemStack item = entry.getValue().item();
            this.tag(item, entry.getKey());
            newInventory.setItem(i, item);

            i++;
        }

        this.inventory = newInventory;
    }

    public Inventory inventory() {
        return inventory;
    }

    @Override
    public Weapon add(@NotNull NamespacedKey key, @NotNull Weapon value) {
        final Weapon weapon = super.add(key, value);

        this.updateInventory();
        return weapon;
    }

    @Override
    public Weapon remove(@NotNull NamespacedKey key) {
        final Weapon weapon = super.remove(key);

        this.updateInventory();
        return weapon;
    }

    public @Nullable Weapon get(final @Nullable ItemStack item) {
        if (item == null) {
            return null;
        }

        return this.get(item.getItemMeta());
    }

    public void tag(final @NotNull ItemStack item, final @NotNull NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        this.tag(meta, key);
        item.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerInteract(final PlayerInteractEvent bukkitEvent) {
        if (bukkitEvent.useInteractedBlock() == Event.Result.DENY
                && bukkitEvent.useItemInHand() == Event.Result.DENY) {
            return;
        }

        final Weapon weapon = this.get(bukkitEvent.getItem());
        if (weapon == null) {
            return;
        }

        final WeaponUseEvent event = WeaponUseEvent.fromPlayerInteractEvent(bukkitEvent);
        if (event == null) {
            return;
        }

        if (weapon.use(event)) {
            bukkitEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onEntityShootBow(final EntityShootBowEvent bukkitEvent) {
        final Weapon weapon = this.get(bukkitEvent.getBow());
        if (weapon == null) {
            return;
        }

        final WeaponShootEvent event = WeaponShootEvent.fromEntityShootBowEvent(bukkitEvent);
        if (event == null) {
            return;
        }

        final Entity newProjectile = weapon.shoot(event);
        if (newProjectile == null) {
            bukkitEvent.setCancelled(true);
            return;
        }

        if (newProjectile != bukkitEvent.getProjectile()) {
            bukkitEvent.setProjectile(newProjectile);
        }
    }
}
