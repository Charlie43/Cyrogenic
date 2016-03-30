package com.project.charlie.cyrogenic.objects;

/**
 * Created by Charlie on 07/03/2016.
 */


public class TurretJSON {
    float x;
    float y;
    String type;

    public TurretJSON() {

    }

    public TurretJSON(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TurretJSON{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

