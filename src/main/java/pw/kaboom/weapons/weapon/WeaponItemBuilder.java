package pw.kaboom.weapons.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponItemBuilder {
	private final ItemStack itemStack;
	private final ItemMeta itemMeta;

	private WeaponItemBuilder(final ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemMeta = this.itemStack.getItemMeta();
	}

	public WeaponItemBuilder enchantment(final Enchantment enchantment) {
		return enchantment(enchantment, enchantment.getStartLevel());
	}

	public WeaponItemBuilder enchantment(final Enchantment enchantment, final int level) {
		this.itemMeta.addEnchant(enchantment, level, true);

		return this;
	}

	public WeaponItemBuilder name(final String name) {
		final Component component = Component.text(name)
			.decoration(TextDecoration.ITALIC, false);
		this.itemMeta.displayName(component);

		return this;
	}

	public static WeaponItemBuilder builder(final ItemStack itemStack) {
		return new WeaponItemBuilder(itemStack);
	}

	public static WeaponItemBuilder builder(final Material material) {
		return new WeaponItemBuilder(new ItemStack(material));
	}

	public ItemStack build() {
		this.itemStack.setItemMeta(this.itemMeta);

		return this.itemStack;
	}
}
