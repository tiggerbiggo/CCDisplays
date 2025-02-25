package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.holoprojector.HoloProjectorBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    private float scale = 0.01f;
    private List<HoloTextElement> elementList = new ArrayList<>();

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.context = context;
    }

    private void drawText(String text, float x, float y, float z, float rotation, float scale, int color, PoseStack pPoseStack, MultiBufferSource pBuffer) {

        pPoseStack.pushPose();
        pPoseStack.translate(x,y,z);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-180));
        pPoseStack.scale(scale,scale,scale);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(
                text,
                0,
                0,
                color,
                false,
                pPoseStack.last().pose(),
                pBuffer,
                Font.DisplayMode.NORMAL,
                0x00AA00,
                0xF000F0
        );

        pPoseStack.popPose();

    }

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();

        elementList = blockEntity.getElementList();

        if(elementList != null && !elementList.isEmpty())
        {
            for(HoloTextElement element: elementList)
            {
                drawText(element.getText(), element.getXPos(), element.getYPos(), element.getZPos(), element.getRotation(), scale * element.getScale(), element.getColor(), poseStack, multiBufferSource);
            }
        }

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
