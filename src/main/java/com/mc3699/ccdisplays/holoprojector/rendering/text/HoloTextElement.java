package com.mc3699.ccdisplays.holoprojector.rendering.text;

import com.mc3699.ccdisplays.holoprojector.rendering.HoloColor;
import com.mc3699.ccdisplays.holoprojector.rendering.IHoloDrawable;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.util.ObjectUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public class HoloTextElement implements IHoloDrawable {
    private static final String HOLOTYPE = "TEXT";
    static{
        IHoloDrawable.registerType(HOLOTYPE, HoloTextElement::new);
    }

    private final HoloOffset offset;
    private final String text;
    private final HoloColor col;
    private final String offsetName;

    public HoloTextElement(String text, HoloColor color, HoloOffset offset, String offsetName) {
        this.text = text != null ? text : "Sample Text";
        this.offset = offset != null ? offset : new HoloOffset();
        this.col = color != null ? color : new HoloColor();
        this.offsetName = offsetName;
    }

    public HoloTextElement(HashMap<String, Object> params) {
        this(
                ObjectUtils.getParamOrDefault(params, "text", null, String.class),
                new HoloColor(
                        params
                ),
                HoloOffset.fromParameterMap(params),
                ObjectUtils.getParamOrDefault(params, "offsetName", null, String.class)
        );
    }
    public HoloTextElement(CompoundTag tag){
        this(
                tag.contains("text") ? tag.getString("text") : null,
                tag.contains("color") ? new HoloColor(tag.getInt("color")) : null,
                tag.contains("offset") ? HoloOffset.fromTag(tag.getCompound("offset")) : null,
                tag.contains("offsetName") ? tag.getString("offsetName") : null
        );
    }

    public String getText() {
        return text;
    }

    public String getOffsetName(){
        return offsetName;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    @Override
    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();
        Font font = Minecraft.getInstance().font;
        float fontHeight = font.lineHeight; // Get the height of a line in the font
        float scale = 1.0f / fontHeight;    // Scale to make text height 1 block
        pPoseStack.scale(scale, scale, scale);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-180));
        //CURRENTLY BROKEN
        //ClientCameraCache.applyTextFlipping(pPoseStack);
        offset.applyOffset(pPoseStack);
        font.drawInBatch(
                text,
                0,
                0,
                col.toRGBA(),
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
        elementTag.putString("holoType", "TEXT");
        elementTag.putString("text", text);
        elementTag.put("offset", offset.generateTag());
        elementTag.putInt("color", col.toRGBA());
        if(offsetName != null){
            elementTag.putString("offsetName", offsetName);
        }
        return elementTag;
    }

    public HashMap<String, Object> asMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("r", (float)col.r);
        result.put("g", (float)col.g);
        result.put("b", (float)col.b);
        result.put("a", (float)col.a);
        if(offsetName != null){
            result.put("offsetName", offsetName);
        }
        result.put("text", text);
        offset.putMap(result);
        return result;
    }
}
