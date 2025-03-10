package com.mc3699.ccdisplays.holoprojector;

import com.mc3699.ccdisplays.holoprojector.rendering.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.HoloTextElement;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

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
        return this.blockEntity.addText(element);
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
            return this.blockEntity.replaceText(index, element);
        }
        return false;
    }

    @LuaFunction
    public final boolean setTextOffset(int index, String offsetName){
        HoloTextElement toUpdate = this.blockEntity.getText(index);
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

        return this.blockEntity.replaceText(index, newText);
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
        return this.blockEntity.setOffset(name, offset);
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

        return this.blockEntity.setPlayerBinding(offsetName, playerName, modifier);
    }

    @LuaFunction
    public void clear()
    {
        this.blockEntity.clearElements();
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
