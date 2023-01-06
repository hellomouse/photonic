package net.hellomouse.photonic.registry.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class MaterialItemRegistry {
	public static final Item STEEL_INGOT = new Item(new QuiltItemSettings().group(ItemGroup.MISC).maxCount(64));

	public static void register(ModContainer mod) {
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "steel_ingot"), STEEL_INGOT);
	}
}
