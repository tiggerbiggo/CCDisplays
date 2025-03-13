package com.mc3699.ccdisplays.graphicsmonitor;

import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

public class GraphicsMonitorPeripheral implements IPeripheral {

    private final GraphicsMonitorBlockEntity blockEntity;

    public GraphicsMonitorPeripheral(GraphicsMonitorBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }




    @Override
    public String getType() {
        return "graphics_monitor";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }


}
