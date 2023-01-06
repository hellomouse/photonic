package net.hellomouse.photonic.registry.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

// Machine blocks
public class MachineBlockRegistry {
	public static final Block HAND_GENERATOR = new Block(QuiltBlockSettings.of(Material.METAL).strength(4.0f));

	public static void register(ModContainer mod) {
		Registry.register(Registry.BLOCK, new Identifier(mod.metadata().id(), "hand_generator"), HAND_GENERATOR);
	}
}
