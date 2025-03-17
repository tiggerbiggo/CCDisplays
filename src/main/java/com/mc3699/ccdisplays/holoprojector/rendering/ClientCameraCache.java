package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Holds the client's camera matrix and performs calculations allowing text to render properly on both sides.
 * DOES NOT CURRENTLY WORK
 * Maybe there's a better way?
 */
public class ClientCameraCache {
    private static Vec3 cameraPosition = Vec3.ZERO;

    /**
     * Updates the cached camera position. Should be called once per frame.
     * @param cameraPos The current camera position
     */
    public static void updateCameraPosition(Vec3 cameraPos) {
        cameraPosition = cameraPos;
    }

    /**
     * Determines if the text at the given pose stack should be flipped to face the camera.
     * @param poseStack The current pose stack containing the text position and rotation
     * @return true if the text should be flipped, false otherwise
     */
    public static boolean shouldFlipText(PoseStack poseStack) {
        // Get the current position of the text in world space
        Matrix4f modelMatrix = poseStack.last().pose();
        Vector4f origin = new Vector4f(0, 0, 0, 1);

        // Transform the origin point using the model matrix
        // Using the correct method for Minecraft's Vector4f class
        float x = modelMatrix.m00() * origin.x() + modelMatrix.m01() * origin.y() + modelMatrix.m02() * origin.z() + modelMatrix.m03() * origin.w();
        float y = modelMatrix.m10() * origin.x() + modelMatrix.m11() * origin.y() + modelMatrix.m12() * origin.z() + modelMatrix.m13() * origin.w();
        float z = modelMatrix.m20() * origin.x() + modelMatrix.m21() * origin.y() + modelMatrix.m22() * origin.z() + modelMatrix.m23() * origin.w();

        Vec3 textPos = new Vec3(x, y, z);

        // Calculate direction from text to camera
        Vec3 viewDirection = cameraPosition.subtract(textPos);

        // Extract the current rotation from the pose stack
        Matrix3f normalMatrix = poseStack.last().normal();
        Vec3 textNormal = new Vec3(0, 0, 1); // Assuming text faces +Z by default
        textNormal = new Vec3(
                normalMatrix.m00() * textNormal.x + normalMatrix.m01() * textNormal.y + normalMatrix.m02() * textNormal.z,
                normalMatrix.m10() * textNormal.x + normalMatrix.m11() * textNormal.y + normalMatrix.m12() * textNormal.z,
                normalMatrix.m20() * textNormal.x + normalMatrix.m21() * textNormal.y + normalMatrix.m22() * textNormal.z
        );

        // Return true if the text is facing away from the camera
        return textNormal.dot(viewDirection) < 0;
    }

    /**
     * Applies the necessary transformations to make the text face the camera.
     * @param poseStack The pose stack to modify
     */
    public static void applyTextFlipping(PoseStack poseStack) {
        if (shouldFlipText(poseStack)) {
            // Save the current transformation
            Matrix4f currentTransform = new Matrix4f(poseStack.last().pose());

            // Reset the pose stack
            poseStack.popPose();
            poseStack.pushPose();

            // Apply a 180-degree rotation around the Y axis
            poseStack.mulPose(Axis.YP.rotationDegrees(180));

            // Apply the original transformation
            poseStack.mulPoseMatrix(currentTransform);
        }
    }
}