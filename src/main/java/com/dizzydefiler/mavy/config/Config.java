package com.dizzydefiler.mavy.config;

import com.dizzydefiler.mavy.Mavy;
import com.dizzydefiler.mavy.util.Logger;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Config {

    public static Property mavyKeys,abortKey,oneHandedMode,masterKey,moveKey,swapKey,
            stickyKey,craftingKey,usageKey,shiftKey, distributeKey,refreshKey,dropKey,
            shiftPrevPage,shiftNextPage,shiftPrevType,shiftNextType;

    public static Configuration config;

    public static void loadConfig(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile(), Mavy.VERSION);
        config.load();
        update();
        config.save();
        ArrayList<Character> arr = new ArrayList<Character>();
        for (String str : mavyKeys.getStringList()) {
            arr.add(str.charAt(0));
        }
        Mavy.keys = arr;
    }

    public static void sync() {
        if (config == null) {
            Logger.error("loadConfig()", "Unable to read config file!");
            return;
        }
        update();
        if (config.hasChanged()) {
            updateMavyKeys();

            config.save();
        }

    }

    public static char getCharFromProperty(Property p) {
        return p.getString().charAt(0);
    }

    private static void updateMavyKeys() {
        ArrayList<Character> arr = new ArrayList<Character>();
        for (String str : mavyKeys.getStringList()) {
            arr.add(str.charAt(0));

        }
        Mavy.updateKeys(arr);
    }

    private static void update() {
        oneHandedMode = config.get("mavy","oneHandedMode",false,"Set defaults to keys more suited for use with one hand, leaving the other free to attend to important matters");
        Pattern reg = Pattern.compile("^\\w$");
        Pattern lowerAZ = Pattern.compile("^[a-z]$");
        Pattern upperAZ = Pattern.compile("^[A-Z]$");

        if (oneHandedMode.getBoolean()) {
            mavyKeys = config.get("mavy","mavyKeys", new String[]{"q","w","a","s","d","x","c","f"," "},"Mavy keys used to jump to targets",reg);
            masterKey = config.get("mavy","masterKey","w","Prefix key to all of the other keys. Make sure this does not conflict with other keys");
            moveKey = config.get("mavy","moveKey","a","Move",reg);
            swapKey = config.get("mavy", "swapKey", "s","Swap",reg);
            stickyKey = config.get("mavy", "stickyKey", "q","Sticky",reg);
            craftingKey = config.get("mavy","craftingKey","c","Recipe",reg);
            usageKey = config.get("mavy","usageKey","v","Usage",reg);
            shiftKey = config.get("mavy","shiftKey","x","Shift click",reg);
            distributeKey = config.get("mavy","distributeKey","d","Distribute",reg);
            dropKey = config.get("mavy","dropKey","z","Drop",reg);
            refreshKey = config.get("mavy","refreshKey","r","Refresh the current candidates (when applicable) and clear current prefix",reg);
            shiftPrevPage = config.get("mavy","shiftPrevPage","A","Previous recipe page (shift key only)",reg);
            shiftNextPage = config.get("mavy","shiftNextPage","D","Next recipe page (shift key only)",reg);
            shiftPrevType = config.get("mavy","shiftPrevType","Q","Previous recipe type (shift key only)",reg);
            shiftNextType = config.get("mavy","shiftNextType","E","Next recipe type (shift key only)",reg);
        } else {
            mavyKeys = config.get("mavy","mavyKeys", new String[]{"a","s","d","f","g","h","j","k","l"},"Mavy keys used to jump to targets",reg);
            masterKey = config.get("mavy","masterKey",";","Prefix Key to all of the other keys. Make sure this does not conflict with other keys",reg);
            moveKey = config.get("mavy","moveKey","m","Move",reg);
            swapKey = config.get("mavy", "swapKey", "s","Swap",reg);
            stickyKey = config.get("mavy", "stickyKey", "t","Sticky",reg);
            craftingKey = config.get("mavy","craftingKey","c","Recipe",reg);
            usageKey = config.get("mavy","usageKey","u","Usage",reg);
            shiftKey = config.get("mavy","shiftKey","f","Shift click",reg);
            distributeKey = config.get("mavy","distributeKey","d","Distribute",reg);
            dropKey = config.get("mavy","dropKey","x","Drop",reg);
            refreshKey = config.get("mavy","refreshKey","r","Refresh the current candidates (when applicable) and clear current prefix",reg);
            shiftPrevPage = config.get("mavy","shiftPrevPage","H","Previous recipe page (shift key only)",reg);
            shiftNextPage = config.get("mavy","shiftNextPage","L","Next recipe page (shift key only)",reg);
            shiftPrevType = config.get("mavy","shiftPrevType","K","Previous recipe type (shift key only)",reg);
            shiftNextType = config.get("mavy","shiftNextType","J","Next recipe type (shift key only)",reg);



        }
        ArrayList<String> fixup = new ArrayList<String>();
        for (String str : mavyKeys.getStringList()) {
            String fixedString = str;
            if (str.length() == 0) {
                fixedString = " ";
            }
            if (str.length() > 1) {
                fixedString = fixedString.substring(0,1);
            }
            fixup.add(fixedString);
        }
        mavyKeys.set(fixup.toArray(new String[0]));
        abortKey = config.get("mavy","abortKey","e","Abort key",reg);

    }
}
