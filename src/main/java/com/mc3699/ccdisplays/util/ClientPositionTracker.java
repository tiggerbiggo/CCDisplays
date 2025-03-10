package com.mc3699.ccdisplays.util;


import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class ClientPositionTracker {

/*    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase != TickEvent.Phase.END){
                return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null || minecraft.player == null){
            return;
        }

        String currentPlayerName = minecraft.player.getGameProfile().getName();
        localPositions.put(currentPlayerName, minecraft.player.position());

        for(Player player : minecraft.level.players()){
            String playerName = player.getGameProfile().getName();
            localPositions.put(playerName, player.position());
        }
    }*/

    //Position data is all on the Entity class, which both LocalPlayer and Player ultimately extend

    public static Vec3 lerp3(Vec3 a, Vec3 b, float o){
        return new Vec3(
                a.x + (b.x - a.x) * o,
                a.y + (b.y - a.y) * o,
                a.z + (b.z - a.z) * o
        );
    }

    public static Vec3 interpolatePosition(Entity player, float partialTicks){
        return lerp3(
                new Vec3(player.xo, player.yo, player.zo),
                player.position(),
                partialTicks
        );
    }

    private static Entity getPlayerEntity(String username){
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;

        // If it's the client player, use their interpolated position
        if (minecraft.player != null && minecraft.player.getGameProfile().getName().equals(username)) {
            return minecraft.player;
        }

        // For other players, find them and get their interpolated position
        for (Player player : minecraft.level.players()) {
            if (player.getGameProfile().getName().equals(username)) {
                return player;
            }
        }

        return null;
    }

    @Nullable
    public static Vec3 getInterpolatedPlayerPosition(String username, float partialTicks) {
        Entity playerEntity = getPlayerEntity(username);
        if(playerEntity != null) {
            return interpolatePosition(playerEntity, partialTicks);
        }
        return null;
    }

    public static Vec2 getInterpolatedViewRotation(String username, float partialTicks){
        Entity playerEntity = getPlayerEntity(username);
        if(playerEntity != null) {
            return new Vec2(
                    playerEntity.getViewXRot(partialTicks),
                    playerEntity.getViewYRot(partialTicks)
            );
        }
        return null;
    }

    public static float getEyeHeight(String username){
        Entity playerEntity = getPlayerEntity(username);
        if(playerEntity != null) {
            return playerEntity.getEyeHeight();
        }
        return 0;
    }
}
