package com.mc3699.ccdisplays.holoprojector.rendering.offset;

import com.mc3699.ccdisplays.holoprojector.rendering.IHoloDrawable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.List;

public class HoloOffset {
    private final float xPos;
    private final float yPos;
    private final float zPos;
    private final float xRot;
    private final float yRot;
    private final float zRot;
    private final float xScale;
    private final float yScale;
    private final float zScale;

    public HoloOffset(float xPos, float yPos, float zPos, float xRot, float yRot, float zRot, float xScale, float yScale, float zScale) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
    }

    public HoloOffset(){
        this(0, 0, 0, 0, 0, 0, 1, 1, 1);
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getzPos() {
        return zPos;
    }

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public float getzRot() {
        return zRot;
    }

    public float getxScale() {
        return xScale;
    }

    public float getyScale() {
        return yScale;
    }

    public float getzScale() {
        return zScale;
    }

    //Will eventually remove this, and just use applyOffset instead
    //The goal is for each element to have its own offset
    @Deprecated
    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer, List<IHoloDrawable> elements) {
        pPoseStack.pushPose();
        pPoseStack.translate(this.xPos,this.yPos,this.zPos);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(this.xRot));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(this.yRot));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(this.zRot));
        pPoseStack.scale(this.xScale,this.yScale,this.zScale);
        for(IHoloDrawable element : elements){
            element.draw(pPoseStack, pBuffer);
        }
        pPoseStack.popPose();
    }

    public void applyOffset(PoseStack pPoseStack) {
        pPoseStack.translate(this.xPos,this.yPos,this.zPos);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(this.xRot));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(this.yRot));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(this.zRot));
        pPoseStack.scale(this.xScale,this.yScale,this.zScale);
    }

    public CompoundTag generateTag(){
        CompoundTag elementTag = new CompoundTag();
        elementTag.putFloat("x",this.xPos);
        elementTag.putFloat("y",this.yPos);
        elementTag.putFloat("z",this.zPos);
        elementTag.putFloat("xRot",this.xRot);
        elementTag.putFloat("yRot",this.yRot);
        elementTag.putFloat("zRot",this.zRot);
        elementTag.putFloat("xScale",this.xScale);
        elementTag.putFloat("yScale",this.yScale);
        elementTag.putFloat("zScale",this.zScale);
        return elementTag;
    }

    public static HoloOffset fromTag(CompoundTag tag){
        return new HoloOffset(
                tag.getFloat("x"),
                tag.getFloat("y"),
                tag.getFloat("z"),
                tag.getFloat("xRot"),
                tag.getFloat("yRot"),
                tag.getFloat("zRot"),
                tag.getFloat("xScale"),
                tag.getFloat("yScale"),
                tag.getFloat("zScale")
        );
    }

    private static float getParamOrDefault(HashMap<String, Object> params, String key, float defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else {
            return defaultValue;
        }
    }
    public static HoloOffset fromParameterMap(HashMap<String, Object> params){
        if(params == null){
            return new HoloOffset();
        }
        float x = getParamOrDefault(params, "x", 0);
        float y = getParamOrDefault(params, "y", 0);
        float z = getParamOrDefault(params, "z", 0);
        float rx = getParamOrDefault(params, "rx", 0);
        float ry = getParamOrDefault(params, "ry", 0);
        float rz = getParamOrDefault(params, "rz", 0);
        float sx = getParamOrDefault(params, "sx", 1);
        float sy = getParamOrDefault(params, "sy", 1);
        float sz = getParamOrDefault(params, "sz", 1);

        return new HoloOffset(x, y, z, rx, ry, rz, sx, sy, sz);
    }

    public void putMap(HashMap<String, Object> result) {
        result.put("x", xPos);
        result.put("y", yPos);
        result.put("z", zPos);
        result.put("rx", xRot);
        result.put("ry", yRot);
        result.put("rz", zRot);
        result.put("sx", xScale);
        result.put("sy", yScale);
        result.put("sz", zScale);
    }
}
