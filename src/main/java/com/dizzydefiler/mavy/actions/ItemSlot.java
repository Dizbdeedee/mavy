package com.dizzydefiler.mavy.actions;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import com.dizzydefiler.mavy.InventoryMove;
import com.dizzydefiler.mavy.Mavy;
import com.dizzydefiler.mavy.render.MavyRender;
import com.dizzydefiler.mavy.MavyState;
import com.dizzydefiler.mavy.render.MavySlotRender;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

import java.util.ArrayList;
import java.util.Arrays;


public class ItemSlot extends Base {
    Slot sl;

    public ItemSlot(Slot sl, float rectX, float rectY) {
        setMavyRender(new MavySlotRender(rectX, rectY, this,sl));
        this.sl = sl;
    }

    @Override
    public void action() {
        switch (Mavy.currentState) {
            case INITAL_SWAP:
                Mavy.drawhandler.nextAction(MavyState.SELECTED_SWAP);
                Mavy.currentState.setSelect(sl);
                break;
            case INITAL_MOVE:
                Mavy.drawhandler.nextAction(MavyState.SELECTED_MOVE);
                Mavy.currentState.setSelect(sl);

                break;
            case CRAFT:
                GuiCraftingRecipe.openRecipeGui("item", sl.getStack().copy());
                Mavy.drawhandler.finishAction();
                break;
            case USAGE:
                GuiUsageRecipe.openRecipeGui("item", sl.getStack().copy());
                Mavy.drawhandler.finishAction();

                break;
            case SELECTED_MOVE:
                Slot selected = Mavy.currentState.getSelect();
                if (selected instanceof SlotCrafting) {
                    if (Mavy.currentState.getPrefix() != 0) {
                        int provides = selected.getStack().stackSize;
                        int target = Mavy.currentState.getPrefix();
                        boolean remainder = target % provides != 0;
                        int clicks = target / provides;
                        if (remainder) clicks++;
                        InventoryMove.moveCraft(selected.slotNumber,sl.slotNumber,clicks);
                    } else {
                        InventoryMove.moveCraft(selected.slotNumber,sl.slotNumber,1);
                    }
                } else {
                    if (Mavy.currentState.getPrefix() != 0) {
                        InventoryMove.move(selected.slotNumber,sl.slotNumber,Mavy.currentState.getPrefix());
                    } else {
                        InventoryMove.moveAll(Mavy.currentState.getSelect().slotNumber,sl.slotNumber);
                    }
                }

                Mavy.drawhandler.finishAction(MavyState.INITAL_MOVE);
                break;
            case SELECTED_SWAP:
                InventoryMove.swap(Mavy.currentState.getSelect().slotNumber,sl.slotNumber);
                Mavy.drawhandler.finishAction(MavyState.INITAL_SWAP);
                break;
            case SHIFT:
                InventoryMove.shiftClick(sl.slotNumber);
                Mavy.drawhandler.finishActionNoRefresh();
                break;
            case DROP:
                InventoryMove.drop(sl.slotNumber);
                Mavy.drawhandler.finishActionNoRefresh();
                break;
            case INITIAL_DISTRIBUTE:
                Mavy.drawhandler.nextAction(MavyState.DISTRIBUTE);
                Mavy.currentState.setSelect(sl);
                break;
            case DISTRIBUTE:
                Mavy.noRefresh = true;

                Mavy.currentState.distributeList.add(sl);

                ArrayList<Integer> ar = new ArrayList<Integer>();
                ArrayList<Integer> remainder = new ArrayList<Integer>();
                remainder.add(Mavy.currentState.getSelect().slotNumber);
                for (Slot slot2 : Mavy.currentState.distributeList) {

                    ar.add(slot2.slotNumber);
                }
                Integer targetSize;
                if (Mavy.currentState.getPrefix() == 0) {
                    targetSize = null;
                } else {
                    targetSize = Mavy.currentState.getPrefix();
                }
                if (!InventoryMove.distributeRemain(ar,remainder,targetSize)) {
                    Mavy.drawhandler.finishAction();
                }

                Mavy.drawhandler.nextActionNoRefresh(MavyState.DISTRIBUTE);
                break;



        }
    }
}
