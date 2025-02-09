package com.grim3212.assorted.storage.common.block.blockentity;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

public interface LockedWorldlyContainer extends WorldlyContainer {

	boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction face);

	boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction face);

	default boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction face, String lockCode, boolean force) {
		return this.canPlaceItemThroughFace(slot, stack, face);
	}

	default boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction face, String lockCode, boolean force) {
		return this.canTakeItemThroughFace(slot, stack, face);
	}

}
