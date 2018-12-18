package pw.kaboom.weapons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.util.Vector;

class Events implements Listener {
	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (event.getInventory().getName().equals("Weapons")) {
			if (event.getCurrentItem().getItemMeta().hasDisplayName() == true) {
				player.getInventory().addItem(event.getCurrentItem());
				player.closeInventory();
				player.sendMessage("You have received the " + event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase() + "!");
			}
		}
	}

	@EventHandler
	void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getItem() != null && event.getItem().getItemMeta().hasDisplayName() == true) {
			Action action = event.getAction();
			Player player = event.getPlayer();
			Material item = event.getMaterial();
			String name = event.getItem().getItemMeta().getDisplayName();

			World world = player.getLocation().getWorld();
			Location eyePos = player.getEyeLocation();
			Location lookPos = player.getTargetBlock((Set<Material>) null, 100).getLocation();
			Location playerPos = player.getLocation();

			Vector direction = eyePos.getDirection();
			Location frontPos = playerPos.add(direction);

			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				if (item == Material.ANVIL && name.equals("§rAnvil Dropper")) {
					for (int x = -2; x <= 2; x++) {
						for (int z = -2; z <= 2; z++) {
							Location blockPos = new Location(world, playerPos.getX() - x, playerPos.getY(), playerPos.getZ() - z);
							MaterialData blockMaterial = new MaterialData(Material.ANVIL);
							world.spawnFallingBlock(blockPos, blockMaterial);
						}
					}

					event.setCancelled(true);
				} else if (item == Material.SPECTRAL_ARROW && name.equals("§rArcher")) {
					world.playSound(playerPos, Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.5F);

					for (int i = 0; i <= 20; i++) {
						double randomX = (Math.random() * ((15 + 15) + 1)) - 15;
						double randomY = (Math.random() * ((15 + 15) + 1)) - 15;
						double randomZ = (Math.random() * ((15 + 15) + 1)) - 15;

						Vector randomDirection = new Vector(randomX, randomY, randomZ).normalize().multiply(8);

						SpectralArrow arrow = (SpectralArrow) world.spawnEntity(frontPos, EntityType.SPECTRAL_ARROW);
						arrow.setCustomName("weaponsSpectralArrow");
						arrow.setShooter(player);
						arrow.setVelocity(randomDirection);
					}

					event.setCancelled(true);
				} else if (item == Material.FIREBALL && name.equals("§rArmageddon")) {
					/*for (int i = 0; i <= 30; i++) {
						double randomX = (Math.random() * ((15 + 15) + 1)) - 15;
						double randomY = (Math.random() * ((15 + 15) + 1)) - 15;
						double randomZ = (Math.random() * ((15 + 15) + 1)) - 15;

						Vector direction = new Vector(randomX, randomY, randomZ).normalize().multiply(6);

						Fireball arrow = player.launchProjectile(Fireball.class);
						arrow.setCustomName(player.getName() + "fireballshooter");
						arrow.setDirection(direction);
					}*/
					for (int i = -12; i <= 12; i += 4) {
						double x = frontPos.getX() + (i * Math.cos(Math.toRadians(frontPos.getYaw())));
						double z = frontPos.getZ() + (i * Math.sin(Math.toRadians(frontPos.getYaw())));

						Fireball fireball = (Fireball) world.spawnEntity(
							new Location(world, x, frontPos.getY(), z),
							EntityType.FIREBALL
						);

						fireball.setBounce(false);
						fireball.setCustomName("weaponsArmegaddonBall");
						fireball.setDirection(direction.multiply(12));
						fireball.setShooter(player);
						fireball.setYield(5);
					}

					world.playSound(eyePos, Sound.ENTITY_GHAST_SHOOT, 0.9F, 1.5F);

					event.setCancelled(true);
				} else if (item == Material.MAGMA_CREAM && name.equals("§rBlobinator")) {
					Snowball snowball = (Snowball) world.spawnEntity(frontPos, EntityType.SNOWBALL);
					snowball.setCustomName("weaponsBlobinatorBall");
					snowball.setShooter(player);
					snowball.setVelocity(direction.multiply(8));

					world.playSound(eyePos, Sound.ITEM_BOTTLE_EMPTY, 1.0F, 0.8F);

					event.setCancelled(true);
				} else if (item == Material.STICK && name.equals("§rLightning Stick")) {
					world.strikeLightning(lookPos);

					event.setCancelled(true);
				} else if (item == Material.BLAZE_ROD && name.equals("§rNuker")) {
					Fireball fireball = (Fireball) world.spawnEntity(frontPos, EntityType.FIREBALL);
					fireball.setVelocity(direction.multiply(10));
					fireball.setYield(8);
					/*world.createExplosion(lookPos, 6);*/

					world.playSound(eyePos, Sound.ENTITY_GHAST_SHOOT, 0.9F, 1.5F);
					world.playSound(eyePos, Sound.ENTITY_BAT_TAKEOFF, 0.8F, 2.0F);

					event.setCancelled(true);
				} else if (item == Material.IRON_BARDING && name.equals("§rSniper")) {
					Snowball snowball = (Snowball) world.spawnEntity(frontPos, EntityType.SNOWBALL);
					snowball.setShooter(player);
					snowball.setVelocity(direction.multiply(6));

					world.playSound(eyePos, Sound.BLOCK_PISTON_CONTRACT, 1.0F, 63.0F);

					event.setCancelled(true);
				}
			} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				if (item == Material.BLAZE_POWDER && name.equals("§rLaser")) {
					double distance = eyePos.distance(lookPos);

					if (lookPos == null) {
						distance = 50;
					}

					world.playSound(eyePos, Sound.ENTITY_FIREWORK_BLAST_FAR, 0.8F, 40.0F);
					world.playSound(eyePos, Sound.ENTITY_FIREWORK_BLAST, 1.0F, 20.0F);

					for (double i = 0; i <= distance; i++) {
						frontPos.add(direction);
						world.spawnParticle(Particle.REDSTONE, frontPos, 0, 1, 0, 0);
					}

					world.getBlockAt(lookPos).breakNaturally();

					event.setCancelled(true);
				} else if (item == Material.IRON_BARDING && name.equals("§rSniper")) {
					if (player.hasPotionEffect(PotionEffectType.SLOW)) {
						player.removePotionEffect(PotionEffectType.SLOW);
					} else {
						PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 90000, 7);
						player.addPotionEffect(effect);
					}

					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	void onProjectileCollide(ProjectileCollideEvent event) {
		Projectile projectile = event.getEntity();
		Entity collidedWith = event.getCollidedWith();

		if (projectile.getCustomName() != null) {
			if (projectile.getType() == EntityType.FIREBALL &&
			projectile.getCustomName().equals("weaponsArmegaddonBall")) {
				if (collidedWith.getType() == EntityType.PLAYER &&
				projectile.getShooter() instanceof Player &&
				((Player)projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) {
					event.setCancelled(true);
				} else if (collidedWith.getType() == EntityType.FIREBALL) {
					event.setCancelled(true);
				}
			} else if (projectile.getType() == EntityType.SNOWBALL &&
			projectile.getCustomName().equals("weaponsBlobinatorBall")) {
				event.setCancelled(true);
			} else if (projectile.getType() == EntityType.SPECTRAL_ARROW &&
			projectile.getCustomName().equals("weaponsSpectralArrow")) {
				if (collidedWith.getType() == EntityType.PLAYER &&
				projectile.getShooter() instanceof Player &&
				((Player)projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Block hitBlock = event.getHitBlock();

		if (projectile.getType() == EntityType.SNOWBALL &&
		hitBlock != null &&
		projectile.getShooter() != null) {
			if (projectile.getCustomName() != null && projectile.getCustomName().equals("weaponsBlobinatorBall")) {
				int radius = 4;
				World world = projectile.getWorld();

				DyeColor[] colors = DyeColor.values();
				Random random = new Random();
				DyeColor color = colors[random.nextInt(colors.length)];

				HashSet<BlockFace> faces = new HashSet<BlockFace>(Arrays.asList(new BlockFace[] {
					BlockFace.NORTH,
					BlockFace.SOUTH,
					BlockFace.WEST,
					BlockFace.EAST,
					BlockFace.UP,
					BlockFace.DOWN
				}));

				for (int x = -radius; x < radius; x++) {
					for (int y = -radius; y < radius; y++) {
						for (int z = -radius; z < radius; z++) {
							Location blockLocation = new Location(world, hitBlock.getX() + x, hitBlock.getY() + y, hitBlock.getZ() + z);

							if (blockLocation.distance(hitBlock.getLocation()) <= radius) {
								Block block = world.getBlockAt(blockLocation);

								if (block.getType() != Material.AIR) {
									for (BlockFace face : faces) {
										if (block.getRelative(face).getType() == Material.AIR) {
											block.setType(Material.WOOL);

											BlockState state = block.getState();
											Wool wool = (Wool) state.getData();
											wool.setColor(color);
											state.setData(wool);
											state.update();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
