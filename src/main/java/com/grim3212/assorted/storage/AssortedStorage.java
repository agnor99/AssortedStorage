package com.grim3212.assorted.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.grim3212.assorted.storage.client.data.StorageBlockstateProvider;
import com.grim3212.assorted.storage.client.data.StorageItemModelProvider;
import com.grim3212.assorted.storage.client.proxy.ClientProxy;
import com.grim3212.assorted.storage.common.block.StorageBlocks;
import com.grim3212.assorted.storage.common.block.tileentity.StorageTileEntityTypes;
import com.grim3212.assorted.storage.common.crafting.StorageRecipeSerializers;
import com.grim3212.assorted.storage.common.data.StorageBlockTagProvider;
import com.grim3212.assorted.storage.common.data.StorageItemTagProvider;
import com.grim3212.assorted.storage.common.data.StorageLootProvider;
import com.grim3212.assorted.storage.common.data.StorageRecipes;
import com.grim3212.assorted.storage.common.inventory.StorageContainerTypes;
import com.grim3212.assorted.storage.common.network.PacketHandler;
import com.grim3212.assorted.storage.common.proxy.IProxy;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AssortedStorage.MODID)
public class AssortedStorage {
	public static final String MODID = "assortedstorage";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static IProxy proxy = new IProxy() {
	};

	public static final ItemGroup ASSORTED_STORAGE_ITEM_GROUP = (new ItemGroup("assortedstorage") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(StorageBlocks.WOOD_CABINET.get());
		}
	});

	public AssortedStorage() {
		DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.starting();

		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setup);
		modBus.addListener(this::gatherData);

		StorageBlocks.BLOCKS.register(modBus);
		StorageBlocks.ITEMS.register(modBus);
		StorageTileEntityTypes.TILE_ENTITIES.register(modBus);
		StorageContainerTypes.CONTAINERS.register(modBus);
		StorageRecipeSerializers.RECIPES.register(modBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}

	private void gatherData(GatherDataEvent event) {
		DataGenerator datagenerator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			datagenerator.addProvider(new StorageRecipes(datagenerator));
			StorageBlockTagProvider blockTagProvider = new StorageBlockTagProvider(datagenerator, fileHelper);
			datagenerator.addProvider(blockTagProvider);
			datagenerator.addProvider(new StorageItemTagProvider(datagenerator, blockTagProvider, fileHelper));
			datagenerator.addProvider(new StorageLootProvider(datagenerator));
		}

		if (event.includeClient()) {
			datagenerator.addProvider(new StorageBlockstateProvider(datagenerator, fileHelper));
			datagenerator.addProvider(new StorageItemModelProvider(datagenerator, fileHelper));
		}
	}
}
