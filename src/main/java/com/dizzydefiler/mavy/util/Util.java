package com.dizzydefiler.mavy.util;


import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Provides basic utility methods. For input methods, look at InputUtil.
 */
public class Util {

    /**
     * Current Instance Client, used for a lot of operations.
     */
    public static final Minecraft client = FMLClientHandler.instance().getClient();

    public static ItemStack getHeldStack() {
        return client.thePlayer.inventory.getItemStack();
    }

    /**
     * Returns if the current player is helding a item stack.
     *
     * @return True, if held stack != null
     */
    public static boolean isHoldingStack() {
        return (getHeldStack() != null);
    }


}
