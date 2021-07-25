package com.dizzydefiler.mavy.actions;

import com.dizzydefiler.mavy.render.MavyRender;

public interface IMavyContainer {

    void action();

    MavyRender getMavyRender();

    void setMavyRender(MavyRender mr);

    String getCharacters();

    void setCharacters(String characters);


}
