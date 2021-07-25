package com.dizzydefiler.mavy.actions;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import com.dizzydefiler.mavy.Mavy;
import com.dizzydefiler.mavy.render.MavyRender;
import net.minecraft.item.ItemStack;

public class Item extends Base {

    ItemStack is;

    public Item(ItemStack is, float rectX, float rectY) {
        setMavyRender(new MavyRender(rectX, rectY, this));
        this.is = is;
    }

    @Override
    public void action() {
        switch (Mavy.currentState) {

            case CRAFT:
                GuiCraftingRecipe.openRecipeGui("item", is.copy());
                Mavy.drawhandler.finishAction();
                Mavy.currentState.refreshRequested = true; //different candidates

                break;
            case USAGE:
                GuiUsageRecipe.openRecipeGui("item", is.copy());
                Mavy.drawhandler.finishAction();
                Mavy.currentState.refreshRequested = true; //different candidates

                break;

        }
    }
}