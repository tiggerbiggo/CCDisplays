package com.mc3699.ccdisplays.graphicsmonitor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class GraphicsMonitorBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public GraphicsMonitorBlock() {
        super(BlockBehaviour.Properties.of().
                sound(SoundType.METAL).
                strength(0.5f, 1200f)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GraphicsMonitorBlockEntity(blockPos,blockState);
    }
}
