package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.PlayerBindings;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitive;
import com.mc3699.ccdisplays.holoprojector.rendering.primitive.HoloPrimitiveList;
import com.mc3699.ccdisplays.holoprojector.rendering.text.HoloTextElement;
import com.mc3699.ccdisplays.holoprojector.rendering.text.HoloTextList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.HashMap;

public class ElementManager {
    private final HoloProjectorBlockEntity entity;
    private final HoloTextList textList = new HoloTextList();

    private final HoloPrimitiveList primitiveList = new HoloPrimitiveList();
    private final HashMap<String, HoloOffset> offsetList = new HashMap<>();

    //Map of offset names to player bindings
    private final HashMap<String, PlayerBindings> bindingMap = new HashMap<>();

    public ElementManager(HoloProjectorBlockEntity entity){
        this.entity = entity;
    }

    public int addText(HoloTextElement element)
    {
        this.textList.add(element);
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
        return this.textList.size() - 1;
    }

    public boolean replaceText(int index, HoloTextElement element){
        if(index >= 0 && index < textList.size()) {
            textList.set(index, element);
            entity.setChanged();
            entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
            return true;
        }
        return false;
    }

    public int addPrimitive(HoloPrimitive primitive) {
        this.primitiveList.add(primitive);
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
        return this.primitiveList.size() - 1;
    }

    public void replacePrimitive(int index, HoloPrimitive primitive) {
        this.primitiveList.set(index, primitive);
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
    }

    public HoloTextElement getText(int index){
        if(index >= 0 && index < textList.size()) {
            return textList.get(index);
        }
        return null;
    }

    public boolean setOffset(String name, HoloOffset offset){
        if (!"".equals(name)){
            offsetList.put(name, offset);
            entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
            return true;
        }
        return false;
    }

    public boolean setPlayerBinding(String offsetName, String playerName, BindingModifier modifier){
        if(offsetName == null || playerName == null || modifier == null){
            return false;
        }
        PlayerBindings bindings = bindingMap.computeIfAbsent(offsetName, k -> new PlayerBindings());

        bindings.addModifier(playerName, modifier);

        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
        return true;
    }

    public HashMap<String, PlayerBindings> getBindingMap(){
        return bindingMap;
    }

    public void clearElements()
    {
        this.textList.clear();
        this.primitiveList.clear();
        this.offsetList.clear();
        this.bindingMap.clear();
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
    }

    public HoloTextList getTextList() {
        return this.textList;
    }

    public void saveAdditional(CompoundTag pTag) {
        textList.saveAdditional(pTag);
        primitiveList.saveAdditional(pTag);

        ListTag offsetListTag = new ListTag();

        for(String s : offsetList.keySet())
        {
            HoloOffset element = offsetList.get(s);
            CompoundTag tag = element.generateTag();
            tag.putString("offsetName", s);
            offsetListTag.add(tag);
        }
        pTag.put("offset", offsetListTag);

        ListTag playerBindingTag = new ListTag();
        ListTag offsetNameTag = new ListTag();

        for(String s : bindingMap.keySet()){
            PlayerBindings bindings = bindingMap.get(s);
            CompoundTag tag = bindings.generateTag();
            playerBindingTag.add(tag);
            offsetNameTag.add(StringTag.valueOf(s));
        }

        pTag.put("playerBinding", playerBindingTag);
        pTag.put("offsetBindings", offsetNameTag);
    }

    public void load(CompoundTag pTag) {
        this.textList.clear();

        if(pTag.contains("text", ListTag.TAG_LIST))
        {
            ListTag textListTag = pTag.getList("text", ListTag.TAG_COMPOUND);
            for(int i = 0; i < textListTag.size(); i++)
            {
                CompoundTag elementTag = textListTag.getCompound(i);
                this.textList.add(HoloTextElement.fromTag(elementTag));
            }
        }

        this.primitiveList.clear();
        if(pTag.contains("primitives", ListTag.TAG_LIST))
        {
            ListTag primitiveListTag = pTag.getList("primitives", ListTag.TAG_COMPOUND);
            for(int i = 0; i < primitiveListTag.size(); i++)
            {
                CompoundTag elementTag = primitiveListTag.getCompound(i);
                this.primitiveList.add(HoloPrimitive.fromTag(elementTag));
            }
        }

        this.offsetList.clear();
        if(pTag.contains("offset", ListTag.TAG_LIST))
        {
            ListTag offsetListTag = pTag.getList("offset", ListTag.TAG_COMPOUND);
            for(int i = 0; i < offsetListTag.size(); i++)
            {
                CompoundTag offsetTag = offsetListTag.getCompound(i);
                HoloOffset offset = HoloOffset.fromTag(offsetTag);
                this.offsetList.put(offsetTag.getString("offsetName"), offset);
            }
        }

        this.bindingMap.clear();
        if(pTag.contains("offsetBindings", ListTag.TAG_LIST)){
            if(pTag.contains("playerBinding", ListTag.TAG_LIST))
            {
                ListTag playerBindingTag = pTag.getList("playerBinding", ListTag.TAG_COMPOUND);
                ListTag offsetNameTag = pTag.getList("offsetBindings", ListTag.TAG_STRING);
                if(playerBindingTag.size() != offsetNameTag.size()){
                    return;
                }
                for(int i = 0; i < playerBindingTag.size(); i++)
                {
                    CompoundTag bindingTag = playerBindingTag.getCompound(i);
                    String offsetName = offsetNameTag.getString(i);
                    this.bindingMap.put(offsetName, PlayerBindings.fromTag(bindingTag));
                }
            }
        }
    }

    public HashMap<String, HoloOffset> getOffsets() {
        return offsetList;
    }

    public HoloPrimitiveList getPrimitiveList() {
        return primitiveList;
    }
}
