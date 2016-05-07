package com.project.charlie.cryogenic.data;

import com.project.charlie.cryogenic.handlers.PlanetHandler;

/**
 * Created by Charlie on 17/03/2016.
 */
public class PlanetData extends ActorData {
    String name;
    String type;
    PlanetHandler planetHandler;
    boolean touched;

    public PlanetData(float width, float height) {
        super(width, height);
        dataType = "Planet";
    }

    public PlanetData(float width, float height, String name, String type) {
        super(width, height);
        this.name = name;
        this.type = type;
        dataType = "Planet";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlanetHandler getPlanetHandler() {
        return planetHandler;
    }

    public void setPlanetHandler(PlanetHandler planetHandler) {
        this.planetHandler = planetHandler;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
