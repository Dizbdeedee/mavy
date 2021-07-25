package com.dizzydefiler.mavy.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class MavyConfigScreen extends GuiConfig {

    public MavyConfigScreen(GuiScreen parentScreen) {
        super(parentScreen, (new ConfigElement(Config.config.getCategory("mavy"))).getChildElements(), "mavy", false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
    }

}