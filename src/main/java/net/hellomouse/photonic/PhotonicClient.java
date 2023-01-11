package net.hellomouse.photonic;

import net.hellomouse.photonic.registry.item.ItemRegistry;
import net.hellomouse.photonic.registry.particle.ParticleRegistry;
import net.hellomouse.photonic.screen.ArcFurnaceScreen;
import net.hellomouse.photonic.screen.ScreenHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class PhotonicClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ItemRegistry.registerClient(mod);
		ParticleRegistry.registerClient(mod);

		HandledScreens.register(ScreenHandlerRegistry.ARC_FURNACE_SCREEN_HANDLER, ArcFurnaceScreen::new);
	}
}
