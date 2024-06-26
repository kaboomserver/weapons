package pw.kaboom.weapons.weapon.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WeaponUseEvent(@NotNull Player source, @NotNull ItemStack item,
                             @NotNull Action action) {
    public static @Nullable WeaponUseEvent fromPlayerInteractEvent(
            final @NotNull PlayerInteractEvent event) {
        final Action action;
        switch (event.getAction()) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> action = Action.LEFT_CLICK;
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> action = Action.RIGHT_CLICK;
            default -> {
                return null;
            }
        }

        final ItemStack item = event.getItem();
        if (item == null) {
            return null;
        }

        return new WeaponUseEvent(event.getPlayer(), event.getItem(), action);
    }

    public enum Action {
        LEFT_CLICK,
        RIGHT_CLICK;

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isClick() {
            return this == LEFT_CLICK || this == RIGHT_CLICK;
        }
    }
}
