package com.mc3699.ccdisplays.holoprojector.rendering.primitive;

import com.mc3699.ccdisplays.holoprojector.rendering.HoloColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.Arrays;
import java.util.List;

public enum HoloPrimitiveType {
    BOX(PrimitiveDrawMethods::drawCube),
    SPHERE(PrimitiveDrawMethods::drawSphere),
    CYLINDER(PrimitiveDrawMethods::drawCylinder);

    private static final List<String> valueList = Arrays.asList(HoloPrimitiveType.values()).stream().map(Enum::name).toList();

    private final IPrimitiveDrawer drawFunction;

    HoloPrimitiveType(IPrimitiveDrawer drawFunction){
        this.drawFunction = drawFunction;
    }

    public static List<String> getValueList() {
        return valueList;
    }

    public void drawPrimitive(
            VertexConsumer vertexConsumer,
            PoseStack poseStack,
            int combinedLight,
            int red, int green, int blue, int alpha){
        drawFunction.draw(vertexConsumer, poseStack, combinedLight, red, green, blue, alpha);
    }
    public void drawPrimitive(
            VertexConsumer vertexConsumer,
            PoseStack poseStack,
            int combinedLight,
            HoloColor col){
        drawPrimitive(vertexConsumer, poseStack, combinedLight, col.r, col.g, col.b, col.a);
    }
}
