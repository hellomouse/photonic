package net.hellomouse.photonic.registry.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hellomouse.photonic.entity.FlamethrowerProjectileEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ProjectileEntityRegistryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(ProjectileEntityRegistry.FLAMETHROWER_PROJECTILE, (context) ->
			new EntityRenderer<>(context) {
				@Override
				public Identifier getTexture(FlamethrowerProjectileEntity entity) {
					return null;
				}
			});
	}
}

