package pw.kaboom.weapons.modules.weapons;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import pw.kaboom.weapons.Main;

public final class WeaponBlobinator implements Listener {
    public static void leftClick(final Material item, final Component name,
                                 final PlayerInteractEvent event) {
        if (item == Material.MAGMA_CREAM && Component.text("Blobinator")
                .decoration(TextDecoration.ITALIC, false).equals(name)) {
            final Player player = event.getPlayer();
            final Location eyeLocation = player.getEyeLocation();
            final Vector velocity = eyeLocation.getDirection().multiply(8);

            final Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.customName(Component.text("WeaponBlobinatorBall"));
            snowball.setShooter(player);
            snowball.setVelocity(velocity);

            final World world = player.getWorld();
            final float volume = 1.0F;
            final float pitch = 0.8F;

            world.playSound(
                eyeLocation,
                Sound.ITEM_BOTTLE_EMPTY,
                volume,
                pitch
            );
            event.setCancelled(true);
        }
    }

    private void createBlobSplash(final World world, final int x, final int y, final int z,
                                  final int radius, final Block hitBlock, final Material color) {
        final Location blockLocation = hitBlock.getLocation().add(x, y, z);

        if (blockLocation.distance(hitBlock.getLocation()) <= radius) {
            final Block block = world.getBlockAt(blockLocation);

            if (block.getType() != Material.AIR
                    && block.getType() != Material.CAVE_AIR) {
                for (BlockFace face : Main.getBlockFaces()) {
                    if (block.getRelative(face).getType() == Material.AIR
                            || block.getRelative(face).getType() == Material.CAVE_AIR) {
                        block.setType(color);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL) {
            final Block hitBlock = event.getHitBlock();
            final Projectile projectile = event.getEntity();

            if (hitBlock != null
                    && Component.text("WeaponBlobinatorBall").equals(projectile.customName())) {
                final int radius = 4;
                final World world = projectile.getWorld();
                final Material color = Main.getColors().get(
                    ThreadLocalRandom.current().nextInt(Main.getColors().size()));

                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            createBlobSplash(world, x, y, z, radius, hitBlock, color);
                        }
                    }
                }
                
                event.setCancelled(true);
            }
        }
    }
}
