package net.hellomouse.photonic;

import net.hellomouse.photonic.registry.item.ItemRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class PhotonicClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ItemRegistry.registerClient(mod);
	}
}
