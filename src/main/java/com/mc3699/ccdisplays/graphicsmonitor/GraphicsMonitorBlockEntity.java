package com.mc3699.ccdisplays.graphicsmonitor;

import com.mc3699.ccdisplays.util.ModBlockEntities;
import com.mc3699.ccdisplays.util.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GraphicsMonitorBlockEntity extends BlockEntity {

    private final GraphicsMonitorPeripheral peripheral;
    private final LazyOptional peripheralLazyOptional;


    public GraphicsMonitorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRAPHICS_MONITOR.get(), pPos, pBlockState);
        this.peripheral = new GraphicsMonitorPeripheral(this);
        this.peripheralLazyOptional = LazyOptional.of(() -> this.peripheral);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModCapabilities.PERIPHERAL_CAPABILITY)
        {
            return peripheralLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        peripheralLazyOptional.invalidate();
    }

    public GraphicsMonitorPeripheral getPeripheral() {
        return this.peripheral;
    }
}
