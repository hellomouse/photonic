package net.hellomouse.photonic.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Util {
	/**
	 * Check if a player can break a block with an item
	 * @param item Tool to break
	 * @param world World
	 * @param state Block state of block
	 * @param pos Position of block
	 * @param player Player
	 * @return Can player break the block here?
	 */
	public static boolean canBreakBlock(Item item, World world, BlockState state, BlockPos pos, PlayerEntity player) {
		return player.canModifyAt(world, pos) && item.canMine(state, world, pos, player);
	}
}
