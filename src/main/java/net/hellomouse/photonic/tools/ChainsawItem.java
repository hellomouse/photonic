package net.hellomouse.photonic.tools;

import net.hellomouse.photonic.tools.powered.AbstractPoweredTool;
import net.hellomouse.photonic.tools.powered.PoweredToolConfig;
import net.hellomouse.photonic.util.Floodfill;
import net.hellomouse.photonic.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ChainsawItem extends AbstractPoweredTool {
	public ChainsawItem(Item.Settings settings) {
		super(settings, new PoweredToolConfig()
				.attackSpeed(-2.4)
				.attackDamage(3, 11)
				.energyCostToHit(10)
				.energyCostToMine(10)
				.miningSpeed(1, 8)
				.energyStorage(4000, 1000, 1000));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		// Prevent swinging if mining a block
		if (selected && entity instanceof LivingEntity) {
			if (entity.raycast(5.0f, 0.0f, false).getType() == HitResult.Type.BLOCK)
				((LivingEntity) entity).handSwinging = false;
		}
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (world.isClient) return false;
		if (!(miner instanceof PlayerEntity player)) return false;
		if (!player.canModifyBlocks() || player.isSneaking())
			return false;

		// TODO: limit horizontal distance? prevent accidentally deleting ur base lol

		// Perform floodfill and find connected logs
		ArrayList<BlockPos> logs = Floodfill.floodfill((ServerWorld)world, pos, Math.min(5000, this.getStoredEnergy(stack) / this.config.getEnergyCostToMine()), 200, true,
			pair -> {
				BlockPos p = pair.getRight();
				BlockState s = pair.getLeft();
				return Util.canBreakBlock(stack.getItem(), world, s, p, player) &&
						s.isIn(BlockTags.LOGS);
		});
		if (logs.size() == 0)
			return false; // No logs found

		// Break all the logs
		this.tryUseEnergy(stack, (long)logs.size() * this.config.getEnergyCostToMine());
		for (BlockPos p: logs)
			world.breakBlock(p, player.canHarvest(world.getBlockState(p)), miner);
		return true; // Return whether used
	}

	// temp hack for infinite energy
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// this.tryUseEnergy(ItemStack stack, long amount);
		this.setStoredEnergy(user.getStackInHand(hand), 4000);
		user.sendSystemMessage(Text.literal("Energy " + this.getStoredEnergy(user.getStackInHand(hand)) + "/" + this.capacity ));
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		// Slower for non-axe or leaves or cobwebs or bamboo
		boolean powered = this.getStoredEnergy(stack) > 0;
		if (state.isOf(Blocks.COBWEB) || state.isOf(Blocks.BAMBOO))
			return powered ? 64.0f : this.config.getMiningSpeedUnpowered(); // Faster since not proper tool
		if (!state.isIn(BlockTags.AXE_MINEABLE) && !state.isIn(BlockTags.LEAVES))
			return powered ? 1.0f : 0.5f;
		return powered ? this.config.getMiningSpeedPowered() : this.config.getMiningSpeedUnpowered();
	}

	@Override
	public boolean isSuitableFor(BlockState state) {
		return state.isIn(BlockTags.AXE_MINEABLE);
	}

	@Override
	public float getAttackDamage(ItemStack stack) {
		return this.getStoredEnergy(stack) > 0 ? 10 : 0;
	}
}
