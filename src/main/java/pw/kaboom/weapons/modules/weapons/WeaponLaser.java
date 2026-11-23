package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class WeaponLaser implements Weapon {
    private static final int DISTANCE = 100;
    private static final int DUST_SIZE = 1;

    @Override
    public String getKey() {
        return "laser";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.BLAZE_POWDER);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Laser"));
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    @Override
    public boolean rightClick(final Player player) {
        final Location eyeLocation = player.getEyeLocation();
        final Vector direction = eyeLocation.getDirection();
        final Location particleLocation = player.getEyeLocation();
        final World world = player.getWorld();
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i <= DISTANCE; i++) {
            particleLocation.add(direction);

            final Particle.DustOptions dustOptions = new Particle.DustOptions(
                    random.nextBoolean() ? Color.TEAL : Color.RED,
                    DUST_SIZE
            );

            world.spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, dustOptions);
        }

        final Vector start = eyeLocation.toVector();
        final BlockIterator blockIterator = new BlockIterator(world, start, direction, 0, DISTANCE);

        while (blockIterator.hasNext()) {
            blockIterator.next().breakNaturally();
        }

        final float volume = 0.8F;
        final float pitch = 63.0F;

        world.playSound(eyeLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, volume, pitch);

        final float volume2 = 1.0F;
        final float pitch2 = 20.0F;

        world.playSound(eyeLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, volume2, pitch2);
        return true;
    }
}
