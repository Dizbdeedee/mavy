package com.dizzydefiler.mavy;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.*;
import codechicken.nei.LayoutManager;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerDrawHandler;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.recipe.GuiRecipe;
import com.dizzydefiler.mavy.actions.IMavyContainer;
import com.dizzydefiler.mavy.actions.Item;
import com.dizzydefiler.mavy.actions.ItemSlot;
import com.dizzydefiler.mavy.actions.OverlayButton;
import com.dizzydefiler.mavy.config.Config;
import com.dizzydefiler.mavy.render.KeyFontRenderer;
import com.dizzydefiler.mavy.render.MavyRender;
import com.dizzydefiler.mavy.render.MavyRenderer;
import com.dizzydefiler.mavy.render.MavySlotRender;
import com.dizzydefiler.mavy.util.Logger;
import com.dizzydefiler.mavy.util.PA;
import com.dizzydefiler.mavy.util.Util;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DrawHandler implements IContainerDrawHandler,IContainerInputHandler {

    public static boolean renderNEI = true;

    HashMap<String,Integer> prevBinding = new HashMap<String, Integer>();

    HashMap<KeyBinding,Integer> mineKeys = new HashMap<KeyBinding, Integer>();

    MavyRenderer mavyRenderer = new MavyRenderer();

    public boolean takingInput = false;

    ArrayList<Character> inputChars = new ArrayList<Character>();

    ArrayList<Integer> inputEvents = new ArrayList<Integer>();

    //Sorry
    ArrayList<Character> currentInput = new ArrayList<Character>();

    boolean updated = false;

    public DrawHandler() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        GuiContainerManager.addDrawHandler(this);
        GuiContainerManager.addInputHandler(this);

    }

    public void updateValues() {
        ItemPanel ip = LayoutManager.itemPanel;
        if (ip == null) {
            return;
        }
        PA._ItemPanel.updateValues(ip);
        updated = true;
    }

    void stopInput() {
        Mavy.currentState.refreshRequested = true;
        Mavy.currentState.distributeList.clear();
        Mavy.noRefresh = false;
        takingInput = false;
        restartMinecraftbinds();
        restartNEIKeyBinds();
        currentInput.clear();
    }

    public void finishAction() {
        if (Mavy.stickyMode) {
            Mavy.currentState.refreshRequested = true;
        }
        finAction();
    }

    public void finishAction(MavyState nextStickyState) {
        if (Mavy.stickyMode) {
            Mavy.currentState.refreshRequested = true;
            Mavy.currentState = nextStickyState;
        }
        finAction();
    }
    public void finishActionNoRefresh() {
        finAction();
    }

    void finAction() {
        if (Mavy.stickyMode) {
            currentInput.clear();
        } else {
            Mavy.currentState = MavyState.NONE;
            stopInput();
        }
    }

    public void nextAction(MavyState nextState) {
        Mavy.currentState.refreshRequested = true;
        Mavy.currentState = nextState;
        currentInput.clear();
    }

    public void nextActionNoRefresh(MavyState nextState) {
        Mavy.currentState = nextState;
        currentInput.clear();
    }

    public void manageKeysShift(int k, char c) {
        if (Util.client.currentScreen instanceof GuiRecipe) {
            GuiRecipe gr = (GuiRecipe) Util.client.currentScreen;
            if (c == Config.getCharFromProperty(Config.shiftPrevPage)) {
                PA._GuiRecipe.prevPage(gr);
            } else if (c == Config.getCharFromProperty(Config.shiftNextPage)) {
                PA._GuiRecipe.nextPage(gr);
            } else if (c == Config.getCharFromProperty(Config.shiftPrevType)) {
                PA._GuiRecipe.prevType(gr);
            } else if (c == Config.getCharFromProperty(Config.shiftNextType)) {
                PA._GuiRecipe.nextType(gr);
            }
        }

    }

    public boolean manageKeys(int k, char c) {
        MavyState initalState = Mavy.currentState;
        if (KeyManager.keyStates.get("mavy.recipe").down) {
            Mavy.currentState = MavyState.CRAFT;
        }
        if (KeyManager.keyStates.get("mavy.usage").down) {
            Mavy.currentState = MavyState.USAGE;
        }
        if (Mavy.currentState == MavyState.MASTER) {
            Mavy.stickyModeNoRefresh = false;
            if (c == Config.getCharFromProperty(Config.moveKey)) {
                Mavy.currentState = MavyState.INITAL_MOVE;

            } else if (c == Config.getCharFromProperty(Config.swapKey)) {
                Mavy.currentState = MavyState.INITAL_SWAP;
            } else if (c == Config.getCharFromProperty(Config.craftingKey)) {
                Mavy.currentState = MavyState.CRAFT;
            } else if (c == Config.getCharFromProperty(Config.stickyKey)) {
                Mavy.stickyMode = !Mavy.stickyMode;
                Mavy.currentState = MavyState.NONE;
                stopInput();
            } else if (c == Config.getCharFromProperty(Config.usageKey)) {
                Mavy.currentState = MavyState.USAGE;

            } else if (c == Config.getCharFromProperty(Config.shiftKey)) {
                Mavy.currentState = MavyState.SHIFT;
                if (Mavy.stickyMode) {
                    Mavy.stickyModeNoRefresh = true;
                }
            } else if (c == Config.getCharFromProperty(Config.dropKey)) {
                Mavy.currentState = MavyState.DROP;
                if (Mavy.stickyMode) {
                    Mavy.stickyModeNoRefresh = true;
                }
            } else if (c == Config.getCharFromProperty(Config.distributeKey)) {
                Mavy.currentState = MavyState.INITIAL_DISTRIBUTE;
            }


        }
        if (c == Config.getCharFromProperty(Config.refreshKey)) {
            Mavy.currentState.refreshRequested = true;
            Mavy.currentState.setPrefix(0);
            currentPrefix = 0;
            prevPrefix = 0;
        }

        if (c == Config.getCharFromProperty(Config.masterKey) && Mavy.currentState == MavyState.NONE) {
            Mavy.currentState = MavyState.MASTER;
        }
        if (Mavy.currentState != MavyState.NONE) {
            stopMinecraftbinds();
            stopNEIKeyBinds();
            String text = LayoutManager.searchField.text();
            if (text.length() > 0 && text.charAt(text.length() - 1) == c && LayoutManager.searchField.focused()) {
                LayoutManager.searchField.setText(text.substring(0,text.length() - 1));
            }
            LayoutManager.searchField.setFocus(false);

        } else {
            restartMinecraftbinds();
            restartNEIKeyBinds();
        }


//        if (KeyManager.keyStates.get("nei."))

        if (Mavy.currentState != MavyState.NONE && Mavy.currentState != MavyState.MASTER) {
            takingInput = true;
        }
        if (initalState != Mavy.currentState) {
            Logger.info("manageKeys()","Set prefix: " + (prevPrefix * 10 + currentPrefix));
            Mavy.currentState.setPrefix(prevPrefix * 10 + currentPrefix);
            Mavy.currentState.refreshRequested = true;
        }
        return initalState != Mavy.currentState;
    }

    void restartNEIKeyBind(String key) {
        int curKey = NEIClientConfig.getKeyBinding(key);
//        System.out.println("see: " + curKey);
        if (curKey == 0 && prevBinding.containsKey(key)) {
//            System.out.println("set " + prevBinding.get(key));
            NEIClientConfig.getSetting("keys." + key).setIntValue(prevBinding.get(key));
        }
    }
    void restartNEIKeyBinds() {
        restartNEIKeyBind("gui.search");
        restartNEIKeyBind("gui.enchant");
        restartNEIKeyBind("gui.recipe");
        restartNEIKeyBind("gui.bookmark");
        prevBinding.clear();
    }
    void stopNEIKeyBinds() {
        stopNEIKeyBind("gui.search");
        stopNEIKeyBind("gui.enchant");
        stopNEIKeyBind("gui.recipe");
        stopNEIKeyBind("gui.bookmark");
    }

    void stopNEIKeyBind(String key) {
        int curKey = NEIClientConfig.getKeyBinding(key);
        if (curKey != 0) {
            NEIClientConfig.getSetting("keys." + key).setIntValue(0);
            prevBinding.put(key,curKey);
//            System.out.println("put " + curKey);
        }
    }

    void stopMinecraftbinds() {

        PA._KeyBinding.updateValues();
        for (Object _ob : PA._KeyBinding.keybindArray) {
            KeyBinding kb = (KeyBinding) _ob;
            int key = kb.getKeyCode();
            if (key != 0) {
                mineKeys.put(kb,key);
                kb.setKeyCode(0);
            }

        }
    }

    void restartMinecraftbinds() {
        for (KeyBinding kb : mineKeys.keySet()) {
            if (kb.getKeyCode() == 0) {
                kb.setKeyCode(mineKeys.get(kb));
            }
        }
        mineKeys.clear();
    }

    MavyTree activeTree;

    MavyTree getActiveTree() {
        if ( !(Mavy.noRefresh || (Mavy.stickyMode && Mavy.stickyModeNoRefresh)) || Mavy.currentState.refreshRequested) {
            ArrayList<IMavyContainer> slots = new ArrayList<IMavyContainer>();
            addContainerSlots(slots);
            addNEISlots(slots);
            addRecipieButtons(slots);
            MavyTree mavyTree = new MavyTree(Mavy.keys);
            mavyTree.make(slots);
            activeTree = mavyTree;
            Mavy.currentState.refreshRequested = false;
        }
        return activeTree;
    }

    int currentPrefix = 0;

    int prevPrefix = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent tick) {
        mavyRenderer.resetAvyRender();
        if (!renderNEI) return;
        if (!(Util.client.currentScreen instanceof GuiContainer)) return;
        for (int i = 0; i < inputChars.size(); i++) {
            int event = inputEvents.get(i);
            char c = inputChars.get(i);
            if (Character.isDigit(c)) {
                prevPrefix = currentPrefix;
                currentPrefix = Character.digit(c,10);
            }
//            Logger.info("onClientTick()",c);
            if (event == Keyboard.KEY_ESCAPE || c == Config.getCharFromProperty(Config.abortKey)) {
                Logger.info("onClientTick","Abort, and die");
                stopInput();
                Mavy.currentState = MavyState.NONE;
                continue;
            }
            boolean keysChanged = false;
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                manageKeysShift(event,c);
            } else {
                keysChanged = manageKeys(event,c);
            }
            if (!keysChanged) {
                currentInput.add(c);
            }
//            if (!Character.isDigit().containsKey(c) && Mavy.currentState == MavyState.NONE) {
//                prevPrefix = 0;
//                currentPrefix = 0;
//            }
        }
        inputChars.clear();
        inputEvents.clear();
        if (Mavy.currentState == MavyState.NONE || !takingInput) return;
        updateValues();
        if (updated) {
            MavyTree mavyTree = getActiveTree();
            if (!mavyTree.findCharacters(currentInput)) {
                currentInput.remove(currentInput.size() - 1);
                mavyTree.findCharacters(currentInput);
            }

            ArrayList<IMavyContainer> candidates = mavyTree.traverse();

            if (candidates.size() == 1) {
                candidates.get(0).action();
                mavyTree.remove(candidates.get(0));
                Logger.info("onClientTick/traverse","Candidate found..");
//                stopInput();
                return;
            }
            MavySlotRender.updateGuiContainer();
            for (IMavyContainer mc : candidates) {
                mc.getMavyRender().update();
                mavyRenderer.addAvyRenderer(mc.getMavyRender());
            }
        }
    }

    static final int ARMOR_START = 5;

    static final int ARMOR_SLOTS = 4;

    public boolean shouldAddSlot(Slot sl) {
        if (Util.client.currentScreen instanceof GuiRecipe) {
            switch (Mavy.currentState) {
                case INFO:
                case CRAFT:
                case USAGE:
                    break;
                default:
                    return false;
            }
        }

        boolean craftingSlot = sl instanceof SlotCrafting;
        boolean furnaceSlot = sl instanceof SlotFurnace;
        boolean cp = Util.client.thePlayer.openContainer instanceof ContainerPlayer;
        boolean armor = cp && sl.slotNumber >= ARMOR_START && sl.slotNumber < ARMOR_START + ARMOR_SLOTS;


        switch (Mavy.currentState) {
            case INITAL_SWAP:
            case SELECTED_SWAP:
            case DROP:
            case INITIAL_DISTRIBUTE:
                return sl.getHasStack() && !craftingSlot && !furnaceSlot && !armor;
            case INITAL_MOVE:
            case CRAFT:
            case INFO:
            case SHIFT:
            case USAGE:
                return sl.getHasStack();
            case SELECTED_MOVE:
                Slot select = Mavy.currentState.getSelect();
                if (armor && select.getHasStack() && select.getStack().getItem() instanceof ItemArmor) {
                    armor = false;
                }
                boolean dropItem = false;
                if (sl.getHasStack() && select.getHasStack()) {
                    net.minecraft.item.Item curItem = sl.getStack().getItem();
                    net.minecraft.item.Item otherItem  = select.getStack().getItem();
                    if (curItem == otherItem && sl.getSlotIndex() != select.getSlotIndex()
                        && sl.getStack().stackSize < sl.getStack().getMaxStackSize()
                    ) {
                        dropItem = true;
                    }
                }
                return (!sl.getHasStack() || dropItem) && !craftingSlot && !furnaceSlot && !armor;
            case DISTRIBUTE:
                return !sl.getHasStack() && !craftingSlot && !furnaceSlot && !armor;
        }
        return false;
    }

    public void addRecipieButtons(ArrayList<IMavyContainer> slots) {
        if (!(Util.client.currentScreen instanceof GuiRecipe)) return;
        switch (Mavy.currentState) {
            case CRAFT:
            case USAGE:
                break;
            default:
                return;
        }
        GuiRecipe gr = (GuiRecipe) Util.client.currentScreen;
        PA._GuiRecipe.updateValues(gr);
        for (int i = 0; i < PA._GuiRecipe.overlayButtons.length; i++) {
            GuiButton gb  = PA._GuiRecipe.overlayButtons[i];
            if (gb.visible) {
                slots.add(new OverlayButton(gb.id,gb.xPosition,gb.yPosition));
            }
        }
    }

    public void addContainerSlots(ArrayList<IMavyContainer> slots) {
        Container inventory = Util.client.thePlayer.inventoryContainer;
        Container openContainer = Util.client.thePlayer.openContainer;
        GuiContainer guiContainer;
        int guiLeft;
        int guiTop;
        if (Util.client.currentScreen instanceof GuiContainer) {
            guiContainer = (GuiContainer) Util.client.currentScreen;
            PA._GuiContainer.updateValues(guiContainer);
            guiLeft = PA._GuiContainer.guiLeft;
            guiTop = PA._GuiContainer.guiTop;

        } else {
            return;
        }
        if (openContainer != null) {
            for (Object _obj : openContainer.inventorySlots) {
                Slot sl = (Slot) _obj;
                if (shouldAddSlot(sl)) {
                    slots.add(new ItemSlot(sl, guiLeft + sl.xDisplayPosition, guiTop + sl.yDisplayPosition));
                }
            }
        } else if (inventory != null) {
            for (Object _obj : inventory.inventorySlots) {
                Slot sl = (Slot) _obj;
                if (shouldAddSlot(sl)) {
                    slots.add(new ItemSlot(sl, guiLeft + sl.xDisplayPosition, guiTop + sl.yDisplayPosition));
                }
            }
        }
    }

    public void addNEISlots(ArrayList<IMavyContainer> slots) {
        switch (Mavy.currentState) {
            case INFO:
            case CRAFT:
            case USAGE:
                break;
            default:
                return;
        }
        if (NEIClientConfig.getSetting("inventory.hidden").getBooleanValue()) {
            return;
        }
        ItemPanel ip = LayoutManager.itemPanel;
        int index = PA._ItemPanel.firstIndex;

        for (int i = 0; i < PA._ItemPanel.row * PA._ItemPanel.column && index < ip.getItems().size(); ++i) {
            if (PA._ItemPanel.validSlotMap[i]) {
                Rectangle4i rect = ip.getSlotRect(i);
                slots.add(new Item(ip.getItems().get(i),rect.x,rect.y));
                ++index;
            }
        }
    }

    //TODO move
    public void postRenderObjects(GuiContainer var1, int var2, int var3) {
        if (Util.client.currentScreen instanceof GuiContainer) {
            GuiContainer gc = (GuiContainer) Util.client.currentScreen;
            PA._GuiContainer.updateValues(gc);
            Util.client.fontRenderer.drawString(getMavyText(),PA._GuiContainer.guiLeft,PA._GuiContainer.guiTop + PA._GuiContainer.ySize, Color.WHITE.getRGB());
        }
        if (!updated || !renderNEI) return;
        // if (event.phase != TickEvent.Phase.END) return;
        KeyFontRenderer fontrender = Mavy.fontrender;
        fontrender.startDraw();
        ArrayList<MavyRender> avyRenderers = mavyRenderer.avyRenderers();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        for (MavyRender mr : avyRenderers) {
            GL11.glPushMatrix();
            GL11.glTranslatef(mr.getRectX(), mr.getRectY(), 0.0F);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, fontrender.charactersToBuffer.get(mr.getCharacters())[0]);
            GL11.glColorPointer(3, GL11.GL_FLOAT, 24, 12);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 24, 0);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, fontrender.charactersToBuffer.get(mr.getCharacters())[2]);
            GL11.glPopMatrix();
        }
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        for (MavyRender mr : avyRenderers) {
            GL11.glPushMatrix();
            GL11.glTranslatef(mr.getRectX(), mr.getRectY(), 0.0F);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, fontrender.charactersToBuffer.get(mr.getCharacters())[1]);
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, fontrender.charactersToBuffer.get(mr.getCharacters())[2]);
            GL11.glPopMatrix();
        }
        fontrender.endDraw();

    }

    public String getMavyText() {
        if (Mavy.currentState == MavyState.NONE) return "";
        StringBuilder sb = new StringBuilder();

        switch (Mavy.currentState) {
            case CRAFT:
                sb.append("Recipe");
                break;
            case SHIFT:
                sb.append("Shift");
                break;
            case INITAL_MOVE:
                sb.append("Select/Move");
                break;
            case MASTER:
                sb.append("Choose mode");
                break;
            case DROP:
                sb.append("Drop");
                break;
            case SELECTED_MOVE:
                sb.append("Move to");
                break;
            case INITAL_SWAP:
                sb.append("Select/Swap");
                break;
            case USAGE:
                sb.append("Usage");
                break;
            case SELECTED_SWAP:
                sb.append("Swap with");
                break;
            case NONE:
                sb.append("Mavy");
                break;
            case DISTRIBUTE:
                sb.append("Distribute to");
                break;
            case INITIAL_DISTRIBUTE:
                sb.append("Select/Distribute");
                break;
        }
        if (Mavy.currentState.getPrefix() != 0) {
            sb.append("(");
            sb.append(Mavy.currentState.getPrefix());
            sb.append(")");
        }
        if (Mavy.stickyMode) {
            sb.append("[");
            sb.append("S");
            sb.append("]");
        }
        return sb.toString();
    }

    public void renderSlotUnderlay(GuiContainer var1, Slot var2) {

    }

    public void renderSlotOverlay(GuiContainer var1, Slot var2) {

    }

    public void onPreDraw(GuiContainer var1) {

    }

    public void renderObjects(GuiContainer var1, int var2, int var3) {
    }

    @Override
    public boolean keyTyped(GuiContainer guiContainer, char c, int i) {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer guiContainer, char c, int i) {
        inputEvents.add(i);
        inputChars.add(c);
    }

    @Override
    public boolean lastKeyTyped(GuiContainer guiContainer, char c, int i) {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer guiContainer, int i, int i1, int i2) {
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer guiContainer, int i, int i1, int i2) {

    }

    @Override
    public void onMouseUp(GuiContainer guiContainer, int i, int i1, int i2) {

    }

    @Override
    public boolean mouseScrolled(GuiContainer guiContainer, int i, int i1, int i2) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer guiContainer, int i, int i1, int i2) {

    }

    @Override
    public void onMouseDragged(GuiContainer guiContainer, int i, int i1, int i2, long l) {

    }
}

