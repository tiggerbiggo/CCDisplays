package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import com.mc3699.ccdisplays.util.ClientPositionTracker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    private float scale = 0.01f;

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.context = context;
    }

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();

        List<HoloTextElement> elementList = blockEntity.getElementList();
        HashMap<String, HoloOffset> offsets = blockEntity.getOffsets();
        HashMap<String, List<HoloTextElement>> offsetDraws = new HashMap<>();
        HashMap<String, String> playerBindings = blockEntity.getPlayerBindings();
        BlockPos projectorPos = blockEntity.getBlockPos();
        poseStack.pushPose();
        poseStack.translate(0.5,0.5, 0.5); // Render from the center of the block
        if(elementList != null && !elementList.isEmpty())
        {
            for(HoloTextElement element: elementList)
            {
                String offset = element.getOffset();
                if(offset == "") {
                    element.draw(poseStack, multiBufferSource);
                }
                else{
                    List<HoloTextElement> drawList = offsetDraws.get(offset);
                    if(drawList == null){
                        drawList = new ArrayList<>();
                        offsetDraws.put(offset, drawList);
                    }
                    drawList.add(element);
                }
            }
            for(String s : offsetDraws.keySet()){
                HoloOffset offset = offsets.get(s);
                String boundPlayer = playerBindings.get(s);
                if(offset != null){
                    poseStack.pushPose();
                    if(boundPlayer != null){
                        Vec3 playerPos = ClientPositionTracker.getInterpolatedPlayerPosition(boundPlayer, partialTick);
                        if(playerPos != null){
                            double x = playerPos.x - projectorPos.getX();
                            double y = playerPos.y - projectorPos.getY();
                            double z = playerPos.z - projectorPos.getZ();
                            poseStack.translate(x, y, z);
                        }
                    }
                    offset.draw(poseStack, multiBufferSource, offsetDraws.get(s));
                    poseStack.popPose();
                }
            }
        }
        poseStack.popPose();
        RenderSystem.enableDepthTest();

    }

    @Override
    public boolean shouldRenderOffScreen(HoloProjectorBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(HoloProjectorBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;
    }
}
