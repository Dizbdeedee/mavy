package com.dizzydefiler.mavy;

// NEW_1_8 cpw.mods.fml => net.minecraftforge.fml

import codechicken.nei.api.API;
import com.dizzydefiler.mavy.config.Config;
import com.dizzydefiler.mavy.render.KeyFontRenderer;
import com.dizzydefiler.mavy.util.Logger;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * Some Methods are based on CraftingKeys by skate702, which is based on InventoryTweaks.
 *
 * @author dizzydefiler
 */
@Mod(modid = Mavy.MODID, name = Mavy.NAME, version = Mavy.VERSION, useMetadata = true, guiFactory = "com.dizzydefiler.mavy.config.ModGuiFactory")
public class Mavy {

    public static final String MODID = "mavy";
    public static final String VERSION = "0.0.1";
    public static final String NAME = "Mavy";
    public static KeyFontRenderer fontrender;

    public static MavyState currentState = MavyState.NONE;

    public static boolean stickyMode = false;

    public static boolean stickyModeNoRefresh = false;

    public static boolean noRefresh = false;

    public static DrawHandler drawhandler;

    public static ArrayList<Character> keys;

    public static void updateKeys(ArrayList<Character> k) {
        keys = k;
        fontrender.generatePatterns();
    }

    @Instance(value = MODID)
    public static Mavy instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logger.info("preInit(e)", "Loading Config now.");
        Config.loadConfig(event);
        Logger.info("preInit(e)", "Finished loading Config.");
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        // Registering
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        Logger.info("load(e)", "Registered Mod.");

        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().renderEngine == null) {
            throw new RuntimeException();
        }
        fontrender = new KeyFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
        fontrender.onResourceManagerReload(null);
        fontrender.generatePatterns();
        drawhandler = new DrawHandler();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        API.addKeyBind("mavy.recipe", Keyboard.KEY_COMMA);
        API.addKeyBind("mavy.usage",Keyboard.KEY_PERIOD);
        API.addKeyBind("mavy.move",Keyboard.KEY_SLASH);

    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals(MODID)) {
            Config.sync();
            Logger.info("onConfigChanged(e)", "Changed config.");
        }
    }



}
