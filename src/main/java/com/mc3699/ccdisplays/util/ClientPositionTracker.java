package com.mc3699.ccdisplays.util;


import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class ClientPositionTracker {
    private static final Map<String, Vec3> localPositions = new HashMap<>();

    @SubscribeEvent
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
    }

    public static Vec3 getInterpolatedPlayerPosition(String username, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return localPositions.get(username);

        // If it's the client player, use their interpolated position
        if (minecraft.player != null && minecraft.player.getGameProfile().getName().equals(username)) {
            return new Vec3(
                    minecraft.player.xo + (minecraft.player.getX() - minecraft.player.xo) * partialTicks,
                    minecraft.player.yo + (minecraft.player.getY() - minecraft.player.yo) * partialTicks,
                    minecraft.player.zo + (minecraft.player.getZ() - minecraft.player.zo) * partialTicks
            );
        }

        // For other players, find them and get their interpolated position
        for (Player player : minecraft.level.players()) {
            if (player.getGameProfile().getName().equals(username)) {
                return new Vec3(
                        player.xo + (player.getX() - player.xo) * partialTicks,
                        player.yo + (player.getY() - player.yo) * partialTicks,
                        player.zo + (player.getZ() - player.zo) * partialTicks
                );
            }
        }

        // Fall back to stored position if available
        return localPositions.get(username);
    }
}
