package pw.kaboom.weapons.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;

public final class EntityUtils {
    private EntityUtils(){
    }

    public static void spawnFallingBlock(final Location location, final Material material) {
        final FallingBlock block = location.getWorld().spawn(location, FallingBlock.class);
        block.setBlockData(material.createBlockData());
    }
}
