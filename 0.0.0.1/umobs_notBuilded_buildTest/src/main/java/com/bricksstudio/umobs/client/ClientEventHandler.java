package com.bricksstudio.umobs.client;

import com.bricksstudio.umobs.client.screen.DisconnectedScreenChat;
import com.bricksstudio.umobs.client.screen.DeathScreenChat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "umobs", value = Dist.CLIENT)
public class ClientEventHandler {
    
    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        // 记录服务器信息
        Minecraft mc = Minecraft.getInstance();
        if (mc.getCurrentServer() != null) {
            String serverInfo = mc.getCurrentServer().name + " (" + mc.getCurrentServer().ip + ")";
            DisconnectedScreenChat.setLastServerInfo(serverInfo);
        } else {
            DisconnectedScreenChat.setLastServerInfo("单人游戏");
        }
        
        // 玩家退出游戏时清空聊天历史
        DisconnectedScreenChat.clearChatHistory();
    }
    
    @SubscribeEvent
    public static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        // 玩家重生时隐藏聊天框
        DeathScreenChat.hideChat();
    }
}