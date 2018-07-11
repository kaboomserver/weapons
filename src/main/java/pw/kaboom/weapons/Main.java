package pw.kaboom.weapons;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.material.MaterialData;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		this.getCommand("weapons").setExecutor(new CommandWeapons());
		this.getServer().getPluginManager().registerEvents(this, this);
	}
 
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clickedItem = event.getCurrentItem();
		String name = clickedItem.getItemMeta().getDisplayName().toLowerCase();

		if (event.getInventory().getName() == "Weapons") {
			if (clickedItem != null) {
				player.getInventory().addItem(clickedItem);
				player.closeInventory();
				player.sendMessage("You have received the " + name + "!");
			}
		}
	}

	@EventHandler
	void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		boolean hasName = event.getItem().getItemMeta().hasDisplayName();
		Material item = event.getMaterial();
		String name = event.getItem().getItemMeta().getDisplayName();
		Location lookPos = player.getTargetBlock((Set<Material>) null, 100).getLocation();
		Location playerPos = player.getLocation();
		World world = player.getLocation().getWorld();

		if (hasName == true) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				if (item == Material.ANVIL && name == "Anvil Dropper") {
					for (int x = -2; x <= 2; x += 1) {
						for (int z = -2; z <= 2; z += 1) {
							Location blockPos = new Location(world, playerPos.getX() - x, playerPos.getY(), playerPos.getZ() - z);
							MaterialData blockMaterial = new MaterialData(Material.ANVIL);
							world.spawnFallingBlock(blockPos, blockMaterial);
						}
					}
					event.setCancelled(true);
				} else if (item == Material.STICK && name == "Lightning Stick") {
					world.strikeLightning(lookPos);
					event.setCancelled(true);
				} else if (item == Material.BLAZE_ROD && name == "Nuker") {
					Projectile projectile = (Projectile) world.spawnEntity(playerPos, EntityType.FIREBALL);
					projectile.setShooter(player);
					projectile.setVelocity(playerPos.getDirection().multiply(6));
					world.createExplosion(lookPos, 6);
					world.playSound(playerPos, Sound.ENTITY_GHAST_SHOOT, 0.9F, 1.5F);
					world.playSound(playerPos, Sound.ENTITY_BAT_TAKEOFF, 0.8F, 2.0F);
					event.setCancelled(true);
				} else if (item == Material.IRON_BARDING && name == "Sniper") {
					Location projectilePos = new Location(world, playerPos.getX(), playerPos.getY() + 1.5, playerPos.getZ());
					Projectile projectile = (Projectile) world.spawnEntity(projectilePos, EntityType.SNOWBALL);
					projectile.setShooter(player);
					projectile.setVelocity(playerPos.getDirection().multiply(6));
					world.playSound(playerPos, Sound.BLOCK_PISTON_CONTRACT, 1.0F, 63.0F);
					event.setCancelled(true);
				}
			} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				if (item == Material.IRON_BARDING && name == "Sniper") {
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
}

class CommandWeapons implements CommandExecutor {
	private void addWeapon(Inventory inventory, Material material, String name) {
		ItemStack item = new ItemStack​(material, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		inventory.addItem(item);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		Inventory inventory = Bukkit.createInventory(null, 9, "Weapons");
		addWeapon(inventory, Material.ANVIL, "Anvil Dropper");
		addWeapon(inventory, Material.STICK, "Lightning Stick");
		addWeapon(inventory, Material.BLAZE_ROD, "Nuker");
		addWeapon(inventory, Material.IRON_BARDING, "Sniper");
		player.openInventory(inventory);
		return true;
	}
}
