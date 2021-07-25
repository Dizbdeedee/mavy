package com.dizzydefiler.mavy.util;

import codechicken.nei.ItemPanel;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.IRecipeHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import scala.Int;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//A mod needs access to private fields... shocking. Java is fun :^)
public class PA {

    public static class _ItemPanel {

        static Field privateRow;

        static Field privateColumns;

        static Field privateIndex;

        static Field privateValidSlotMap;

        static {
            try {
                privateValidSlotMap = ReflectionHelper.findField(ItemPanel.class,"validSlotMap");
                privateIndex = ReflectionHelper.findField(ItemPanel.class,"firstIndex");
                privateColumns = ReflectionHelper.findField(ItemPanel.class,"columns");
                privateRow = ReflectionHelper.findField(ItemPanel.class,"rows");

                privateColumns.setAccessible(true);
                privateRow.setAccessible(true);
                privateIndex.setAccessible(true);
                privateValidSlotMap.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Integer row;

        public static Integer column;

        public static Integer firstIndex;

        public static boolean[] validSlotMap;

        public static void updateValues(ItemPanel ip) {
            try {
                row = (Integer) privateRow.get(ip);
                firstIndex = (Integer) privateIndex.get(ip);
                validSlotMap = (boolean[]) privateValidSlotMap.get(ip);
                column = (Integer) privateColumns.get(ip);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class _GuiContainer {

        static Field privateGuiTop;

        static Field privateGuiLeft;

        static Field privateXSize;

        static Field privateYSize;

        static {
            try {
                privateGuiLeft = ReflectionHelper.findField(GuiContainer.class,"guiLeft","field_147003_i");
                privateGuiTop = ReflectionHelper.findField(GuiContainer.class,"guiTop","field_147009_r");
                privateXSize = ReflectionHelper.findField(GuiContainer.class,"xSize","field_146999_f");
                privateYSize = ReflectionHelper.findField(GuiContainer.class,"ySize","field_147000_g");

                privateGuiLeft.setAccessible(true);
                privateGuiTop.setAccessible(true);
                privateXSize.setAccessible(true);
                privateYSize.setAccessible(true);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Integer guiLeft;

        public static Integer guiTop;

        public static Integer xSize;

        public static Integer ySize;



        public static void updateValues(net.minecraft.client.gui.inventory.GuiContainer gc) {
            try {
                guiLeft = (Integer) privateGuiLeft.get(gc);
                guiTop = (Integer) privateGuiTop.get(gc);
                xSize = (Integer) privateXSize.get(gc);
                ySize = (Integer) privateYSize.get(gc);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class _KeyBinding {
        static Field privatekeybindArray;
        static {
            try {
                privatekeybindArray = ReflectionHelper.findField(KeyBinding.class,"keybindArray","field_74516_a");
                privatekeybindArray.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static List<KeyBinding> keybindArray;

        public static void updateValues() {
            try {
                keybindArray = (List<KeyBinding>) privatekeybindArray.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public static class _GuiRecipe {
        static Method nextPage;

        static Method prevPage;

        static Method nextType;

        static Method prevType;

        static Method getRecipiesPerPage;

        static Field privateOVERLAY_BUTTON_ID_START;

        static Field privateHandler;

        static Field privateOverlayButtons;


        public static Integer OVERLAY_BUTTON_ID_START;

        public static IRecipeHandler handler;

        public static GuiButton[] overlayButtons;


        static {
            try {

                nextPage = GuiRecipe.class.getDeclaredMethod("nextPage");
                nextPage.setAccessible(true);

                prevPage = GuiRecipe.class.getDeclaredMethod("prevPage");
                prevPage.setAccessible(true);

                nextType = GuiRecipe.class.getDeclaredMethod("nextType");
                nextType.setAccessible(true);

                prevType = GuiRecipe.class.getDeclaredMethod("prevType");
                prevType.setAccessible(true);

                getRecipiesPerPage = GuiRecipe.class.getDeclaredMethod("getRecipesPerPage");
                getRecipiesPerPage.setAccessible(true);

                privateOVERLAY_BUTTON_ID_START = GuiRecipe.class.getDeclaredField("OVERLAY_BUTTON_ID_START");
                privateOVERLAY_BUTTON_ID_START.setAccessible(true);

                privateHandler = GuiRecipe.class.getDeclaredField("handler");
                privateHandler.setAccessible(true);

                privateOverlayButtons = GuiRecipe.class.getDeclaredField("overlayButtons");
                privateOverlayButtons.setAccessible(true);

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        public static void nextPage(GuiRecipe obj) {
            try {
                nextPage.invoke(obj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public static void prevPage(GuiRecipe obj) {
            try {
                prevPage.invoke(obj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public static void nextType(GuiRecipe obj) {
            try {
                nextType.invoke(obj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public static void prevType(GuiRecipe obj) {
            try {
                prevType.invoke(obj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public static Integer getRecipiesPerPage(GuiRecipe obj) {
            try {
                return (Integer) getRecipiesPerPage.invoke(obj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException();
            } catch (IllegalAccessException e) {
                throw new RuntimeException();
            }
        }

        public static void updateValues(GuiRecipe obj) {
            try {
                OVERLAY_BUTTON_ID_START = (Integer) privateOVERLAY_BUTTON_ID_START.get(obj);
                handler = (IRecipeHandler) privateHandler.get(obj);
                overlayButtons = (GuiButton[]) privateOverlayButtons.get(obj);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
