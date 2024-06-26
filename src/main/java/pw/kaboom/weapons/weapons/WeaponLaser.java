package pw.kaboom.weapons.weapons;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pw.kaboom.weapons.weapon.Weapon;
import pw.kaboom.weapons.weapon.WeaponItemBuilder;
import pw.kaboom.weapons.weapon.event.WeaponUseEvent;

import java.util.Random;

public final class WeaponLaser implements Weapon {
    @Override
    public @NotNull ItemStack item() {
        return WeaponItemBuilder.builder(Material.BLAZE_POWDER)
                .name("Laser")
                .build();
    }

    @Override
    public boolean use(final @NotNull WeaponUseEvent event) {
        if (event.action() != WeaponUseEvent.Action.RIGHT_CLICK) {
            return false;
        }

        final Player player = event.source();
        final Location eyeLocation = player.getEyeLocation();
        final Vector direction = eyeLocation.getDirection();
        final int distance = 100;
        final Location particleLocation = player.getEyeLocation();
        final World world = player.getWorld();

        for (int i = 0; i <= distance; i++) {
            particleLocation.add(direction);

            final int count = 1;
            final double offsetX = 0;
            final double offsetY = 0;
            final double offsetZ = 0;

            final Random random = new Random();
            final int size = 1;
            final Particle.DustOptions dustOptions = new Particle.DustOptions(
                    random.nextBoolean() ? Color.TEAL : Color.RED,
                    size
            );

            world.spawnParticle(
                    Particle.REDSTONE,
                    particleLocation,
                    count,
                    offsetX,
                    offsetY,
                    offsetZ,
                    dustOptions
            );
        }

        final Vector start = eyeLocation.toVector();
        final int yOffset = 0;

        final BlockIterator blockIterator = new BlockIterator(
                world,
                start,
                direction,
                yOffset,
                distance
        );

        while (blockIterator.hasNext()) {
            blockIterator.next().breakNaturally();
        }

        final float volume = 0.8F;
        final float pitch = 63.0F;

        world.playSound(
                eyeLocation,
                Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR,
                volume,
                pitch
        );

        final float volume2 = 1.0F;
        final float pitch2 = 20.0F;

        world.playSound(
                eyeLocation,
                Sound.ENTITY_FIREWORK_ROCKET_BLAST,
                volume2,
                pitch2
        );

        return true;
    }
}
