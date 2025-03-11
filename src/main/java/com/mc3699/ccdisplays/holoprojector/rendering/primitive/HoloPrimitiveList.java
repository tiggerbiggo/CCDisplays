package com.mc3699.ccdisplays.holoprojector.rendering.primitive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;

public class HoloPrimitiveList {
    private final List<HoloPrimitive> elements;

    public HoloPrimitiveList() {
        elements = new ArrayList<>();
    }

    public void add(HoloPrimitive element) {
        elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    public void set(int index, HoloPrimitive element) {
        elements.set(index, element);
    }

    public HoloPrimitive get(int index) {
        return elements.get(index);
    }

    public void clear() {
        elements.clear();
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public List<HoloPrimitive> elements(){
        return elements;
    }

    public void saveAdditional(CompoundTag pTag) {

        ListTag elementListTag = new ListTag();

        for(HoloPrimitive element :elements) {
            elementListTag.add(element.generateTag());
        }
        pTag.put("primitives",elementListTag);
    }


}
