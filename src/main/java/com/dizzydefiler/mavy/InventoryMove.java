package com.dizzydefiler.mavy;

import com.dizzydefiler.mavy.util.Logger;
import com.dizzydefiler.mavy.util.Util;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Some code below attributed/modififed
 * MIT License
 *
 * Copyright (c) 2020 sebinside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class InventoryMove {


    static Container getContainer() {
        if (Util.client.thePlayer.openContainer != null) {
            return Util.client.thePlayer.openContainer;
        } else {
            throw new RuntimeException("Uh... no!");
        }
    }

    /**
     * Moves a full Stack from a slot to another.
     *
     * @param srcIndex  The Source Slot Index of the Container
     * @param destIndex The Destination Slot Index of the Container
     */
    public static void moveAll(int srcIndex, int destIndex) {

        ItemStack source = getItemStack(srcIndex);

        if (source == null) {
            Logger.debug("moveAll(i,i)", "Source ItemStack from Index == null");
        } else {
            move(srcIndex, destIndex, source.stackSize);
        }

    }

    public static void moveCraft(int craftIndex, int destIndex, int clicks) {
        for (int i = 0; i < clicks; i++) {
            leftClick(craftIndex);
        }
        leftClick(destIndex);
    }

    /**
     * Moves a specified amount of Items from a slot to another. [Based on
     * INVTW]
     *
     * @param srcIndex  The Source Slot Index of the Container
     * @param destIndex The Destination Slot Index of the Container
     * @param amount    The amount of items to move (can be bigger then Stack Size)
     */
    public static void move(int srcIndex, int destIndex, int amount) {


        ItemStack source = getItemStack(srcIndex);
        ItemStack destination = getItemStack(destIndex);
        if (source == null) {
            return;
        } else if (srcIndex == destIndex) {
            return;
        }

        // Test for max. moving Amount
        int sourceSize = source.stackSize;

        int movedAmount = Math.min(amount, sourceSize);
        if (destination != null && source.isItemEqual(destination) && srcIndex >= 0) {
            movedAmount = Math.min(movedAmount,destination.getMaxStackSize() - destination.stackSize);
        }

        // Move some
        if (destination == null || source.isItemEqual(destination)) {

            if (srcIndex >= 0) {
                leftClick(srcIndex);
            }

            for (int i = 0; i < movedAmount; i++) {
                rightClick(destIndex);
            }

            // Move back
            if (movedAmount < sourceSize && srcIndex > 0) {
                leftClick(srcIndex);
            }

            Logger.info("move(i,i,i)", "Moved " + movedAmount + " from " + srcIndex + " to " + destIndex + "!");

        } else {
            Logger.info("move(i,i,i)", "Unable to move!");
        }
    }

    public static void shiftClick(int index) {
        Util.client.playerController.
                windowClick(getContainer().windowId, index, 0, 1, Util.client.thePlayer);
    }

    public static void swap(int srcIndex, int destIndex) {
        // Stacks
        ItemStack source = getItemStack(srcIndex);
        ItemStack destination = getItemStack(destIndex);

        // Same Location?
        if (source == null) {
            return;
        } else if (srcIndex == destIndex) {
            return;
        }

        if (destination != null) {
            Item sourceItem = source.getItem();
            Item destItem = destination.getItem();
            if (sourceItem == destItem) {
                if (source.stackSize > destination.stackSize) {
                    move(srcIndex,destIndex,source.stackSize - destination.stackSize);
                } else if (destination.stackSize > source.stackSize) {
                    move(destIndex,srcIndex,destination.stackSize - source.stackSize);
                } else if (destination.getMaxStackSize() == destination.stackSize
                        && source.getMaxStackSize() == source.stackSize){ //test for unique items that can swap
                    //if fail, then they are the same item with space available and same stacksize, no movement necessary
                    leftClick(srcIndex);
                    leftClick(destIndex);
                    leftClick(srcIndex);
                }
                return;
            }
        }
        leftClick(srcIndex);


        leftClick(destIndex);

        // Move back
        leftClick(srcIndex);

    }

    public static void drop(int srcIndex) {
        if (srcIndex >= 0) {
            leftClick(srcIndex);
        }
        leftClick(-999);
    }

    /**
     * Returns the ItemStack in a slot [Based on INVTW]
     *
     * @param index The index of the slot in the container
     * @return Returns the ItemStack
     */
    static ItemStack getItemStack(int index) {
        Logger.debug("getItemStack(i)","get: " + index);
        if (index >= 0 && index < getContainer().inventorySlots.size()) {

            Slot slot = (Slot) (getContainer().inventorySlots.get(index));
            return (slot == null) ? null : slot.getStack();

        } else if (index == -1 && Util.isHoldingStack()) {
            return Util.getHeldStack();
        } else {

            Logger.debug("getItemStack(i)", "Invalid index");
            return null;

        }

    }

    static void moveToAllSlots(int source, ArrayList<Integer> dests) {
        ItemStack isSource = getItemStack(source);
        if (isSource == null) return;
        for (Integer dist : dests) {
            ItemStack isDest = getItemStack(dist);
            if (isDest != null) {
                int avaliable = isDest.getMaxStackSize() - isDest.stackSize;
                if (isSource.stackSize > avaliable) {
                    move(source,dist,avaliable);
                } else {
                    moveAll(source,dist);
                    break;
                }
            } else {
                moveAll(source,dist);
                break;
            }
        }
    }

    static void moveToAllSlots(int source,int many, ArrayList<Integer> dests) {
        ItemStack isSource = getItemStack(source);
        if (isSource == null) return;
        for (Integer dist : dests) {
            ItemStack isDest = getItemStack(dist);
            if (isDest != null) {
                int avaliable = isDest.getMaxStackSize() - isDest.stackSize;
                if (many > avaliable) {
                    move(source,dist,avaliable);
                } else {
                    move(source,dist,many);
                    break;
                }
            } else {
                move(source,dist,many);
                break;
            }
        }
    }

    public static boolean distributeRemain(ArrayList<Integer> distList, ArrayList<Integer> remainder, Integer setTargetSize) {
        for (Integer remain : remainder) {
            ItemStack isRemain = getItemStack(remain);
            if (isRemain != null) {
                moveToAllSlots(remain, distList);
            }
        }
        int totalStack = getStackSize(distList);
        if (totalStack <= distList.size()) {
            return false;
        }
        int targetSize;
        if (setTargetSize == null) {
            targetSize = totalStack / distList.size();
        } else {
            targetSize = setTargetSize;
        }
        dist(distList,targetSize);
        for (Integer dist: distList) {
            ItemStack isDist = getItemStack(dist);
            if (isDist != null && isDist.stackSize > targetSize) {
                moveToAllSlots(dist,isDist.stackSize - targetSize,remainder);
            }
        }
        return true;
    }

    static int getStackSize(ArrayList<Integer> slots) {
        int totalStack = 0;
        for (Integer index : slots) {
            ItemStack is = getItemStack(index);
            if (is != null) {
//                System.out.println("index: " + index);
                totalStack += is.stackSize;
            }
        }
        return totalStack;
    }

    public static boolean distribute(ArrayList<Integer> distList) {
        int totalStack = getStackSize(distList);
        int targetSize = totalStack / distList.size();
        if (totalStack <= distList.size()) {
            return false;
        }
        dist(distList,targetSize);
        return true;
    }

    static void dist(ArrayList<Integer> distList,int targetSize) {
        ArrayList<Integer> bigger = new ArrayList<Integer>();
        ArrayList<Integer> smaller = new ArrayList<Integer>();
        for (Integer index : distList) {
            ItemStack is = getItemStack(index);
            if (is != null) {
                if (is.stackSize > targetSize) {
                    bigger.add(index);
                } else if (is.stackSize < targetSize) {
                    smaller.add(index);
                }
            } else {
                smaller.add(index);
            }
        }
        boolean satisfied = false;
        for (Integer indexSmall : smaller) {
            ItemStack itemSmall = getItemStack(indexSmall);
            int needed;
            if (itemSmall == null) {
                needed = targetSize;
            } else {
                needed = targetSize - itemSmall.stackSize;
            }
            Logger.info("dist()","needed: " + needed);
            for (Integer indexBig : bigger) {
                ItemStack itemBig = getItemStack(indexBig);
                int avaliable = itemBig.stackSize - targetSize;
                if (avaliable >= needed) {
                    Logger.info("dist()","avaliable: " + needed);

                    move(indexBig,indexSmall,needed);
                    satisfied = true;
                    break;
                } else {
                    move(indexBig,indexSmall,avaliable);
                    needed -= avaliable;
                }
            }
            if (satisfied) break;
        }
    }

    /**
     * Moves a stack (held or not) to the next fitting inventory slot.
     *
     * @param sourceIndex A slot index of the source items
     */
    static void moveStackToInventory(int sourceIndex) {

        // Moving Stack
        ItemStack stackToMove = null;

        // Get the stack, index or held, cleanup held stack
        if (sourceIndex == -1) {
            if (Util.isHoldingStack()) {
                stackToMove = Util.getHeldStack();
            }
        } else {
            stackToMove = getItemStack(sourceIndex);

            // Is there a currently held stack?
            if (Util.isHoldingStack()) {
                moveStackToInventory(-1);
            }
        }

        // Test stack
        if (stackToMove == null) {
            Logger.debug("moveStackToInvetory(i)", "Stack at sourceIndex not found.");
            return;
        }

        // Get destination index
        int destIndex = calcInventoryDestination(stackToMove);

        // Additional click on source index, if not held
        if (sourceIndex != -1) {
            leftClick(sourceIndex);
        }

        // Move the item
        if (destIndex == -1) { // -1 means: Found none, drop item
            leftClick(-999);
            Logger.info("moveStackToInventory(i)", "Dropped item from index " + sourceIndex + ".");
        } else {
            leftClick(destIndex);
            Logger.info("moveStackToInventory(i)", "Moved item from index " + sourceIndex + " to " + destIndex + ".");
        }

    }

    /**
     * Calculates the next fitting or free inventory slot.
     *
     * @param stackToMove The ItemType and sized Stack to move
     * @return A super cool inventory slot index! Or -1, if you are to dumb
     * to keep your bloody inventory sorted! WHY U NO USE INV TWEAKS?!
     */
    private static int calcInventoryDestination(ItemStack stackToMove) {

        // First run: Try to find a nice stack to put items on additionally
        for (int i = 0; i < getContainer().inventorySlots.size(); i++) {

            ItemStack potentialGoalStack = getItemStack(i);

            if (potentialGoalStack != null && stackToMove != null) {
                if (potentialGoalStack.isItemEqual(stackToMove)) {
                    if (potentialGoalStack.stackSize + stackToMove.stackSize <= stackToMove.getMaxStackSize()) {
                        return i;
                    }
                }
            }
        }

        // Second run: Find a free slot
        for (int i = 0; i < getContainer().inventorySlots.size(); i++) {
            if (getItemStack(i) == null) {
                return i;
            }
        }

        // Third run: No slot found. Drop this shit!
        return -1;

    }

    /**
     * Executes a left mouse click on a slot. [Based on INVTW]
     *
     * @param index The index of the slot in the container
     */
    static void leftClick(int index) {
        slotClick(index, false);
    }

    /**
     * Executes a right mouse click on a slot. [Based on INVTW]
     *
     * @param index The index of the slot in the container
     */
    static void rightClick(int index) {
        slotClick(index, true);
    }

    /**
     * Executes a mouse click on a slot. [Based on INVTW]
     *
     * @param index      The index of the slot in the container
     * @param rightClick True, if the click is with the right mouse button
     */
    private static void slotClick(int index, boolean rightClick) {
        int rightClickData;
        if (rightClick) {
            rightClickData = 1;
        } else {
            rightClickData = 0;
        }
        Util.client.playerController.windowClick(getContainer().windowId, index, rightClickData, 0, Util.client.thePlayer);
    }
}
