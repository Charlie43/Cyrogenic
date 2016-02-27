package com.project.charlie.cyrogenic.data;

/**
 * Created by Charlie on 12/02/2016.
 */
public abstract class ActorData {
    protected float width;
    protected float height;
    public boolean isRemoved;
    protected String dataType;

    public ActorData() {
    }

    public ActorData(float width, float height) {
        this.width = width;
        this.height = height;
        this.isRemoved = false;
        this.dataType = "";

    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getDataType() {
        return dataType;
    }
}
