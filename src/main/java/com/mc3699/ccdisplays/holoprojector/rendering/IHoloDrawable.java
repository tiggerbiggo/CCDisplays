package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface IHoloDrawable {
    Map<String, Function<CompoundTag, IHoloDrawable>> TYPE_REGISTRY = new HashMap<>();

    static void registerType(String id, Function<CompoundTag, IHoloDrawable> factory) {
        TYPE_REGISTRY.put(id, factory);
    }
    static IHoloDrawable load(CompoundTag tag) {
        String type = tag.getString("holoType");
        if (TYPE_REGISTRY.containsKey(type)) {
            return TYPE_REGISTRY.get(type).apply(tag);
        }
        throw new IllegalArgumentException("Unknown drawable type: " + type);
    }
    String getType();
    void draw(PoseStack pPoseStack, MultiBufferSource pBuffer);
    CompoundTag generateTag();

    String getOffsetName();
}
