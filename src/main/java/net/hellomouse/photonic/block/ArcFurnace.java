package net.hellomouse.photonic.block;

import net.hellomouse.photonic.block.entity.ArcFurnaceEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArcFurnace extends BlockWithEntity implements BlockEntityProvider {
	public ArcFurnace(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ArcFurnaceEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public <T extends BlockEntity>BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, MachineBlockRegistry.ARC_FURNACE_ENTITY, ArcFurnaceEntity::tick);
	}

}
