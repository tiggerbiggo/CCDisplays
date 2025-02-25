package com.mc3699.ccdisplays.util;

import com.mc3699.ccdisplays.CCDisplays;
import com.mc3699.ccdisplays.holoprojector.rendering.HoloProjectorBlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CCDisplays.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ModBlockEntities.HOLOGRAM_PROJECTOR.get(), HoloProjectorBlockEntityRenderer::new);
    }

}
