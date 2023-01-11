package net.hellomouse.photonic.screen;

import net.hellomouse.photonic.Photonic;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class ScreenHandlerRegistry {
	public static ScreenHandlerType<ArcFurnaceScreenHandler> ARC_FURNACE_SCREEN_HANDLER = new ScreenHandlerType<>(ArcFurnaceScreenHandler::new);
}
