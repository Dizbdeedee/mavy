package com.dizzydefiler.mavy.render;

import com.dizzydefiler.mavy.render.MavyRender;

import java.util.ArrayList;

public class MavyRenderer {

    private ArrayList<MavyRender> avyRenderers;

    public MavyRenderer() {
        avyRenderers = new ArrayList<MavyRender>();
    }

    public void addAvyRenderer(MavyRender avyRender) {
        avyRenderers.add(avyRender);
    }

    public void resetAvyRender() {
        avyRenderers.clear();
    }


    public ArrayList<MavyRender> avyRenderers() {
        return avyRenderers;
    }
}
