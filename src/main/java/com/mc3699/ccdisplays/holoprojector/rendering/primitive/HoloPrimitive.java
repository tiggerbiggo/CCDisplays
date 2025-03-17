package com.mc3699.ccdisplays.holoprojector.rendering.primitive;

import com.mc3699.ccdisplays.holoprojector.rendering.HoloColor;
import com.mc3699.ccdisplays.holoprojector.rendering.IHoloDrawable;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.util.ObjectUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;

import java.util.HashMap;

public class HoloPrimitive implements IHoloDrawable {
    private static final String HOLOTYPE = "PRIMITIVE";
    static{
        IHoloDrawable.registerType(HOLOTYPE, HoloPrimitive::fromTag);
    }
    private final HoloOffset offset;
    private final HoloPrimitiveType primitive;
    public final HoloColor col;
    private final String offsetName;
    public HoloPrimitive(HoloOffset offset, HoloPrimitiveType primitive, int r, int g, int b, int a, String offsetName) {
        this.offset = offset;
        this.primitive = primitive;
        this.col = new HoloColor(r, g, b, a);
        this.offsetName = offsetName;
    }

    public HoloPrimitive(HoloOffset offset, HoloPrimitiveType primitive, HashMap<String, Object> params) {
        this.offset = offset;
        this.primitive = primitive;
        int r = getParamOrDefault(params, "r", 0);
        int g = getParamOrDefault(params, "g", 0);
        int b = getParamOrDefault(params, "b", 0);
        int a = getParamOrDefault(params, "a", 255);
        this.col = new HoloColor(r, g, b, a);
        this.offsetName = (String)params.get("offsetName");
    }

    private static int getParamOrDefault(HashMap<String, Object> params, String key, int defaultValue) {
        return ObjectUtils.getParamOrDefault(params, key, defaultValue, Integer.class);
    }


    @Override
    public String getType() {
        return "PRIMITIVE";
    }

    @Override
    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();
        offset.applyOffset(pPoseStack);
        VertexConsumer primitiveConsumer = pBuffer.getBuffer(RenderType.debugQuads());

        //Didn't want to pass down VertexConsumer and combinedLight, so i'm just using 1 for the light
        //and grabbing a new consumer from the buffer
        primitive.drawPrimitive(primitiveConsumer, pPoseStack, 1, col);
        pPoseStack.popPose();
    }

    @Override
    public CompoundTag generateTag() {
        CompoundTag tag = new CompoundTag();
        col.writeTag(tag);
        tag.put("holoType", StringTag.valueOf(HOLOTYPE));
        tag.put("offset", offset.generateTag());
        tag.put("primitiveType", StringTag.valueOf(primitive.name()));
        if(offsetName != null) {
            tag.put("offsetName", StringTag.valueOf(offsetName));
        }
        return tag;
    }

    @Override
    public String getOffsetName() {
        return offsetName;
    }

    public static HoloPrimitive fromTag(CompoundTag tag){
        int r = tag.getInt("r");
        int g = tag.getInt("g");
        int b = tag.getInt("b");
        int a = tag.getInt("a");

        String offsetName = null;
        if(tag.contains("offsetName")) {
            offsetName = tag.getString("offsetName");
        }

        HoloOffset offset = HoloOffset.fromTag(tag.getCompound("offset"));
        String typeName = tag.getString("primitiveType");
        HoloPrimitiveType type;
        try {
            type = HoloPrimitiveType.valueOf(typeName);
        }
        catch (IllegalArgumentException e){
            type = HoloPrimitiveType.BOX;
        }
        return new HoloPrimitive(offset, type, r, g, b, a, offsetName);
    }

    public HashMap<String, Object> asMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("r", (float)col.r);
        result.put("g", (float)col.g);
        result.put("b", (float)col.b);
        result.put("a", (float)col.a);
        result.put("offsetName", offsetName);
        result.put("primitiveType", primitive.name());
        offset.putMap(result);
        return result;
    }
}
