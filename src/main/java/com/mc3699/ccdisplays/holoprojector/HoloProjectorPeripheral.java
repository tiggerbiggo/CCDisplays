package com.mc3699.ccdisplays.holoprojector;

import com.mc3699.ccdisplays.holoprojector.rendering.ElementManager;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitive;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitiveType;
import com.mc3699.ccdisplays.holoprojector.rendering.text.HoloTextElement;
import com.mc3699.ccdisplays.util.ObjectUtils;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class HoloProjectorPeripheral implements IPeripheral {

    private final HoloProjectorBlockEntity blockEntity;

    private final ElementManager elementManager;

    HoloProjectorPeripheral(HoloProjectorBlockEntity blockEntity)
    {
        this.blockEntity = blockEntity;
        this.elementManager = blockEntity.getElementManager();
    }


    @Override
    public String getType() {
        return "holo_projector";
    }

    @LuaFunction
    public final MethodResult addText(IArguments args){
        try {
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(0);
            HoloTextElement textElement = new HoloTextElement(params);
            HashMap<String, Object> result = textElement.asMap();
            int id = elementManager.addElement(textElement);
            result.put("id", (float)id);
            return MethodResult.of(result);
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false, e.getMessage());
        }
    }

    @LuaFunction
    public final MethodResult replaceText(IArguments args){
        try {
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(0);
            HoloTextElement textElement = new HoloTextElement(params);
            HashMap<String, Object> result = textElement.asMap();
            int index = ObjectUtils.getParamOrDefault(params, "id", -1, Integer.class);
            if(index <= -1){
                return MethodResult.of(false, "id was nil or less than 0");
            }
            elementManager.replaceElement(index, textElement);
            result.put("id", index);
            return MethodResult.of(result);
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false, e.getMessage());
        }
    }

    @LuaFunction
    public final MethodResult setOffset(IArguments args){
        try{
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(0);
            if(params == null){
                return MethodResult.of(false, "Table expected, found " + args.getType(0));
            }
            String offsetName = ObjectUtils.getParamOrDefault(params, "offsetName", null, String.class);
            if(offsetName == null){
                return MethodResult.of(false, "Expected table key 'offsetName' to be a string, found non-string value or nil.");
            }
            elementManager.setOffset(offsetName, HoloOffset.fromParameterMap(params));
            return MethodResult.of();
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false, e.getMessage());
        }
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
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(0);
            if(params == null){
                return MethodResult.of(false, "Table expected, found " + args.getType(0));
            }
            String primitiveType = ObjectUtils.getParamOrDefault(params, "primitiveType", null, String.class);
            if(primitiveType == null){
                return MethodResult.of(false, "Expected table key 'primitiveType' to be a string, found non-string value or nil.");
            }
            HoloOffset offset = HoloOffset.fromParameterMap(params);
            HoloPrimitive primitive = new HoloPrimitive(offset, HoloPrimitiveType.valueOf(primitiveType), params);
            HashMap<String, Object> result = primitive.asMap();
            int id = elementManager.addElement(primitive);
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
            HashMap<String, Object> params = (HashMap<String, Object>) args.getTable(0);
            if(params == null){
                return MethodResult.of(false, "Table expected, found " + args.getType(0));
            }
            String primitiveType = ObjectUtils.getParamOrDefault(params, "primitiveType", null, String.class);
            if(primitiveType == null){
                return MethodResult.of(false, "Expected table key 'primitiveType' to be a string, found non-string value or nil.");
            }
            int index = ObjectUtils.getParamOrDefault(params, "id", -1, Integer.class);
            if(index <= -1){
                return MethodResult.of(false, "id was nil or less than 0");
            }
            HoloOffset offset = HoloOffset.fromParameterMap(params);
            HoloPrimitive primitive = new HoloPrimitive(offset, HoloPrimitiveType.valueOf(primitiveType), params);
            HashMap<String, Object> result = primitive.asMap();
            elementManager.replaceElement(index, primitive);
            result.put("id", (float)index);
            return MethodResult.of(result);
        }
        catch(LuaException | ClassCastException e){
            return MethodResult.of(false, e.getMessage());
        }
    }

    @LuaFunction
    public MethodResult getPrimitiveTypes(){
        //Object cast is for explicitly passing the whole array instead of varargs
        return MethodResult.of(HoloPrimitiveType.getValueList());
    }

    @LuaFunction
    public void clear()
    {
        this.blockEntity.getElementManager().clearAll();
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
