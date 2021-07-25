package com.dizzydefiler.mavy.actions;

import com.dizzydefiler.mavy.render.MavyRender;

public abstract class Base implements IMavyContainer {

    MavyRender mr;

    String characters;

    public MavyRender getMavyRender() {
        return mr;
    }

    public void setMavyRender(MavyRender mr) {
        this.mr = mr;
    }

    @Override
    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        return "MavyBase{" +
                "characters='" + characters + '\'' +
                '}';
    }
}
