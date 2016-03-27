package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Tesla;

/**
 * Created by Charlie on 27/03/2016.
 */
public class TeslaActorData extends ActorData {
    private Tesla tesla;
    float damage;
    float rotation;

    public TeslaActorData(float width, float height, float rotation, String shotBy) {
        super(width, height);
        this.rotation = rotation;
        dataType = "Tesla";
        this.shotBy = shotBy;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Tesla getTesla() {
        return tesla;
    }

    public void setTesla(Tesla tesla) {
        this.tesla = tesla;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
