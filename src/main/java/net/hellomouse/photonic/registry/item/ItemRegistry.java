package net.hellomouse.photonic.registry.item;

import net.hellomouse.photonic.Photonic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ItemRegistry {
	// Materials
	public static final Item STEEL_INGOT = new Item(new QuiltItemSettings().group(ItemGroup.MISC).maxCount(64));

	// Raw materials
	public static final Item CARBON = new Item(new QuiltItemSettings().group(ItemGroup.MISC).maxCount(64));
	public static final Item SILICON = new Item(new QuiltItemSettings().group(ItemGroup.MISC).maxCount(64));

	// Custom item group
	private static final ItemGroup ITEM_GROUP = QuiltItemGroup.builder(new Identifier(Photonic.MOD_ID, "item_group"))
			.icon(() -> new ItemStack(STEEL_INGOT))
			.appendItems(stacks -> {
				// Raw materials
				stacks.add(new ItemStack(CARBON));
				stacks.add(new ItemStack(SILICON));

				// Processed materials
				stacks.add(new ItemStack(STEEL_INGOT));
			})
			.build();

	public static void register(ModContainer mod) {
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "steel_ingot"), STEEL_INGOT);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "carbon"), CARBON);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "silicon"), SILICON);
	}
}
