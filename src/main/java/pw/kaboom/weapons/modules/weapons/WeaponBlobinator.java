package pw.kaboom.weapons.modules.weapons;

import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import pw.kaboom.weapons.Main;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class WeaponBlobinator implements Weapon, Listener {
    private static final int RADIUS = 4;

    @Override
    public String getKey() {
        return "blobinator";
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack weapon = ItemStack.of(Material.MAGMA_CREAM);
        weapon.editMeta(meta -> {
            meta.displayName(Component.text("Blobinator"));
            meta.getPersistentDataContainer().set(
                    Main.getPdcKey(),
                    PersistentDataType.LIST.strings(),
                    List.of(getKey())
            );
        });
        return weapon;
    }

    @Override
    public boolean leftClick(final Player player) {
        final Location eyeLocation = player.getEyeLocation();
        final Vector velocity = eyeLocation.getDirection().multiply(8);

        final Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.customName(Component.text("WeaponBlobinatorBall"));
        snowball.setShooter(player);
        snowball.setVelocity(velocity);

        final World world = player.getWorld();
        final float volume = 1.0F;
        final float pitch = 0.8F;

        world.playSound(eyeLocation, Sound.ITEM_BOTTLE_EMPTY, volume, pitch);
        return true;
    }

    private void createBlobSplash(final World world, final int x, final int y, final int z,
                                  final Block hitBlock, final Material color) {
        final Location blockLocation = hitBlock.getLocation().add(x, y, z);

        if (blockLocation.distance(hitBlock.getLocation()) <= RADIUS) {
            final Block block = world.getBlockAt(blockLocation);

            if (block.getType() != Material.AIR
                    && block.getType() != Material.CAVE_AIR) {
                for (final BlockFace face : Main.getBlockFaces()) {
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
        if (event.getEntityType() != EntityType.SNOWBALL) {
            return;
        }

        final Block hitBlock = event.getHitBlock();
        final Projectile projectile = event.getEntity();

        if (!Component.text("WeaponBlobinatorBall").equals(projectile.customName())) {
            return;
        }

        if (hitBlock != null) {
            final World world = projectile.getWorld();
            final Material color = Main.getColors().get(
                    ThreadLocalRandom.current().nextInt(Main.getColors().size()));

            for (int x = -RADIUS; x < RADIUS; x++) {
                for (int y = -RADIUS; y < RADIUS; y++) {
                    for (int z = -RADIUS; z < RADIUS; z++) {
                        createBlobSplash(world, x, y, z, hitBlock, color);
                    }
                }
            }
        }

        event.setCancelled(true);
    }
}
