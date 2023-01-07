package net.hellomouse.photonic.util;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;
import java.util.function.Predicate;

// Adapted from https://github.com/illusivesoulworks/veinmining/blob/1.19.3/common/src/main/java/com/illusivesoulworks/veinmining/common/veinmining/logic/VeinMiningLogic.java
// GNU public license
public class Floodfill {
	private static final Direction[] CARDINAL_DIRECTIONS =
			new Direction[] {
					Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
					Direction.NORTH, Direction.SOUTH };

	/**
	 * Perform a floodfill, returning a list of block positions that match the criteria:
	 * Note: the first block position does not necessarily need to match the criteria
	 * but it will not be included if it doesn't (its neighbors will be considered)
	 * @param world World
	 * @param start Pos to start floodfill (included)
	 * @param limit Max blocks to return
	 * @param maxDistance Max distance from start pos to consider
	 * @param allowDiagonal Allow not directly adjacent blocks
	 * @param filter Filter what blocks are included in the floodfill here
	 * @return List of block positions
	 */
	public static ArrayList<BlockPos> floodfill(ServerWorld world, BlockPos start, long limit, long maxDistance, boolean allowDiagonal, Predicate<Pair<BlockState, BlockPos>> filter) {
		ArrayList<BlockPos> blocks = new ArrayList<>();
		Set<BlockPos> visited = new HashSet<>();
		LinkedList<BlockPos> toVisit = new LinkedList<>();
		int foundCount = 0;
		toVisit.add(start);
		maxDistance = (maxDistance < 0 ? -1 : 1) * maxDistance * maxDistance; // We work in squared distances

		while (!toVisit.isEmpty() && foundCount < limit) {
			BlockPos here = toVisit.pop();
			if (visited.contains(here))
				continue;

			BlockState blockState = world.getBlockState(here);
			double distance = maxDistance > 0 ? here.getSquaredDistance(start) : maxDistance;
			boolean valid = filter.test(new Pair<>(blockState, here)) && distance <= maxDistance;
			visited.add(here);

			if (here.equals(start) || valid) {
				if (valid) {
					blocks.add(here);
					foundCount++;
				}

				// Also add neighbors to queue
				BlockPos[] blockPositions = new BlockPos[] {here.up(), here.down(), here};
				toVisit.add(here.up());
				toVisit.add(here.down());

				if (allowDiagonal) {
					for (BlockPos blockPos : blockPositions) {
						toVisit.add(blockPos.west());
						toVisit.add(blockPos.east());
						toVisit.add(blockPos.north());
						toVisit.add(blockPos.south());

						toVisit.add(blockPos.north().west());
						toVisit.add(blockPos.north().east());
						toVisit.add(blockPos.south().west());
						toVisit.add(blockPos.south().east());
					}
				} else {
					for (Direction direction: CARDINAL_DIRECTIONS)
						toVisit.add(here.offset(direction));
				}
			}
		}
		return blocks;
	}
}
