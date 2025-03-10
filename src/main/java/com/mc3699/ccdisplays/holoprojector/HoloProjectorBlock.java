package com.mc3699.ccdisplays.holoprojector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HoloProjectorBlock extends Block implements EntityBlock {
    public HoloProjectorBlock() {
        super(BlockBehaviour.Properties.of().
                sound(SoundType.METAL).
                strength(0.5f, 1200f)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HoloProjectorBlockEntity(blockPos,blockState);
    }

}
