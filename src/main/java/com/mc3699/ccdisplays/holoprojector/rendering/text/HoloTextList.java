package com.mc3699.ccdisplays.holoprojector.rendering.text;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;

public class HoloTextList {
    private final List<HoloTextElement> elements;

    public HoloTextList() {
        elements = new ArrayList<>();
    }

    public void add(HoloTextElement element) {
        elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    public void set(int index, HoloTextElement element) {
        elements.set(index, element);
    }

    public HoloTextElement get(int index) {
        return elements.get(index);
    }

    public void clear() {
        elements.clear();
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public List<HoloTextElement> elements(){
        return elements;
    }

    public void saveAdditional(CompoundTag pTag) {

        ListTag elementListTag = new ListTag();

        for(HoloTextElement element :elements) {
            elementListTag.add(element.generateTag());
        }
        pTag.put("text",elementListTag);
    }
}
