package com.dizzydefiler.mavy;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public enum MavyState {

    SELECTED_MOVE,
    SELECTED_SWAP,
    INITAL_MOVE,
    INITAL_SWAP,
    CRAFT,
    USAGE,
    SHIFT,
    INFO,
    MASTER,
    INITIAL_DISTRIBUTE,
    DISTRIBUTE,
    PULL,
    PUSH,
    DROP,
    NONE;

    Slot select = null;

    static int prefix = 0;

    public ArrayList<Slot> distributeList = new ArrayList<Slot>();

    public boolean refreshRequested = false;

    public Slot getSelect() {
        return select;
    }

    public void setSelect(Slot sl) {
        this.select = sl;
    }

    public void setPrefix(int prefix) {
        MavyState.prefix = prefix;
    }

    public int getPrefix() {
        return prefix;
    }
}
