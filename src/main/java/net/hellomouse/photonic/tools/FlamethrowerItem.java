package net.hellomouse.photonic.tools;

import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.entity.FlamethrowerProjectileEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class FlamethrowerItem  extends Item {
	public FlamethrowerItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		tooltip.add(Text.translatable("item." + Photonic.MOD_ID + ".slime_finder.tooltip")
				.formatted(Formatting.GRAY));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		// TODO: spawn particles at end of thing
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		for (int i = 0; i < 5; i++) {
			ProjectileEntity persistentProjectileEntity = new FlamethrowerProjectileEntity(player, 0, 1, 0, world);
			persistentProjectileEntity.setOnFireFor(100);

			float offset1 = (world.getRandom().nextFloat() - 0.5f) * 10.0f;
			float offset2 = (world.getRandom().nextFloat() - 0.5f) * 10.0f;
			float speedOffset = (world.getRandom().nextFloat() - 0.5f) * 0.5f;

			// Modifier z is fire speed (1.7)
			persistentProjectileEntity.setProperties(player, player.getPitch() + offset1, player.getYaw() + offset2, 0.0F, 1.7F + speedOffset, 1.0F);
			world.spawnEntity(persistentProjectileEntity);
		}

		return TypedActionResult.pass(player.getStackInHand(hand));
	}
}
