package com.dizzydefiler.mavy.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import com.dizzydefiler.mavy.Mavy;
import com.dizzydefiler.mavy.MavyTree;
import com.dizzydefiler.mavy.util.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class KeyFontRenderer extends net.minecraft.client.gui.FontRenderer {


    public KeyFontRenderer(GameSettings arg1, ResourceLocation arg2, TextureManager arg3, boolean arg4) {
        super(arg1, arg2, arg3, arg4);
    }

    public HashMap<String, int[]> charactersToBuffer = new HashMap<String, int[]>();


    int _charWidth(char c) {
        if (c == ' ') {
            return 4;
        } else {
            return this.charWidth[c];
        }
    }

    public void generatePatterns() {
        // well, deleting buffers after yourself and being a good boy makes opengl slow down to a crawl on the following code... cool.
        // try not to change keys too many times in a single run ok?
        charactersToBuffer.clear();
        Set<String> smp = MavyTree.generateAllSamples(Mavy.keys);
        Logger.info("generatePatterns()","Now generating arrays...");
        for (String s : smp) {
            float charwidth = 0F;
            int idBack = GL15.glGenBuffers();
            int idChar = GL15.glGenBuffers();
            float[] totalBack = new float[0];
            float[] totalChar = new float[0];
            int totalVerticies = 0;
            float length = 0F;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (i == 3) {
                    length = 8.0F;
                    charwidth = 0F;
                }
                float f = (float) (c % 16 * 8);
                float f1 = (float) (c / 16 * 8);
                float f3 = (float) _charWidth(c) - 0.01F;
                float[] v = {0 + charwidth, 0 + length, 1.0F,
                        0 + charwidth, 7.99F + length, 1.0F,
                        (f3 - 1.0F) + charwidth, 0 + length, 1.0F,
                        (f3 - 1.0F) + charwidth, 0 + length, 1.0F,
                        0 + charwidth, 7.99F + length, 1.0F,
                        (f3 - 1.0F) + charwidth, 7.99F + length, 1.0F};
                float[] drawBack = {v[0], v[1], v[2], colors[i][0], colors[i][1], colors[i][2],
                        v[3], v[4], v[5], colors[i][0], colors[i][1], colors[i][2],
                        v[6], v[7], v[8], colors[i][0], colors[i][1], colors[i][2],
                        v[9], v[10], v[11], colors[i][0], colors[i][1], colors[i][2],
                        v[12], v[13], v[14], colors[i][0], colors[i][1], colors[i][2],
                        v[15], v[16], v[17], colors[i][0], colors[i][1], colors[i][2]};
                float[] drawChar = {v[0], v[1], v[2], f / 128.0F, f1 / 128.0F, //v1
                        v[3], v[4], v[5], f / 128.0F, (f1 + 7.99F) / 128.0F, //v2
                        v[6], v[7], v[8], (f + f3 - 1.0F) / 128.0F, f1 / 128.0F, //v3
                        v[9], v[10], v[11], (f + f3 - 1.0F) / 128.0F, f1 / 128.0F, //v3
                        v[12], v[13], v[14], f / 128.0F, (f1 + 7.99F) / 128.0F, //v2
                        v[15], v[16], v[17], (f + f3 - 1.0F) / 128.0F, (f1 + 7.99F) / 128.0F //v4
                };
                totalBack = ArrayUtils.addAll(totalBack, drawBack);
                totalChar = ArrayUtils.addAll(totalChar, drawChar);
                charwidth += _charWidth(c);
                totalVerticies += 6;
            }
            charactersToBuffer.put(s, new int[]{idBack, idChar, totalVerticies});
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, idBack);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, makeFloatBuffer(totalBack), GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, idChar);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, makeFloatBuffer(totalChar), GL15.GL_STATIC_DRAW);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    @Override
    protected float renderDefaultChar(int chr, boolean italic) {
//	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,bufid[chr - 97]);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
        return (float) charWidth[chr];
    }

    public static float[][] colors = {{1.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 1.0F}, {1.0F, 0.411764705882F, 0.705882352941F}, {0.0F, 0.498F, 0.275F}, {0.0F,1.0F,0.0F}};

    public void _bindTexture() {
        bindTexture(this.locationFontTexture);
    }

    public void startDraw() {

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        _bindTexture();
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
    }

    public void endDraw() {
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

}
