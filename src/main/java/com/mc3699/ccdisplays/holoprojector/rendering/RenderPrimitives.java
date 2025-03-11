package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RenderPrimitives {

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

    public static void drawCube(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, double xCenterWorld, double yCenterWorld, double zCenterWorld, int red, int green, int blue, int alpha, float size) {
        float halfSize = size / 2.0F;
        float minX = (float)xCenterWorld - halfSize;
        float maxX = (float)xCenterWorld + halfSize;
        float minY = (float)yCenterWorld - halfSize;
        float maxY = (float)yCenterWorld + halfSize;
        float minZ = (float)zCenterWorld - halfSize;
        float maxZ = (float)zCenterWorld + halfSize;

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

    public static void drawSphere(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, float xCenterWorld, float yCenterWorld, float zCenterWorld, float radius, int red, int green, int blue, int alpha) {
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
                float x_A = (float) (xCenterWorld + radius * sin(phi_i) * cos(theta_j));
                float y_A = (float) (yCenterWorld + radius * sin(phi_i) * sin(theta_j));
                float z_A = (float) (zCenterWorld + radius * cos(phi_i));
                float nx_A = (float) (sin(phi_i) * cos(theta_j));
                float ny_A = (float) (sin(phi_i) * sin(theta_j));
                float nz_A = (float) cos(phi_i);

                // Vertex B: stack i+1, sector j
                float x_B = (float) (xCenterWorld + radius * sin(phi_i1) * cos(theta_j));
                float y_B = (float) (yCenterWorld + radius * sin(phi_i1) * sin(theta_j));
                float z_B = (float) (zCenterWorld + radius * cos(phi_i1));
                float nx_B = (float) (sin(phi_i1) * cos(theta_j));
                float ny_B = (float) (sin(phi_i1) * sin(theta_j));
                float nz_B = (float) cos(phi_i1);

                // Vertex C: stack i+1, sector j+1
                float x_C = (float) (xCenterWorld + radius * sin(phi_i1) * cos(theta_j1));
                float y_C = (float) (yCenterWorld + radius * sin(phi_i1) * sin(theta_j1));
                float z_C = (float) (zCenterWorld + radius * cos(phi_i1));
                float nx_C = (float) (sin(phi_i1) * cos(theta_j1));
                float ny_C = (float) (sin(phi_i1) * sin(theta_j1));
                float nz_C = (float) cos(phi_i1);

                // Vertex D: stack i, sector j+1
                float x_D = (float) (xCenterWorld + radius * sin(phi_i) * cos(theta_j1));
                float y_D = (float) (yCenterWorld + radius * sin(phi_i) * sin(theta_j1));
                float z_D = (float) (zCenterWorld + radius * cos(phi_i));
                float nx_D = (float) (sin(phi_i) * cos(theta_j1));
                float ny_D = (float) (sin(phi_i) * sin(theta_j1));
                float nz_D = (float) cos(phi_i);

                // Add vertices in order A-B-C-D to form a quad
                addVertex(vertexConsumer, poseStack, combinedLight, x_A, y_A, z_A, nx_A, ny_A, nz_A, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_B, y_B, z_B, nx_B, ny_B, nz_B, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_C, y_C, z_C, nx_C, ny_C, nz_C, red, green, blue, alpha);
                addVertex(vertexConsumer, poseStack, combinedLight, x_D, y_D, z_D, nx_D, ny_D, nz_D, red, green, blue, alpha);
            }
        }
    }

    public static void drawCylinder(VertexConsumer vertexConsumer, PoseStack poseStack, int combinedLight, float xCenterWorld, float yCenterWorld, float zCenterWorld, float radius, float height, int red, int green, int blue, int alpha) {
        int numSectors = 20;
        float pi = (float)Math.PI;
        float twoPi = 2 * pi;
        float halfHeight = height / 2.0F;

        // Calculate bottom and top centers
        float bottomY = yCenterWorld - halfHeight;
        float topY = yCenterWorld + halfHeight;
        Vector3f bottomCenter = new Vector3f(xCenterWorld, bottomY, zCenterWorld);
        Vector3f topCenter = new Vector3f(xCenterWorld, topY, zCenterWorld);

        // Calculate bottom and top points
        float[] bottomX = new float[numSectors];
        float[] bottomZ = new float[numSectors];
        float[] topX = new float[numSectors];
        float[] topZ = new float[numSectors];
        for(int j = 0; j < numSectors; j++) {
            float theta = (j / (float)numSectors) * twoPi;
            bottomX[j] = (float) (xCenterWorld + radius * cos(theta));
            bottomZ[j] = (float) (zCenterWorld + radius * sin(theta));
            topX[j] = (float) (xCenterWorld + radius * cos(theta));
            topZ[j] = (float) (zCenterWorld + radius * sin(theta));
        }

        // Render side
        for(int j = 0; j < numSectors; j++) {
            int j1 = (j + 1) % numSectors;

            // Vertex A: bottom[j]
            float x_A = bottomX[j];
            float y_A = bottomY;
            float z_A = bottomZ[j];
            float nx_A = (x_A - xCenterWorld) / radius;
            float ny_A = 0;
            float nz_A = (z_A - zCenterWorld) / radius;

            // Vertex B: bottom[j1]
            float x_B = bottomX[j1];
            float y_B = bottomY;
            float z_B = bottomZ[j1];
            float nx_B = (x_B - xCenterWorld) / radius;
            float ny_B = 0;
            float nz_B = (z_B - zCenterWorld) / radius;

            // Vertex C: top[j1]
            float x_C = topX[j1];
            float y_C = topY;
            float z_C = topZ[j1];
            float nx_C = (x_C - xCenterWorld) / radius;
            float ny_C = 0;
            float nz_C = (z_C - zCenterWorld) / radius;

            // Vertex D: top[j]
            float x_D = topX[j];
            float y_D = topY;
            float z_D = topZ[j];
            float nx_D = (x_D - xCenterWorld) / radius;
            float ny_D = 0;
            float nz_D = (z_D - zCenterWorld) / radius;

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
