package pw.kaboom.weapons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import org.bukkit.material.Wool;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.util.Vector;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;

class Events implements Listener {
	Main main;
	Events(Main main) {
		this.main = main;
	}

	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		if ("Weapons".equals(event.getView().getTitle())) {
			final ItemStack item = event.getCurrentItem();

			if (item.getItemMeta().hasDisplayName() == true) {
				final Player player = (Player) event.getWhoClicked();

				player.getInventory().addItem(item);
				player.closeInventory();
				player.sendMessage("You have received the " + item.getItemMeta().getDisplayName().toLowerCase() + "!");
			}
		}
	}

	@EventHandler
	void onPlayerEggThrow(PlayerEggThrowEvent event) {
		final PlayerInventory inventory = event.getPlayer().getInventory();
		final String name = inventory.getItemInMainHand().getItemMeta().getDisplayName();

		if ("§rGrenade".equals(name)) {
			final Egg egg = event.getEgg();
			egg.setCustomName("weaponsGrenade");
			event.setHatching(false);
		}
	}

	@EventHandler
	void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasItem() == true &&
			event.getItem().getItemMeta().hasDisplayName() == true) {
			final Action action = event.getAction();
			final Material item = event.getMaterial();
			final String name = event.getItem().getItemMeta().getDisplayName();

			if (action == Action.LEFT_CLICK_AIR ||
				action == Action.LEFT_CLICK_BLOCK) {
				if (item == Material.ANVIL &&
					"§rAnvil Dropper".equals(name)) {
					for (int x = -2; x <= 2; x++) {
						for (int z = -2; z <= 2; z++) {
							final Location blockLocation = event.getPlayer().getLocation().subtract(x, 0, z);
							final World world = event.getPlayer().getWorld();

							world.spawnFallingBlock(
								blockLocation,
								Material.ANVIL.createBlockData()
							);
						}
					}

					event.setCancelled(true);
				} else if (item == Material.SPECTRAL_ARROW &&
					"§rArcher".equals(name)) {
					final World world = event.getPlayer().getWorld();

					for (int i = 0; i <= 20; i++) {
						final double randomX = (Math.random() * ((15 + 15) + 1)) - 15;
						final double randomY = (Math.random() * ((15 + 15) + 1)) - 15;
						final double randomZ = (Math.random() * ((15 + 15) + 1)) - 15;

						final Player player = event.getPlayer();
						final Vector randomDirection = new Vector(
							randomX,
							randomY,
							randomZ).normalize().multiply(8);

						final SpectralArrow arrow = (SpectralArrow) world.spawnEntity(
							player.getLocation(),
							EntityType.SPECTRAL_ARROW
						);
						arrow.setCustomName("weaponsCancelCollision");
						arrow.setShooter(player);
						arrow.setVelocity(randomDirection);
					}

					final Location eyeLocation = event.getPlayer().getEyeLocation();
					final float volume = 1.0F;
					final float pitch = 1.5F;

					world.playSound(
						eyeLocation,
						Sound.BLOCK_ANVIL_DESTROY,
						volume,
						pitch
					);
					event.setCancelled(true);
				} else if (item == Material.FIRE_CHARGE &&
					"§rArmageddon".equals(name)) {
					final World world = event.getPlayer().getWorld();

					for (int i = -12; i <= 12; i += 4) {
						final Player player = event.getPlayer();
						final Location eyeLocation = player.getEyeLocation();

						final double x = (i * Math.cos(Math.toRadians(eyeLocation.getYaw())));
						final double z = (i * Math.sin(Math.toRadians(eyeLocation.getYaw())));

						final Vector velocity = eyeLocation.getDirection().multiply(12);

						final Fireball fireball = (Fireball) world.spawnEntity(
							eyeLocation.add(x, 0, z),
							EntityType.FIREBALL
						);

						fireball.setBounce(false);
						fireball.setCustomName("weaponsArmegaddonBall");
						fireball.setDirection(velocity);
						fireball.setShooter(player);
						fireball.setYield(5);
					}

					final Location eyeLocation = event.getPlayer().getEyeLocation();
					final float volume = 0.9F;
					final float pitch = 1.5F;

					world.playSound(
						eyeLocation,
						Sound.ENTITY_GHAST_SHOOT,
						volume,
						pitch
					);
					event.setCancelled(true);
				} else if (item == Material.MAGMA_CREAM &&
					"§rBlobinator".equals(name)) {
					final Player player = event.getPlayer();
					final Location eyeLocation = player.getEyeLocation();
					final Vector velocity = eyeLocation.getDirection().multiply(8);

					final Snowball snowball = player.launchProjectile(Snowball.class);
					snowball.setCustomName("weaponsBlobinatorBall");
					snowball.setShooter(player);
					snowball.setVelocity(velocity);

					final World world = event.getPlayer().getWorld();
					final float volume = 1.0F;
					final float pitch = 0.8F;

					world.playSound(
						eyeLocation,
						Sound.ITEM_BOTTLE_EMPTY,
						volume,
						pitch
					);
					event.setCancelled(true);
				} else if (item == Material.STICK &&
					"§rLightning Stick".equals(name)) {
					final Player player = event.getPlayer();
					final Location lookLocation = player.getTargetBlock((Set<Material>) null, 100).getLocation();
					final World world = player.getWorld();

					world.strikeLightning(lookLocation);

					event.setCancelled(true);
				} else if (item == Material.BLAZE_ROD &&
					"§rNuker".equals(name)) {
					final Player player = event.getPlayer();
					final Location eyeLocation = player.getEyeLocation();
					final Vector velocity = eyeLocation.getDirection().multiply(10);

					final Fireball fireball = player.launchProjectile(Fireball.class);
					fireball.setCustomName("weaponsCancelCollision");
					fireball.setShooter(player);
					fireball.setVelocity(velocity);
					fireball.setYield(8);

					final World world = event.getPlayer().getWorld();
					final float volume1 = 0.9F;
					final float pitch1 = 1.5F;
					final float volume2 = 0.8F;
					final float pitch2 = 2.0F;

					world.playSound(
						eyeLocation,
						Sound.ENTITY_GHAST_SHOOT,
						volume1,
						pitch1
					);
					world.playSound(
						eyeLocation,
						Sound.ENTITY_BAT_TAKEOFF,
						volume2,
						pitch2
					);
					event.setCancelled(true);
				} else if (item == Material.IRON_HORSE_ARMOR &&
					"§rSniper".equals(name)) {
					final Player player = event.getPlayer();
					final Location eyeLocation = player.getEyeLocation();
					final Vector velocity = eyeLocation.getDirection().multiply(12);

					final Snowball snowball = player.launchProjectile(Snowball.class);
					snowball.setCustomName("weaponsCancelCollision");
					snowball.setShooter(player);
					snowball.setVelocity(velocity);

					final World world = event.getPlayer().getWorld();
					final float volume = 1.0F;
					final float pitch = 63.0F;

					world.playSound(
						eyeLocation,
						Sound.BLOCK_PISTON_CONTRACT,
						volume,
						pitch
					);
					event.setCancelled(true);
				}
			} else if (action == Action.RIGHT_CLICK_AIR ||
				action == Action.RIGHT_CLICK_BLOCK) {
				if (item == Material.BLAZE_POWDER &&
					"§rLaser".equals(name)) {
					final Player player = event.getPlayer();
					final Location eyeLocation = player.getEyeLocation();
					final Vector direction = eyeLocation.getDirection();
					final Location lookLocation = player.getTargetBlock(
						(Set<Material>) null,
						100).getLocation();
					double distance = eyeLocation.distance(lookLocation);
					final Location particleLocation = player.getEyeLocation();
					final World world = player.getWorld();

					if (lookLocation == null) {
						distance = 50;
					}

					for (double i = 0; i <= distance; i++) {
						particleLocation.add(direction);

						final int count = 1;
						final double offsetX = 0;
						final double offsetY = 0;
						final double offsetZ = 0;
						final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);

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

					world.getBlockAt(lookLocation).breakNaturally();

					final float volume1 = 0.8F;
					final float pitch1 = 63.0F;
					final float volume2 = 1.0F;
					final float pitch2 = 20.0F;

					world.playSound(
						eyeLocation,
						Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR,
						volume1,
						pitch1
					);
					world.playSound(
						eyeLocation,
						Sound.ENTITY_FIREWORK_ROCKET_BLAST,
						volume2,
						pitch2
					);
					event.setCancelled(true);
				} else if (item == Material.GOLDEN_HORSE_ARMOR &&
					"§rMachine Gun".equals(name)) {
					final UUID playerUUID = event.getPlayer().getUniqueId();

					if (!main.machineGunActive.contains(playerUUID)) {
						main.machineGunActive.add(playerUUID);
						final PlayerInteractEvent eventMachine = event;

						new BukkitRunnable() {
							int i = 0;
							public void run() {
								i++;

								final Player player = eventMachine.getPlayer();
								final Location eyeLocation = player.getEyeLocation();
								final World world = player.getWorld();
								final Vector velocity = eyeLocation.getDirection().multiply(12);

								final TippedArrow arrow = player.launchProjectile(TippedArrow.class);

								final int duration = 90000;
								final int amplifier = 3;
								final boolean ambient = true;
								final boolean particles = false;

								final PotionEffect harm = new PotionEffect(
									PotionEffectType.HARM,
									duration,
									amplifier,
									ambient,
									particles
								);

								arrow.setCustomName("weaponsProjectileBlock");
								arrow.addCustomEffect(harm, true);
								arrow.setShooter(player);
								arrow.setVelocity(velocity);

								final float volume = 1.0F;
								final float pitch = 63.0F;

								world.playSound(
									eyeLocation,
									Sound.ENTITY_GENERIC_EXPLODE,
									volume,
									pitch
								);

								if (i == 20) {
									main.machineGunActive.remove(playerUUID);
									cancel();
								}
							}
						}.runTaskTimer(main, 0L, 1L);
					}

					event.setCancelled(true);
				} else if (item == Material.IRON_HORSE_ARMOR &&
					"§rSniper".equals(name)) {
					final Player player = event.getPlayer();

					if (player.hasPotionEffect(PotionEffectType.SLOW)) {
						player.removePotionEffect(PotionEffectType.SLOW);
					} else {
						final int duration = 90000;
						final int amplifier = 7;

						final PotionEffect effect = new PotionEffect(
							PotionEffectType.SLOW,
							duration,
							amplifier
						);
						player.addPotionEffect(effect);
					}

					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	void onProjectileCollide(ProjectileCollideEvent event) {
		if (event.getEntityType() == EntityType.FIREBALL) {
			final Projectile projectile = event.getEntity();

			if ("weaponsArmegaddonBall".equals(projectile.getCustomName())) {
				final Entity collidedWith = event.getCollidedWith();

				if (collidedWith.getType() == EntityType.PLAYER &&
					projectile.getShooter() instanceof Player &&
					((Player) projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) {
					event.setCancelled(true);
				} else if (collidedWith.getType() == EntityType.FIREBALL) {
					event.setCancelled(true);
				}
			}
		} else if (event.getEntityType() == EntityType.SNOWBALL) {
			final Projectile projectile = event.getEntity();

			if ("weaponsBlobinatorBall".equals(projectile.getCustomName())) {
				event.setCancelled(true);
			}
		} else if (event.getEntityType() == EntityType.SPECTRAL_ARROW) {
			final Projectile projectile = event.getEntity();

			if ("weaponsProjectileBlock".equals(projectile.getCustomName())) {
				final Entity collidedWith = event.getCollidedWith();

				if (collidedWith.getType() == EntityType.PLAYER &&
					projectile.getShooter() instanceof Player &&
					((Player) projectile.getShooter()).getUniqueId().equals(collidedWith.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.EGG) {
			final Projectile projectile = event.getEntity();

			if ("weaponsGrenade".equals(projectile.getCustomName())) {
				final Location location = projectile.getLocation();
				final World world = location.getWorld();
				final float power = 6;

				world.createExplosion(location, power);
			}
		} else if (event.getEntityType() == EntityType.TIPPED_ARROW) {
			final Projectile projectile = event.getEntity();

			if ("weaponsProjectileBlock".equals(projectile.getCustomName())) {
				projectile.remove();
			}
		} else if (event.getEntityType() == EntityType.SNOWBALL) {
			final Block hitBlock = event.getHitBlock();
			final Projectile projectile = event.getEntity();

			if (hitBlock != null &&
				"weaponsBlobinatorBall".equals(projectile.getCustomName())) {
				final int radius = 4;
				final World world = projectile.getWorld();

				final DyeColor[] colors = DyeColor.values();
				final Random random = new Random();
				final DyeColor color = colors[random.nextInt(colors.length)];

				final HashSet<BlockFace> faces = new HashSet<BlockFace>(Arrays.asList(new BlockFace[] {
					BlockFace.NORTH,
					BlockFace.SOUTH,
					BlockFace.WEST,
					BlockFace.EAST,
					BlockFace.UP,
					BlockFace.DOWN,
				}));

				for (int x = -radius; x < radius; x++) {
					for (int y = -radius; y < radius; y++) {
						for (int z = -radius; z < radius; z++) {
							final Location blockLocation = hitBlock.getLocation().add(x, y, z);

							if (blockLocation.distance(hitBlock.getLocation()) <= radius) {
								final Block block = world.getBlockAt(blockLocation);

								if (block.getType() != Material.AIR) {
									for (BlockFace face : faces) {
										if (block.getRelative(face).getType() == Material.AIR) {
											block.setType(Material.LEGACY_WOOL);

											final BlockState state = block.getState();
											final Wool wool = (Wool) state.getData();
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
