package com.mc3699.ccdisplays.holoprojector.rendering;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;

public class HoloColor {
    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public HoloColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static HoloColor fromInt(int color){
        int a = (color >> 24) & 0xFF;  // Alpha (bits 24-31)
        int r = (color >> 16) & 0xFF;  // Red (bits 16-23)
        int g = (color >> 8) & 0xFF;   // Green (bits 8-15)
        int b = color & 0xFF;          // Blue (bits 0-7)

        return new HoloColor(r, g, b, a);
    }

    public void writeTag(CompoundTag tag) {
        tag.put("r", IntTag.valueOf(r));
        tag.put("g", IntTag.valueOf(g));
        tag.put("b", IntTag.valueOf(b));
        tag.put("a", IntTag.valueOf(a));
    }
}
