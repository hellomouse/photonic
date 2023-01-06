package net.hellomouse.photonic.entity;

import net.hellomouse.photonic.registry.entity.ProjectileEntityRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlamethrowerProjectileEntity extends ProjectileEntity {
	private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(FlamethrowerProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);;

	public FlamethrowerProjectileEntity(EntityType<? extends FlamethrowerProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public FlamethrowerProjectileEntity(double x, double y, double z, double vx, double vy, double vz, World world) {
		this(ProjectileEntityRegistry.FLAMETHROWER_PROJECTILE, world);
		this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
		this.setVelocity(vx, vy, vz);
		this.refreshPosition();
	}

	public FlamethrowerProjectileEntity(LivingEntity owner, double directionX, double directionY, double directionZ, World world) {
		this(owner.getX(), owner.getEyeY() - 0.4, owner.getZ(), directionX, directionY, directionZ, world);
		this.setOwner(owner);
		this.setRotation(owner.getYaw(), owner.getPitch());
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(ITEM, ItemStack.EMPTY);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		float f = (float)this.getVelocity().length();
		int i = MathHelper.ceil(MathHelper.clamp((double)f * 4.0f, 0.0, 2.147483647E9));

		Entity entity = entityHitResult.getEntity();
		Entity owner = this.getOwner();
		DamageSource damageSource = owner instanceof LivingEntity ? DamageSource.mob((LivingEntity)owner) : DamageSource.IN_FIRE;
		entity.setOnFireFor(7);

		if (entity.damage(damageSource, (float)i)) {
			if (entity instanceof LivingEntity livingEntity) {
				if (!this.world.isClient && owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity)owner, livingEntity);
				}

				if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent())
					((ServerPlayerEntity)owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
			}
		} else {
			entity.setFireTicks(7 * 20);
			this.setVelocity(this.getVelocity().multiply(-0.1));
			this.setYaw(this.getYaw() + 180.0F);
			this.prevYaw += 180.0F;
		}
	}

	// So it doesn't set itself on fire and look weird
	protected boolean isBurning() {
		return false;
	}

	@Override
	public void tick() {
		Entity owner = this.getOwner();
		if (this.world.isClient || (owner == null || !owner.isRemoved()) && this.world.isChunkLoaded(this.getBlockPos())) {
			super.tick();

			// Fall down
			if (!this.hasNoGravity()) {
				Vec3d vec3d4 = this.getVelocity();
				this.setVelocity(vec3d4.x, vec3d4.y - 0.05, vec3d4.z);
			}

			// Create fire particles in same direction
			Vec3d v = this.getVelocity().multiply(0.3);
			ParticleEffect particleEffect = ParticleTypes.LAVA;
			int choice = world.getRandom().nextInt(10);
			if (choice < 6)
				particleEffect = ParticleTypes.FLAME;
			else if (choice < 9)
				particleEffect = ParticleTypes.LARGE_SMOKE;

			world.addImportantParticle(particleEffect, this.getX(), this.getY(), this.getZ(), v.getX(), v.getY(), v.getZ());

			HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
			if (hitResult.getType() != HitResult.Type.MISS)
				this.onCollision(hitResult);

			this.checkBlockCollision();
			Vec3d vec3d = this.getVelocity();
			double x = this.getX() + vec3d.x;
			double y = this.getY() + vec3d.y;
			double z = this.getZ() + vec3d.z;
			ProjectileUtil.rotateTowardsMovement(this, 0.2F);

			// Make bubbles in water
			if (this.isTouchingWater()) {
				for(int i = 0; i < 4; ++i)
					this.world.addParticle(ParticleTypes.BUBBLE, x - vec3d.x * 0.25, y - vec3d.y * 0.25, z - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
			}

			this.world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
			this.setPosition(x, y, z);
		} else {
			this.discard();
		}
	}

	@Override
	public boolean doesRenderOnFire() { return false; }

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!this.isInvulnerableTo(source)) {
			this.scheduleVelocityUpdate();
			Entity owner = source.getAttacker();
			if (owner != null) {
				if (!this.world.isClient) {
					Vec3d vec3d = owner.getRotationVector();
					this.setVelocity(vec3d);
					this.setOwner(owner);
				}
				return true;
			}
		}
		return false;
	}
}

