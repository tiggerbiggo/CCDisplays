package com.mc3699.ccdisplays.holoprojector.rendering.text;

import com.mc3699.ccdisplays.holoprojector.rendering.IHoloDrawable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

public class HoloTextElement implements IHoloDrawable {

    private final float xPos;
    private final float yPos;
    private final float zPos;
    private final int color;
    private final float rotation;
    private final float scale;
    private final String text;
    private final String offsetName;

    public HoloTextElement(float x, float y, float z, float rotation, float scale, int color, String text, String offset)
    {
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
        this.color = color;
        this.text = text;
        this.scale = scale;
        this.rotation = rotation;
        this.offsetName = offset;
    }
    public HoloTextElement(float x, float y, float z, float rotation, float scale, int color, String text)
    {
        this(x, y, z, rotation, scale, color, text, "");
    }



    public String getText() {
        return text;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public float getZPos() {
        return zPos;
    }

    public int getColor() {
        return color;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public String getOffsetName(){
        return offsetName;
    }

    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();
        pPoseStack.translate(this.xPos,this.yPos,this.zPos);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-180));
        pPoseStack.scale(this.scale,this.scale,this.scale);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(this.rotation));
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(
                this.text,
                0,
                0,
                this.color,
                false,
                pPoseStack.last().pose(),
                pBuffer,
                Font.DisplayMode.NORMAL,
                0x00AA00,
                0xF000F0
        );

        pPoseStack.popPose();
    }

    public CompoundTag generateTag(){
        CompoundTag elementTag = new CompoundTag();
        elementTag.putString("text",this.text);
        elementTag.putFloat("x",this.xPos);
        elementTag.putFloat("y",this.yPos);
        elementTag.putFloat("z",this.zPos);
        elementTag.putFloat("rotation",this.rotation);
        elementTag.putFloat("scale",this.scale);
        elementTag.putInt("color",this.color);
        elementTag.putString("offset",this.offsetName);
        return elementTag;
    }

    public static HoloTextElement fromTag(CompoundTag tag){
        return new HoloTextElement(
                tag.getFloat("x"),
                tag.getFloat("y"),
                tag.getFloat("z"),
                tag.getFloat("rotation"),
                tag.getFloat("scale"),
                tag.getInt("color"),
                tag.getString("text"),
                tag.getString("offset")
        );
    }
}
