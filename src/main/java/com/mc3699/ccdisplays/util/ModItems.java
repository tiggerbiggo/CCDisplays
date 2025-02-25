package com.mc3699.ccdisplays.util;

import com.mc3699.ccdisplays.CCDisplays;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCDisplays.MODID);



    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }


}
