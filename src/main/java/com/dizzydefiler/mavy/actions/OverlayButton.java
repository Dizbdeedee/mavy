package com.dizzydefiler.mavy.actions;

import codechicken.nei.LayoutManager;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import com.dizzydefiler.mavy.Mavy;
import com.dizzydefiler.mavy.render.MavyRender;
import com.dizzydefiler.mavy.util.PA;
import com.dizzydefiler.mavy.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

public class OverlayButton extends Base {

    int id;

    public OverlayButton(int id,int rectX, int rectY) {
        this.id = id;
        setMavyRender(new MavyRender(rectX,rectY,this));
    }

    @Override
    public void action() {
        if (!(Util.client.currentScreen instanceof GuiRecipe)) return;
        GuiRecipe gr = (GuiRecipe) Util.client.currentScreen;
        switch (Mavy.currentState) {
            case CRAFT:
            case USAGE:
                PA._GuiRecipe.updateValues(gr);
                int recipe = gr.page * PA._GuiRecipe.getRecipiesPerPage(gr) + id - PA._GuiRecipe.OVERLAY_BUTTON_ID_START;
                GuiContainer firstGui = gr.getFirstScreen();

                final IRecipeOverlayRenderer renderer = PA._GuiRecipe.handler.getOverlayRenderer(firstGui, recipe);
                final IOverlayHandler overlayHandler = PA._GuiRecipe.handler.getOverlayHandler(firstGui, recipe);
                if (PA._GuiRecipe.handler != null) {
                    Minecraft.getMinecraft().displayGuiScreen(firstGui);
                    overlayHandler.overlayRecipe(firstGui, gr.currenthandlers.get(gr.recipetype), recipe, true);
                } else if (renderer != null) {
                    Minecraft.getMinecraft().displayGuiScreen(firstGui);
                    LayoutManager.overlayRenderer = renderer;
                }
                Mavy.drawhandler.finishAction();
                break;
        }
//        page * recipesPerPage + guibutton.id - OVERLAY_BUTTON_ID_START
    }
}
