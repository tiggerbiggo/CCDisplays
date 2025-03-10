package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

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

    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer, List<HoloTextElement> elements) {
        pPoseStack.pushPose();
        pPoseStack.translate(this.xPos,this.yPos,this.zPos);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(this.xRot));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(this.yRot));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(this.zRot));
        pPoseStack.scale(this.xScale,this.yScale,this.zScale);
        for(HoloTextElement element : elements){
            element.draw(pPoseStack, pBuffer);
        }
        pPoseStack.popPose();
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
}
