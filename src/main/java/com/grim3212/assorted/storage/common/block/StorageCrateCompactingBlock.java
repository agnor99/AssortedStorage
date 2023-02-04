package com.grim3212.assorted.storage.common.block;

import com.grim3212.assorted.storage.common.block.blockentity.StorageCrateCompactingBlockEntity;
import com.grim3212.assorted.storage.common.util.CrateLayout;
import com.grim3212.assorted.storage.common.util.StorageMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StorageCrateCompactingBlock extends StorageCrateBlock {

	public StorageCrateCompactingBlock(StorageMaterial material, CrateLayout layout) {
		super(material, layout);
	}

	public StorageCrateCompactingBlock(StorageMaterial material, CrateLayout layout, Block.Properties props) {
		super(material, layout, props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageCrateCompactingBlockEntity(pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tileentity = worldIn.getBlockEntity(pos);

			if (tileentity instanceof StorageCrateCompactingBlockEntity crate) {
				Containers.dropContents(worldIn, pos, crate.asItemStacks());
				Containers.dropContents(worldIn, pos, crate.getEnhancements());
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
				worldIn.removeBlockEntity(pos);
			}
		}
	}
}
