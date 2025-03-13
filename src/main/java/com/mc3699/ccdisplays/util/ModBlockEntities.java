package com.mc3699.ccdisplays.util;

import com.mc3699.ccdisplays.CCDisplays;
import com.mc3699.ccdisplays.graphicsmonitor.GraphicsMonitorBlockEntity;
import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CCDisplays.MODID);

    public static final RegistryObject<BlockEntityType<HoloProjectorBlockEntity>> HOLOGRAM_PROJECTOR =
            BLOCK_ENTITIES.register("holo_projector",
                    () -> BlockEntityType.Builder.of(HoloProjectorBlockEntity::new, ModBlocks.HOLOGRAM_PROJECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<GraphicsMonitorBlockEntity>> GRAPHICS_MONITOR =
            BLOCK_ENTITIES.register("graphics_monitor",
                    () -> BlockEntityType.Builder.of(GraphicsMonitorBlockEntity::new, ModBlocks.GRAPHICS_MONITOR.get()).build(null));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
