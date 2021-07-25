package com.dizzydefiler.mavy.render;

import com.dizzydefiler.mavy.actions.IMavyContainer;

public class MavyRender {

    private float rectX;

    private float rectY;

    IMavyContainer mavyContainer;

    public MavyRender(float rectX, float rectY, IMavyContainer mc) {
        this.rectX = rectX;
        this.rectY = rectY;
        mavyContainer = mc;
    }

    public void update() {}

    public String getCharacters() {
        return mavyContainer.getCharacters();
    }

    public float getRectX() {
        return rectX;
    }

    public void setRectX(float rectX) {
        this.rectX = rectX;
    }

    public float getRectY() {
        return rectY;
    }

    public void setRectY(float rectY) {
        this.rectY = rectY;
    }
}
