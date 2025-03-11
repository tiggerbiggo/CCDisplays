package com.mc3699.ccdisplays.holoprojector.rendering.primitive;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

@FunctionalInterface
public interface IPrimitiveDrawer {
    void draw(
            VertexConsumer vertexConsumer,
            PoseStack poseStack,
            int combinedLight,
            int red,
            int green,
            int blue,
            int alpha
    );
}
