package com.mc3699.ccdisplays.holoprojector;

import com.mc3699.ccdisplays.holoprojector.rendering.offset.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitive;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitiveType;
import com.mc3699.ccdisplays.holoprojector.rendering.text.HoloTextElement;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class HoloProjectorPeripheral implements IPeripheral {

    private final HoloProjectorBlockEntity blockEntity;

    HoloProjectorPeripheral(HoloProjectorBlockEntity blockEntity)
    {
        this.blockEntity = blockEntity;
    }


    @Override
    public String getType() {
        return "holo_projector";
    }

    @LuaFunction
    public final int addText(String text, double x, double y, double z, double rotation, double scale, int color, @Nullable String offset)
    {
        HoloTextElement element;
        if(offset == null) {
            element = new HoloTextElement((float) x, (float) y, (float) z, (float) rotation, (float) scale, color, text);
        }
        else{
            element = new HoloTextElement((float) x, (float) y, (float) z, (float) rotation, (float) scale, color, text, offset);
        }
        return this.blockEntity.getElementManager().addText(element);
    }

    @LuaFunction
    public final boolean replaceText(int index, String text, double x, double y, double z, double rotation, double scale, int color, @Nullable String offset) {
        if(index >= 0) {
            HoloTextElement element;
            if(offset == null) {
                element = new HoloTextElement((float) x, (float) y, (float) z, (float) rotation, (float) scale, color, text);
            }
            else{
                element = new HoloTextElement((float) x, (float) y, (float) z, (float) rotation, (float) scale, color, text, offset);
            }
            return this.blockEntity.getElementManager().replaceText(index, element);
        }
        return false;
    }

    @LuaFunction
    public final boolean setTextOffset(int index, String offsetName){
        HoloTextElement toUpdate = this.blockEntity.getElementManager().getText(index);
        if(toUpdate == null){
            return false;
        }
        HoloTextElement newText = new HoloTextElement(
                toUpdate.getXPos(),
                toUpdate.getYPos(),
                toUpdate.getZPos(),
                toUpdate.getRotation(),
                toUpdate.getScale(),
                toUpdate.getColor(),
                toUpdate.getText(),
                offsetName
        );

        return this.blockEntity.getElementManager().replaceText(index, newText);
    }

    @LuaFunction
    public final boolean setOffset(String name, double xPos, double yPos, double zPos, double xRot, double yRot, double zRot, double xScale, double yScale, double zScale){
        if(name == ""){
            return false;
        }
        HoloOffset offset = new HoloOffset(
                (float)xPos, (float)yPos, (float)zPos,
                (float)xRot, (float)yRot, (float)zRot,
                (float)xScale, (float)yScale, (float)zScale);
        return this.blockEntity.getElementManager().setOffset(name, offset);
    }

    @LuaFunction
    public boolean setPlayerBinding(String offsetName, String playerName, String modifierType){
        BindingModifier modifier;
        try {
            modifier = BindingModifier.valueOf(modifierType);
        }
        catch(IllegalArgumentException e){
            return false;
        }

        return this.blockEntity.getElementManager().setPlayerBinding(offsetName, playerName, modifier);
    }

    /*
    addPrimitive(String primitiveType, Table params)
    params:
    {
        x, y, z,
        rx, ry, rz,
        sx, sy, sz
    }
     */
    @LuaFunction
    public MethodResult addPrimitive(IArguments args){
        try {
            String primitiveType = args.getString(0);
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(1);
            HoloOffset offset = HoloOffset.fromParameterMap(params);
            HoloPrimitive primitive = new HoloPrimitive(offset, HoloPrimitiveType.valueOf(primitiveType), params);
            HashMap<String, Object> result = primitive.asMap();
            int id = this.blockEntity.getElementManager().addPrimitive(primitive);
            result.put("id", (float)id);
            return MethodResult.of(result);
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false);
        }
    }

    @LuaFunction
    public MethodResult replacePrimitive(IArguments args){
        try {
            int index = args.getInt(0);
            String primitiveType = args.getString(1);
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(2);
            HoloOffset offset = HoloOffset.fromParameterMap(params);
            HoloPrimitive primitive = new HoloPrimitive(offset, HoloPrimitiveType.valueOf(primitiveType), params);
            HashMap<String, Object> result = primitive.asMap();
            this.blockEntity.getElementManager().replacePrimitive(index, primitive);
            result.put("id", (float)index);
            return MethodResult.of(result);
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false);
        }
    }

    @LuaFunction
    public void clear()
    {
        this.blockEntity.getElementManager().clearElements();
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
