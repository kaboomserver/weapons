package pw.kaboom.weapons.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import pw.kaboom.weapons.Main;

public final class BlockUtil {
    public static final Material[] WOOL_MATERIALS = {
            Material.WHITE_WOOL, Material.BLACK_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL,
            Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL,
            Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL
    };

    private BlockUtil() {}

    public static Material randomWool() {
        return WOOL_MATERIALS[Main.RANDOM.nextInt(WOOL_MATERIALS.length)];
    }

    public static void sphereFill(final Block source, final Material material,
                                  final int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final double euclideanDist = Math.sqrt((x * x) + (y * y) + (z * z));
                    if (euclideanDist > radius) continue;

                    final Block block = source.getRelative(x, y, z);
                    if (!block.isSolid()) {
                        continue;
                    }

                    block.setType(material, false);
                }
            }
        }
    }
}
