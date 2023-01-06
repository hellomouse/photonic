package net.hellomouse.photonic.registry.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.particle.FlamethrowerFlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;

public class ParticleRegistry {
	public static final DefaultParticleType FLAMETHROWER_FLAME = FabricParticleTypes.simple();

	public static void register(ModContainer mod) {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Photonic.MOD_ID, "flamethrower_flame"), FLAMETHROWER_FLAME);
	}

	public static void registerClient(ModContainer mod) {
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
			registry.register(new Identifier(Photonic.MOD_ID, "particle/flamethrower_flame"));
		}));

		ParticleFactoryRegistry.getInstance().register(FLAMETHROWER_FLAME, FlamethrowerFlameParticle.Factory::new);
	}
}
