package pw.kaboom.weapons.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Manager<V, T extends PersistentDataHolder> implements Listener {
    private final NamespacedKey containerKey;
    private final Map<NamespacedKey, V> map = new HashMap<>();

    protected Manager(final @NotNull NamespacedKey containerKey) {
        this.containerKey = containerKey;
    }

    public V add(final @NotNull NamespacedKey key, final @NotNull V value) {
        this.map.put(key, value);
        return value;
    }

    @UnmodifiableView
    public Map<NamespacedKey, V> entries() {
        return Collections.unmodifiableMap(this.map);
    }

    public @Nullable V get(final @Nullable NamespacedKey key) {
        if (key == null) {
            return null;
        }

        return this.map.get(key);
    }

    public @Nullable V get(final @Nullable T holder) {
        if (holder == null) {
            return null;
        }

        final PersistentDataContainer dataContainer = holder.getPersistentDataContainer();
        final String keyString = dataContainer.get(this.containerKey,
                PersistentDataType.STRING);
        if (keyString == null) {
            return null;
        }

        final NamespacedKey key;
        try {
            key = NamespacedKey.fromString(keyString);
        } catch (final Exception ignored) {
            return null;
        }

        return this.get(key);
    }

    public V remove(final @NotNull NamespacedKey key) {
        return this.map.remove(key);
    }

    public void tag(final @NotNull T holder, final @NotNull NamespacedKey key) {
        final PersistentDataContainer dataContainer = holder.getPersistentDataContainer();
        dataContainer.set(this.containerKey, PersistentDataType.STRING, key.asString());
    }
}
