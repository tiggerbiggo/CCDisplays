package com.mc3699.ccdisplays.holoprojector;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

public class HoloProjectorPeripheral implements IPeripheral {

    private final HoloProjectorBlockEntity blockEntity;

    HoloProjectorPeripheral(HoloProjectorBlockEntity blockEntity)
    {
        this.blockEntity = blockEntity;
    }


    @Override
    public String getType() {
        return "holo_projector";
    }

    @LuaFunction
    public void addText(String text, double x, double y, double z, double rotation, double scale, int color)
    {
        this.blockEntity.addText(text, (float) x, (float) y, (float) z, (float) rotation, (float) scale, color);
    }

    @LuaFunction
    public void clear()
    {
        this.blockEntity.clearElements();
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
