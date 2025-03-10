package com.mc3699.ccdisplays.holoprojector.rendering;

import com.mc3699.ccdisplays.util.ClientPositionTracker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public enum BindingModifier {
    PLAYER_POSITION{
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            Vec3 pos = ClientPositionTracker.getInterpolatedPlayerPosition(username, partialTicks);
            if(pos != null) {
                pos = pos.subtract(holoPos);
                poseStack.translate(pos.x, pos.y, pos.z);
            }
        }
    },
    HEAD_RELATIVE{
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            float height = ClientPositionTracker.getEyeHeight(username);
            poseStack.translate(0, height, 0);
        }
    },
    HEAD_ROTATION {
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            Vec2 rot = ClientPositionTracker.getInterpolatedViewRotation(username, partialTicks);
            if(rot != null) {
                poseStack.mulPose(Axis.YP.rotationDegrees(-rot.y));
                poseStack.mulPose(Axis.XP.rotationDegrees(rot.x));
            }
        }
    },
    HEAD_ROTX {
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            Vec2 rot = ClientPositionTracker.getInterpolatedViewRotation(username, partialTicks);
            if(rot != null) {
                poseStack.mulPose(Axis.XP.rotationDegrees(rot.x));
            }
        }
    },
    HEAD_ROTY {
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            Vec2 rot = ClientPositionTracker.getInterpolatedViewRotation(username, partialTicks);
            if(rot != null) {
                poseStack.mulPose(Axis.YP.rotationDegrees(-rot.y));
            }
        }
    },
    BODY_ROTATION {
        @Override
        public void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos) {
            //Not implemented yet
        }
    };

    public abstract void applyTransform(String username, PoseStack poseStack, float partialTicks, Vec3 holoPos);
}
