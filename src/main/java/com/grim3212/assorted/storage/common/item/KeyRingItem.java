package com.grim3212.assorted.storage.common.item;

import com.grim3212.assorted.storage.common.inventory.keyring.KeyRingCapabilityProvider;
import com.grim3212.assorted.storage.common.inventory.keyring.KeyRingContainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class KeyRingItem extends Item {

	public KeyRingItem(Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new KeyRingCapabilityProvider(stack, nbt);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		if (!level.isClientSide) {
			playerIn.openMenu(new MenuProvider() {

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
					return new KeyRingContainer(id, player.level, player.blockPosition(), inv, player);
				}

				@Override
				public Component getDisplayName() {
					return playerIn.getItemInHand(handIn).getHoverName();
				}
			});
		}
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}
}
