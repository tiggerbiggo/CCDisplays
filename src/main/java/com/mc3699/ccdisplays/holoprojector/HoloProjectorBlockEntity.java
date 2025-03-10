package com.mc3699.ccdisplays.holoprojector;

import com.mc3699.ccdisplays.holoprojector.rendering.BindingModifier;
import com.mc3699.ccdisplays.holoprojector.rendering.HoloOffset;
import com.mc3699.ccdisplays.holoprojector.rendering.HoloTextElement;
import com.mc3699.ccdisplays.holoprojector.rendering.PlayerBindings;
import com.mc3699.ccdisplays.util.ModBlockEntities;
import com.mc3699.ccdisplays.util.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HoloProjectorBlockEntity extends BlockEntity {

    private final List<HoloTextElement> elementList = new ArrayList<>();
    private final HashMap<String, HoloOffset> offsetList = new HashMap<>();

    //Map of offset names to player bindings
    private final HashMap<String, PlayerBindings> bindingMap = new HashMap<>();

    private final HoloProjectorPeripheral peripheral;
    private final LazyOptional peripheralLazyOptional;

    public int addText(HoloTextElement element)
    {
        this.elementList.add(element);
        setChanged();
        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        return this.elementList.size() - 1;
    }

    public boolean replaceText(int index, HoloTextElement element){
        if(index >= 0 && index < elementList.size()) {
            elementList.set(index, element);
            setChanged();
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            return true;
        }
        return false;
    }

    public HoloTextElement getText(int index){
        if(index >= 0 && index < elementList.size()) {
            return elementList.get(index);
        }
        return null;
    }

    public boolean setOffset(String name, HoloOffset offset){
        if (!"".equals(name)){
            offsetList.put(name, offset);
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
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

        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        return true;
    }

    public HashMap<String, PlayerBindings> getBindingMap(){
        return bindingMap;
    }

    public void clearElements()
    {
        this.elementList.clear();
        this.offsetList.clear();
        this.bindingMap.clear();
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public List<HoloTextElement> getElementList() {
        return this.elementList;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        ListTag elementListTag = new ListTag();

        for(HoloTextElement element : elementList)
        {
            elementListTag.add(element.generateTag());
        }
        pTag.put("text", elementListTag);

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

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.elementList.clear();

        if(pTag.contains("text", ListTag.TAG_LIST))
        {
            ListTag textListTag = pTag.getList("text", ListTag.TAG_COMPOUND);
            for(int i = 0; i < textListTag.size(); i++)
            {
                CompoundTag elementTag = textListTag.getCompound(i);
                this.elementList.add(HoloTextElement.fromTag(elementTag));
            }
        }
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

    public HoloProjectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.HOLOGRAM_PROJECTOR.get(), pPos, pBlockState);
        this.peripheral = new HoloProjectorPeripheral(this);
        this.peripheralLazyOptional = LazyOptional.of(() -> this.peripheral);
    }

    public HoloProjectorPeripheral getPeripheral() {
        return this.peripheral;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModCapabilities.PERIPHERAL_CAPABILITY)
        {
            return peripheralLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        peripheralLazyOptional.invalidate();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-128, -128, -128), worldPosition.offset(128, 128, 128));
    }

    public HashMap<String, HoloOffset> getOffsets() {
        return offsetList;
    }
}
