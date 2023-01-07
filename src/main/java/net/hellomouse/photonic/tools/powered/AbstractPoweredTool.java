package net.hellomouse.photonic.tools.powered;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyItem;

// TODO: mining speed modifier doesnt take into account nbt?
public abstract class AbstractPoweredTool extends Item implements SimpleEnergyItem {
	protected final long capacity;
	protected final PoweredToolConfig config;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public AbstractPoweredTool(Item.Settings settings, PoweredToolConfig config) {
		super(settings);
		this.config = config;
		this.capacity = config.getCapacity();

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier",
						config.getDamageUnpowered(), EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier",
						config.getAttackSpeed(), EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	/** @return Attack damage, can change based on powered state */
	public abstract float getAttackDamage(ItemStack stack);

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return this.getStoredEnergy(stack) > 0 ? this.config.getMiningSpeedPowered() : this.config.getMiningSpeedUnpowered();
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// this.tryUseEnergy(ItemStack stack, long amount);
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public boolean onClickedOnOther(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player) {
		// Todo transfer e?
		return false;
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		// TODO: transfer e?
		return false;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		float damage = this.getAttackDamage(stack);
		if (damage > config.getDamageUnpowered() && this.tryUseEnergy(stack, this.config.getEnergyCostToHitMob()))
			target.damage(DamageSource.mob(attacker), damage - config.getDamageUnpowered());
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		return this.tryUseEnergy(stack, this.config.getEnergyCostToMine());
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
	}

	// Powered tools can't be easily enchanted without mixining EnchantmentTarget
	@Override
	public int getEnchantability() { return 0; }

	// Display power on durability bar
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.round((float)this.getStoredEnergy(stack) * 13.0F / (float)this.capacity);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		float f = Math.max(0.0F, ((float)this.getStoredEnergy(stack)) / (float)this.capacity);
		return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	// Get capacity and transfer limits
	@Override
	public long getEnergyCapacity(ItemStack stack) { return capacity; }

	@Override
	public long getEnergyMaxInput(ItemStack stack) { return config.getMaxInputRate(); }

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {return config.getMaxOutputRate(); }

	// Attribute modifiers
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}
