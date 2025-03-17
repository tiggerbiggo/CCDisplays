package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.offset.PlayerBindings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElementManager {
    private final HoloProjectorBlockEntity entity;
    private final List<IHoloDrawable> elementList = new ArrayList<>();
    private final HashMap<String, HoloOffset> offsetList = new HashMap<>();

    //Map of offset names to player bindings
    private final HashMap<String, PlayerBindings> bindingMap = new HashMap<>();

    public ElementManager(HoloProjectorBlockEntity entity){
        this.entity = entity;
    }


    public boolean setOffset(String name, HoloOffset offset){
        if (!"".equals(name)){
            offsetList.put(name, offset);
            entity.setChanged();
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

    public int addElement(IHoloDrawable element) {
        elementList.add(element);
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
        return elementList.size() - 1;
    }

    public boolean replaceElement(int index, IHoloDrawable element) {
        if(index < 0 || index >= elementList.size()){
            return false;
        }
        elementList.set(index, element);
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
        return true;
    }

    public HashMap<String, PlayerBindings> getBindingMap(){
        return bindingMap;
    }

    public void clearAll()
    {
        elementList.clear();
        offsetList.clear();
        bindingMap.clear();
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(),entity.getBlockState(),entity.getBlockState(),3);
    }

    public void saveAdditional(CompoundTag pTag) {
        ListTag elementListTag = new ListTag();
        for(IHoloDrawable drawable : elementList){
            elementListTag.add(drawable.generateTag());
        }
        pTag.put("elements", elementListTag);

        CompoundTag offsetCTag = new CompoundTag();
        for(String s : offsetList.keySet())
        {
            HoloOffset element = offsetList.get(s);
            CompoundTag tag = element.generateTag();
            offsetCTag.put(s, tag);
        }
        pTag.put("offsets", offsetCTag);

        CompoundTag offsetBindingsCTag = new CompoundTag();

        for(String s : bindingMap.keySet()){
            PlayerBindings bindings = bindingMap.get(s);
            CompoundTag tag = bindings.generateTag();
            offsetBindingsCTag.put(s, tag);
        }

        pTag.put("offsetBindings", offsetBindingsCTag);
    }

    public void load(CompoundTag pTag) {
        this.elementList.clear();
        if(pTag.contains("elements", ListTag.TAG_LIST)){
            ListTag elementListTag = pTag.getList("elements", ListTag.TAG_COMPOUND);
            for(int i = 0; i < elementListTag.size(); i++)
            {
                CompoundTag elementTag = elementListTag.getCompound(i);
                IHoloDrawable drawable = IHoloDrawable.load(elementTag);
                elementList.add(drawable);
            }
        }

        this.offsetList.clear();
        if(pTag.contains("offsets", CompoundTag.TAG_COMPOUND))
        {
            CompoundTag offsetListCTag = pTag.getCompound("offsets");
            for(String offsetName : offsetListCTag.getAllKeys()){
                CompoundTag offsetCTag = offsetListCTag.getCompound(offsetName);
                HoloOffset offset = HoloOffset.fromTag(offsetCTag);
                offsetList.put(offsetName, offset);
            }
        }

        this.bindingMap.clear();
        if(pTag.contains("offsetBindings", CompoundTag.TAG_COMPOUND)){
            //Full map of offsetName to <playerName, [bindingType, ...]>
            CompoundTag offsetBindingListCTag = pTag.getCompound("offsetBindings");
            for(String offsetName : offsetBindingListCTag.getAllKeys()){
                //Inner map of playerName to [bindingType, ...]

                CompoundTag playerBindingListCTag = offsetBindingListCTag.getCompound(offsetName);
                bindingMap.put(offsetName, PlayerBindings.fromTag(playerBindingListCTag));
            }
        }
    }

    public HashMap<String, HoloOffset> getOffsets() {
        return offsetList;
    }


    public List<IHoloDrawable> getElements() {
        return elementList;
    }
}
