package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

public interface IHoloDrawable {
    void draw(PoseStack pPoseStack, MultiBufferSource pBuffer);
    CompoundTag generateTag();

    String getOffsetName();
}
