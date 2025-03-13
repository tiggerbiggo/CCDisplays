package com.mc3699.ccdisplays.graphicsmonitor.rendering;

import com.mc3699.ccdisplays.CCDisplays;
import com.mc3699.ccdisplays.graphicsmonitor.GraphicsMonitorBlockEntity;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;
import org.joml.Random;

public class GraphicsMonitorBlockEntityRenderer implements BlockEntityRenderer<GraphicsMonitorBlockEntity> {


    private final BlockEntityRendererProvider.Context context;
    private boolean init = false;
    ResourceLocation screenTex = new ResourceLocation(CCDisplays.MODID, "monitor");
    DynamicTexture texture = new DynamicTexture(32, 32, true);
    private Random random = new Random();

    public GraphicsMonitorBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.context = context;
        Minecraft.getInstance().getTextureManager().register(screenTex, texture);
    }

    public void renderScreen(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay)
    {

        poseStack.pushPose();
        poseStack.scale(0.00343f,0.00344f,0.003f);
        poseStack.translate(17.5f,17,-1f);


        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                    texture.getPixels().setPixelRGBA(y,x,0xFF333333*random.nextInt(0xFFFFF00)); // random static
            }
        }
        texture.upload();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(screenTex));

        Matrix4f matrix = poseStack.last().pose();

        vertexConsumer.vertex(matrix, 0, 0, 0)
                .color(255, 255, 255, 255)
                .uv(0.0f, 1.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(0.0f, 0.0f, 0.0f)
                .endVertex();
        vertexConsumer.vertex(matrix, 256, 0, 0)
                .color(255, 255, 255, 255)
                .uv(0.0f, 0.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(0.0f, 0.0f, 0.0f)
                .endVertex();
        vertexConsumer.vertex(matrix, 256, 256, 0)
                .color(255, 255, 255, 255)
                .uv(1.0f, 0.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(0.0f, 0.0f, 0.0f)
                .endVertex();
        vertexConsumer.vertex(matrix, 0, 256, 0)
                .color(255, 255, 255, 255)
                .uv(1.0f, 1.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(0.0f, 0.0f, 0.0f)
                .endVertex();

        poseStack.popPose();
    }


    @Override
    public void render(GraphicsMonitorBlockEntity graphicsMonitorBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        renderScreen(poseStack, multiBufferSource, i, i1);
    }
}
