package net.hellomouse.photonic.block;

import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.block.entity.ArcFurnaceEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

// Machine blocks
public class MachineBlockRegistry {
	public static final Block ARC_FURNACE = new ArcFurnace(QuiltBlockSettings.of(Material.METAL).strength(8.0f));
	public static BlockEntityType<ArcFurnaceEntity> ARC_FURNACE_ENTITY =
			Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Photonic.MOD_ID, "arc_furnace_entity"), QuiltBlockEntityTypeBuilder.create(ArcFurnaceEntity::new, ARC_FURNACE).build());
	public static final Block HAND_GENERATOR = new Block(QuiltBlockSettings.of(Material.METAL).strength(4.0f));


	public static void register(ModContainer mod) {
		registerBlocks(mod);
		registerBlockEntities(mod);

	}

	public static void registerBlocks(ModContainer mod) {
		//Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(mod.metadata().id(), "arc_furnace_entity"), ARC_FURNACE_ENTITY);
		Registry.register(Registry.BLOCK, new Identifier(mod.metadata().id(), "arc_furnace"), ARC_FURNACE);
		Registry.register(Registry.BLOCK, new Identifier(mod.metadata().id(), "hand_generator"), HAND_GENERATOR);
	}

	public static void registerBlockEntities(ModContainer mod) {

	}

}
