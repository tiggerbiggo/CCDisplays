package com.mc3699.ccdisplays.util;

import com.mc3699.ccdisplays.graphicsmonitor.GraphicsMonitorBlockEntity;
import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;

public class ModPeripheralProviders implements IPeripheralProvider {
    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos blockPos, Direction direction) {

        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if(blockEntity instanceof HoloProjectorBlockEntity)
        {
            return LazyOptional.of(() -> ((HoloProjectorBlockEntity) blockEntity).getPeripheral());
        }

        if(blockEntity instanceof GraphicsMonitorBlockEntity)
        {
            return LazyOptional.of(() -> ((GraphicsMonitorBlockEntity) blockEntity).getPeripheral());
        }


        return LazyOptional.empty();
    }
}
