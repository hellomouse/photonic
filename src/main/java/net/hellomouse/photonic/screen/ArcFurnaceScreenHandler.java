package net.hellomouse.photonic.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ArcFurnaceScreenHandler extends ScreenHandler {
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;


	public ArcFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(2));
	}
	public ArcFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ScreenHandlerRegistry.ARC_FURNACE_SCREEN_HANDLER, syncId);
		checkSize(inventory, 3);
		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);
		this.propertyDelegate = propertyDelegate;

		this.addSlot(new Slot(inventory, 0, 12, 15));
		this.addSlot(new Slot(inventory, 1, 22, 15));
		this.addSlot(new Slot(inventory, 2, 22, 15));

		addPlayerInventory(playerInventory);
		addPlayerHotbar(playerInventory);

		addProperties(propertyDelegate);
	}
	@Override
	public ItemStack quickTransfer(PlayerEntity player, int fromIndex) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	public boolean isCrafting() {
		return propertyDelegate.get(0) > 0;
	}

	public int getScaledProgress() {
		int progress = this.propertyDelegate.get(0);
		int maxProgress = this.propertyDelegate.get(1);

		if (progress > maxProgress) return 100; // In case we keep ticking progress
		return (progress / maxProgress) * 100;
	}

	private void addPlayerInventory(PlayerInventory playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
			}
		}
	}

	private void addPlayerHotbar(PlayerInventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
		}
	}
}
