package net.hellomouse.photonic.registry.item;

import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.registry.block.MachineBlockRegistry;
import net.hellomouse.photonic.tools.SlimeFinderItem;
import net.hellomouse.photonic.util.Networking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class ItemRegistry {
	// Materials
	public static final Item STEEL_INGOT = new Item(new QuiltItemSettings().maxCount(64));

	// Raw materials
	public static final Item CARBON = new Item(new QuiltItemSettings().maxCount(64));
	public static final Item SILICON = new Item(new QuiltItemSettings().maxCount(64));

	// Machine parts
	public static final Item MECHANICAL_CASING = new Item(new QuiltItemSettings().maxCount(64));

	// Machines
	public static final Item HAND_GENERATOR_ITEM = new BlockItem(MachineBlockRegistry.HAND_GENERATOR, new QuiltItemSettings().maxCount(64));

	// Tools
	public static final Item SLIME_FINDER = new SlimeFinderItem(new QuiltItemSettings().maxCount(1));
	public static final Item CHAINSAW = new Item(new QuiltItemSettings().maxCount(1));

	// Custom item group
	private static final ItemGroup ITEM_GROUP = QuiltItemGroup.builder(new Identifier(Photonic.MOD_ID, "item_group"))
			.icon(() -> new ItemStack(STEEL_INGOT))
			.appendItems(stacks -> {
				// Sort by these categories first, then
				// by progression

				// Tools
				stacks.add(new ItemStack(SLIME_FINDER));
				stacks.add(new ItemStack(CHAINSAW));

				// Raw materials
				stacks.add(new ItemStack(CARBON));
				stacks.add(new ItemStack(SILICON));

				// Processed materials
				stacks.add(new ItemStack(STEEL_INGOT));

				// Machine parts
				stacks.add(new ItemStack(MECHANICAL_CASING));

				// Machines
				stacks.add(new ItemStack(HAND_GENERATOR_ITEM));
			})
			.build();

	public static void register(ModContainer mod) {
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "steel_ingot"), STEEL_INGOT);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "carbon"), CARBON);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "silicon"), SILICON);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "mechanical_casing"), MECHANICAL_CASING);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "hand_generator"), HAND_GENERATOR_ITEM);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "slime_finder"), SLIME_FINDER);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "chainsaw"), CHAINSAW);
	}

	public static void registerClient(ModContainer mod) {
		ClientPlayNetworking.registerGlobalReceiver(Networking.SLIME_FINDER_PACKET_ID, (client, handler, buf, responseSender) -> {
			boolean isSlime = buf.readBoolean();
			client.execute(() -> {
				if (client.world != null) SlimeFinderItem.onClientWorld(client, isSlime);
			});
		});
	}
}
