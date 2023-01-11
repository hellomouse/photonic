package net.hellomouse.photonic.block.entity;

import net.hellomouse.photonic.block.MachineBlockRegistry;
import net.hellomouse.photonic.screen.ArcFurnaceScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ArcFurnaceEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory {
	protected final PropertyDelegate propertyDelegate;
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private int progress = 0;
	private int maxProgress = 100;

	public ArcFurnaceEntity(BlockPos pos, BlockState state) {
		super(MachineBlockRegistry.ARC_FURNACE_ENTITY, pos, state);
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				switch (index) {
					case 0: return ArcFurnaceEntity.this.progress;
					case 1: return ArcFurnaceEntity.this.maxProgress;
					default: return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0: ArcFurnaceEntity.this.progress = value; break;
					case 1: ArcFurnaceEntity.this.maxProgress = value; break;
				}
			}

			@Override
			public int size() {
				return 2;
			}
		};
	}

	public static void tick(World world, BlockPos blockPos, BlockState state, ArcFurnaceEntity entity) {
		if (world.isClient()) return;

		if (hasRecipe(entity)) {
			entity.progress++;
			markDirty(world, blockPos, state);
			if (entity.progress >= entity.maxProgress) {
				produceItemFromRecipe(entity);
			}
		} else {
			entity.resetProgress();
			markDirty(world, blockPos, state);
		}
	}

	private static void produceItemFromRecipe(ArcFurnaceEntity entity) {
	}

	private void resetProgress() {
		progress = 0;
	}

	private static boolean hasRecipe(ArcFurnaceEntity entity) {
		return true;
	};

	@Override
	public Text getDisplayName() {
		return Text.literal("Arc Furnace");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new ArcFurnaceScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		Inventories.readNbt(nbt, inventory);
		super.readNbt(nbt);
		progress = nbt.getInt("arc_furnace.progress");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt("arc_furnace.progress", progress);
	}

	/**
	 * Gets the available slot positions that are reachable from a given side.
	 *
	 * @param side
	 */
	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[0];
	}

	/**
	 * Determines whether the given stack can be inserted into this inventory at the specified slot position from the given direction.
	 *
	 * @param slot
	 * @param stack
	 * @param dir
	 */
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}

	/**
	 * Determines whether the given stack can be removed from this inventory at the specified slot position from the given direction.
	 *
	 * @param slot
	 * @param stack
	 * @param dir
	 */
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Fetches the stack currently stored at the given slot. If the slot is empty,
	 * or is outside the bounds of this inventory, returns see {@link ItemStack#EMPTY}.
	 *
	 * @param slot
	 */
	@Override
	public ItemStack getStack(int slot) {
		return null;
	}

	/**
	 * Removes a specific number of items from the given slot.
	 *
	 * @param slot
	 * @param amount
	 * @return the removed items as a stack
	 */
	@Override
	public ItemStack removeStack(int slot, int amount) {
		return null;
	}

	/**
	 * Removes the stack currently stored at the indicated slot.
	 *
	 * @param slot
	 * @return the stack previously stored at the indicated slot.
	 */
	@Override
	public ItemStack removeStack(int slot) {
		return null;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {

	}
}
