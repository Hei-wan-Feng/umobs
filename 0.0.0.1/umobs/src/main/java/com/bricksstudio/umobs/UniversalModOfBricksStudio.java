package com.bricksstudio.umobs;

import com.bricksstudio.umobs.block.ModBlocks;
import com.bricksstudio.umobs.config.ModConfig;
import com.bricksstudio.umobs.event.GameRuleHandlers;
import com.bricksstudio.umobs.event.QuickBridgeHandler;
import com.bricksstudio.umobs.item.ModItems;
import com.bricksstudio.umobs.recipe.ModRecipes;
import com.bricksstudio.umobs.screen.ModConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("umobs")
public class UniversalModOfBricksStudio {
    public static final String MOD_ID = "umobs";
    public static final Logger LOGGER = LogManager.getLogger();

    private final ModLoadingContext modLoadingContext;
    
    public static float SERVER_TICK_RATE = 20.0f;
    public static boolean IS_TICK_FROZEN = false;
    public static boolean QUICK_BRIDGE_ENABLED = true;

    public UniversalModOfBricksStudio(FMLJavaModLoadingContext fmlContext, ModLoadingContext modLoadingContext) {
        this.modLoadingContext = modLoadingContext;
        IEventBus modEventBus = fmlContext.getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(GameRuleHandlers.class);
        MinecraftForge.EVENT_BUS.register(QuickBridgeHandler.class);
        
        this.modLoadingContext.registerConfig(Type.COMMON, ModConfig.SPEC, "umobs-common.toml");
        
        // 注册方块、物品和配方
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Universal Mod of Bricks Studio 初始化完成！");
        
        event.enqueueWork(() -> {
            com.bricksstudio.umobs.network.ChatPacketHandler.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            this.modLoadingContext.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (mc, parent) -> new ModConfigScreen(parent)
                )
            );
        }
    }
}