package com.mc3699.ccdisplays.holoprojector.rendering.primitive;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PrimitiveDrawMethods {

    public static void addVertex(VertexConsumer builder, PoseStack poseStack, int combinedLight, float x, float y, float z, float nx, float ny, float nz, int red, int green, int blue, int alpha)
    {
        Matrix4f matrix = poseStack.last().pose();
        builder.vertex(matrix, x, y, z)
                .color(red, green, blue, alpha)
                .uv(0.0f, 0.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(nx, ny, nz)
                .endVertex();
    }



    // I generated these with Grok3, there's no way I'm writing all that lol

    public static void drawCube(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, int red, int green, int blue, int alpha) {
        float halfSize = 1 / 2.0F;
        float minX = -halfSize;
        float maxX = halfSize;
        float minY = -halfSize;
        float maxY = halfSize;
        float minZ = -halfSize;
        float maxZ = halfSize;

        // Front face (z = minZ, normal -z)
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, minZ, 0.0f, 0.0f, -1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, minZ, 0.0f, 0.0f, -1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, minZ, 0.0f, 0.0f, -1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, minZ, 0.0f, 0.0f, -1.0f, red, green, blue, alpha);

        // Back face (z = maxZ, normal +z)
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, maxZ, 0.0f, 0.0f, 1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, maxZ, 0.0f, 0.0f, 1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, maxZ, 0.0f, 0.0f, 1.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, maxZ, 0.0f, 0.0f, 1.0f, red, green, blue, alpha);

        // Left face (x = minX, normal -x)
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, minZ, -1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, minZ, -1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, maxZ, -1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, maxZ, -1.0f, 0.0f, 0.0f, red, green, blue, alpha);

        // Right face (x = maxX, normal +x)
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, minZ, 1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, maxZ, 1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, maxZ, 1.0f, 0.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, minZ, 1.0f, 0.0f, 0.0f, red, green, blue, alpha);

        // Bottom face (y = minY, normal -y)
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, minZ, 0.0f, -1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, minY, maxZ, 0.0f, -1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, maxZ, 0.0f, -1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, minY, minZ, 0.0f, -1.0f, 0.0f, red, green, blue, alpha);

        // Top face (y = maxY, normal +y)
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, minZ, 0.0f, 1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, minZ, 0.0f, 1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, maxX, maxY, maxZ, 0.0f, 1.0f, 0.0f, red, green, blue, alpha);
        addVertex(vertexConsumer, poseStack, combinedLight, minX, maxY, maxZ, 0.0f, 1.0f, 0.0f, red, green, blue, alpha);
    }

    public static void drawSphere(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, int red, int green, int blue, int alpha) {
        int numStacks = 10;
        int numSectors = 20;
        float pi = (float)Math.PI;
        float twoPi = 2 * pi;

        for(int i = 0; i < numStacks; i++) {
            float phi_i = (i / (float)numStacks) * pi;
            float phi_i1 = ((i+1) / (float)numStacks) * pi;
            for(int j = 0; j < numSectors; j++) {
                float theta_j = (j / (float)numSectors) * twoPi;
                float theta_j1 = ((j+1) % numSectors / (float)numSectors) * twoPi;

                // Vertex A: stack i, sector j
                float x_A = (float) (sin(phi_i) * cos(theta_j));
                float y_A = (float) (sin(phi_i) * sin(theta_j));
                float z_A = (float) (cos(phi_i));
                float nz_A = (float) cos(phi_i);

                // Vertex B: stack i+1, sector j
                float x_B = (float) (sin(phi_i1) * cos(theta_j));
                float y_B = (float) (sin(phi_i1) * sin(theta_j));
                float z_B = (float) (cos(phi_i1));
                float nz_B = (float) cos(phi_i1);

                // Vertex C: stack i+1, sector j+1
                float x_C = (float) (sin(phi_i1) * cos(theta_j1));
                float y_C = (float) (sin(phi_i1) * sin(theta_j1));
                float z_C = (float) (cos(phi_i1));
                float nz_C = (float) cos(phi_i1);

                // Vertex D: stack i, sector j+1
                float x_D = (float) (sin(phi_i) * cos(theta_j1));
                float y_D = (float) (sin(phi_i) * sin(theta_j1));
                float z_D = (float) (cos(phi_i));
                float nz_D = (float) cos(phi_i);

                // Add vertices in order A-B-C-D to form a quad
                addVertex(vertexConsumer, poseStack, combinedLight, x_A, y_A, z_A, x_A, y_A, nz_A, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_B, y_B, z_B, x_B, y_B, nz_B, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_C, y_C, z_C, x_C, y_C, nz_C, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_D, y_D, z_D, x_D, y_D, nz_D, red, green, blue, alpha);
            }
        }
    }

    public static void drawCylinder(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, int red, int green, int blue, int alpha) {
        int numSectors = 20;
        float pi = (float)Math.PI;
        float twoPi = 2 * pi;
        float halfHeight = 1 / 2.0F;

        // Calculate bottom and top centers
        float bottomY = - halfHeight;
        float topY = halfHeight;
        Vector3f bottomCenter = new Vector3f(0, bottomY, 0);
        Vector3f topCenter = new Vector3f(0, topY, 0);

        // Calculate bottom and top points
        float[] bottomX = new float[numSectors];
        float[] bottomZ = new float[numSectors];
        float[] topX = new float[numSectors];
        float[] topZ = new float[numSectors];
        for(int j = 0; j < numSectors; j++) {
            float theta = (j / (float)numSectors) * twoPi;
            bottomX[j] = (float) cos(theta);
            bottomZ[j] = (float) sin(theta);
            topX[j] = (float) cos(theta);
            topZ[j] = (float) sin(theta);
        }

        // Render side
        for(int j = 0; j < numSectors; j++) {
            int j1 = (j + 1) % numSectors;

            // Vertex A: bottom[j]
            float x_A = bottomX[j];
            float y_A = bottomY;
            float z_A = bottomZ[j];
            float nx_A = x_A;
            float ny_A = 0;
            float nz_A = z_A;

            // Vertex B: bottom[j1]
            float x_B = bottomX[j1];
            float y_B = bottomY;
            float z_B = bottomZ[j1];
            float nx_B = x_B;
            float ny_B = 0;
            float nz_B = z_B;

            // Vertex C: top[j1]
            float x_C = topX[j1];
            float y_C = topY;
            float z_C = topZ[j1];
            float nx_C = x_C;
            float ny_C = 0;
            float nz_C = z_C;

            // Vertex D: top[j]
            float x_D = topX[j];
            float y_D = topY;
            float z_D = topZ[j];
            float nx_D = x_D;
            float ny_D = 0;
            float nz_D = z_D;

            // Add vertices in order A-B-C-D
            addVertex(vertexConsumer, poseStack, combinedLight, x_A, y_A, z_A, nx_A, ny_A, nz_A, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_B, y_B, z_B, nx_B, ny_B, nz_B, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_C, y_C, z_C, nx_C, ny_C, nz_C, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_D, y_D, z_D, nx_D, ny_D, nz_D, red, green, blue, alpha);
        }

        // Render bottom base
        for(int j = 0; j < numSectors; j++) {
            int j1 = (j + 1) % numSectors;

            // Vertex A: bottomCenter
            float x_A = bottomCenter.x();
            float y_A = bottomCenter.y();
            float z_A = bottomCenter.z();
            float nx_A = 0;
            float ny_A = -1;
            float nz_A = 0;

            // Vertex B: bottom[j]
            float x_B = bottomX[j];
            float y_B = bottomY;
            float z_B = bottomZ[j];
            float nx_B = 0;
            float ny_B = -1;
            float nz_B = 0;

            // Vertex C: bottom[j1]
            float x_C = bottomX[j1];
            float y_C = bottomY;
            float z_C = bottomZ[j1];
            float nx_C = 0;
            float ny_C = -1;
            float nz_C = 0;

            // Vertex D: bottomCenter
            float x_D = bottomCenter.x();
            float y_D = bottomCenter.y();
            float z_D = bottomCenter.z();
            float nx_D = 0;
            float ny_D = -1;
            float nz_D = 0;

            // Add vertices in order A-B-C-D
            addVertex(vertexConsumer, poseStack, combinedLight, x_A, y_A, z_A, nx_A, ny_A, nz_A, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_B, y_B, z_B, nx_B, ny_B, nz_B, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_C, y_C, z_C, nx_C, ny_C, nz_C, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_D, y_D, z_D, nx_D, ny_D, nz_D, red, green, blue, alpha);
        }

        // Render top base
        for(int j = 0; j < numSectors; j++) {
            int j1 = (j + 1) % numSectors;

            // Vertex A: topCenter
            float x_A = topCenter.x();
            float y_A = topCenter.y();
            float z_A = topCenter.z();
            float nx_A = 0;
            float ny_A = 1;
            float nz_A = 0;

            // Vertex B: top[j]
            float x_B = topX[j];
            float y_B = topY;
            float z_B = topZ[j];
            float nx_B = 0;
            float ny_B = 1;
            float nz_B = 0;

            // Vertex C: top[j1]
            float x_C = topX[j1];
            float y_C = topY;
            float z_C = topZ[j1];
            float nx_C = 0;
            float ny_C = 1;
            float nz_C = 0;

            // Vertex D: topCenter
            float x_D = topCenter.x();
            float y_D = topCenter.y();
            float z_D = topCenter.z();
            float nx_D = 0;
            float ny_D = 1;
            float nz_D = 0;

            // Add vertices in order A-B-C-D
            addVertex(vertexConsumer, poseStack, combinedLight, x_A, y_A, z_A, nx_A, ny_A, nz_A, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_B, y_B, z_B, nx_B, ny_B, nz_B, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_C, y_C, z_C, nx_C, ny_C, nz_C, red, green, blue, alpha);
            addVertex(vertexConsumer, poseStack, combinedLight, x_D, y_D, z_D, nx_D, ny_D, nz_D, red, green, blue, alpha);
        }
    }


}
