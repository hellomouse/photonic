package net.hellomouse.photonic;

import net.hellomouse.photonic.registry.item.ItemRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Photonic implements ModInitializer {
	// Must be all lowercase alphanumeric
	public static final String MOD_ID = "photonic";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		ItemRegistry.register(mod);
	}
}
