package com.grim3212.assorted.storage.common.inventory.bag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.grim3212.assorted.storage.common.util.StorageMaterial;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class BagCapabilityProvider implements ICapabilitySerializable<Tag> {
	private final BagItemHandler inventory;
	private final LazyOptional<IItemHandler> optional;

	public BagCapabilityProvider(ItemStack stack, @Nullable StorageMaterial material, CompoundTag nbtIn) {
		inventory = new BagItemHandler(stack, material);
		optional = LazyOptional.of(() -> inventory);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return optional.cast();
		} else
			return LazyOptional.empty();
	}

	@Override
	public Tag serializeNBT() {
		inventory.save();
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(Tag nbt) {
		inventory.load();
	}
}
