package com.mc3699.ccdisplays.holoprojector.rendering.offset;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class PlayerBindings {
    //Maps player names to lists of binding modifiers to apply based on that player
    private final Map<String, List<BindingModifier>> bindingMap = new HashMap<>();

    public CompoundTag generateTag(){
        CompoundTag tag = new CompoundTag();

        for(String playerName : bindingMap.keySet()){
            List<BindingModifier> modifiers = bindingMap.get(playerName);
            ListTag modList = new ListTag();
            for(BindingModifier modifier : modifiers){
                modList.add(StringTag.valueOf(modifier.name()));
            }

            tag.put(playerName, modList);
        }

        return tag;
    }

    public static PlayerBindings fromTag(CompoundTag bindingTag) {
        PlayerBindings bindings = new PlayerBindings();
        Set<String> nameList = bindingTag.getAllKeys();
        for(String playerName : nameList){
            ListTag modList = bindingTag.getList(playerName, ListTag.TAG_STRING);
            for(int i = 0; i < modList.size(); i++) {
                String modifierName = modList.getString(i);
                try {
                    BindingModifier modifier = BindingModifier.valueOf(modifierName);
                    bindings.addModifier(playerName, modifier);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
        return bindings;
    }


    public void addModifier(String playerName, BindingModifier modifier) {
        List<BindingModifier> modifiers = bindingMap.computeIfAbsent(playerName, k -> new ArrayList<>());
        modifiers.add(modifier);
    }

    public void applyModifiers(PoseStack poseStack, float partialTicks, Vec3 holoPos){
        for(String playerName : bindingMap.keySet()){
            for(BindingModifier modifier : bindingMap.get(playerName)){
                modifier.applyTransform(playerName, poseStack, partialTicks, holoPos);
            }
        }
    }
}
