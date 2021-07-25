package com.dizzydefiler.mavy.render;

import com.dizzydefiler.mavy.actions.IMavyContainer;
import com.dizzydefiler.mavy.util.PA;
import com.dizzydefiler.mavy.util.Util;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class MavySlotRender extends MavyRender {

    Slot sl;

    public MavySlotRender(float rectX, float rectY, IMavyContainer mc, Slot sl) {
        super(rectX, rectY, mc);
        this.sl = sl;
    }

    static int guiLeft;

    static int guiTop;

    public static void updateGuiContainer() {
        if (!(Util.client.currentScreen instanceof GuiContainer)) return;
        GuiContainer guiContainer = (GuiContainer) Util.client.currentScreen;
        PA._GuiContainer.updateValues(guiContainer);
        guiLeft = PA._GuiContainer.guiLeft;
        guiTop = PA._GuiContainer.guiTop;
    }

    @Override
    public void update() {
        setRectX(guiLeft + sl.xDisplayPosition);
        setRectY(guiTop + sl.yDisplayPosition);
    }
}
