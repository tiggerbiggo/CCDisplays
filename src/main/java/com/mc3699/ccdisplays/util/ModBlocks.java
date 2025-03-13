package com.mc3699.ccdisplays.util;

import com.mc3699.ccdisplays.CCDisplays;
import com.mc3699.ccdisplays.graphicsmonitor.GraphicsMonitorBlock;
import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CCDisplays.MODID);


    public static RegistryObject<Block> HOLOGRAM_PROJECTOR = registerBlock("holo_projector", HoloProjectorBlock::new);

    public static RegistryObject<Block> GRAPHICS_MONITOR = registerBlock("graphics_monitor", GraphicsMonitorBlock::new);



    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
