package net.hellomouse.photonic.registry.entity;

import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.entity.FlamethrowerProjectileEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class ProjectileEntityRegistry implements ModInitializer {
	public static final EntityType<FlamethrowerProjectileEntity> FLAMETHROWER_PROJECTILE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(Photonic.MOD_ID, "flamethrower_projectile"),
			QuiltEntityTypeBuilder.<FlamethrowerProjectileEntity>create(SpawnGroup.MISC, FlamethrowerProjectileEntity::new)
					.setDimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackingTickInterval(10).maxBlockTrackingRange(32)
					.build());

	@Override
	public void onInitialize(ModContainer mod) {

	}
}
