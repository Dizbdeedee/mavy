package com.dizzydefiler.mavy.config;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;

import java.util.Set;

public class ModGuiFactory implements IModGuiFactory {

    @Override
    public Class mainConfigGuiClass() {
        return MavyConfigScreen.class;
    }



    public void initialize(Minecraft minecraftInstance) {}

    public Set runtimeGuiCategories() {
        return null;
    }

    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }


}


