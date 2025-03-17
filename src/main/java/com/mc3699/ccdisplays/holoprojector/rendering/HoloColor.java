package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.util.ObjectUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;

import java.util.HashMap;

public class HoloColor {
    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public HoloColor(int r, int g, int b, int a) {
        this.r = Math.min(255, Math.max(0, r));
        this.g = Math.min(255, Math.max(0, g));
        this.b = Math.min(255, Math.max(0, b));
        this.a = Math.min(255, Math.max(0, a));
    }

    public HoloColor(){
        this(255, 255, 255, 255);
    }

    public HoloColor (int color){
        this(
                (color >> 16) & 0xFF,  // Red (bits 16-23)
                (color >> 8) & 0xFF,   // Green (bits 8-15)
                color & 0xFF,          // Blue (bits 0-7)
                (color >> 24) & 0xFF  // Alpha (bits 24-31)
        );
    }

    public HoloColor(HashMap<String, Object> params) {
        this(
                ObjectUtils.getParamOrDefault(params, "r", 255, Integer.class),
                ObjectUtils.getParamOrDefault(params, "g", 255, Integer.class),
                ObjectUtils.getParamOrDefault(params, "b", 255, Integer.class),
                ObjectUtils.getParamOrDefault(params, "a", 255, Integer.class)
        );
    }

    public void writeTag(CompoundTag tag) {
        tag.put("r", IntTag.valueOf(r));
        tag.put("g", IntTag.valueOf(g));
        tag.put("b", IntTag.valueOf(b));
        tag.put("a", IntTag.valueOf(a));
    }

    public int toRGBA() {
        // (a << 24) | (r << 16) | (g << 8) | b
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                (b & 0xFF);
    }
}
