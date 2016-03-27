package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Laser;

/**
 * Created by Charlie on 24/03/2016.
 */
public class LaserActorData extends ActorData {
    public Laser laser;
    float damage;

    public LaserActorData(Laser laser, float damage) {
        this.laser = laser;
        this.damage = damage;
        dataType = "Laser";
    }

    public LaserActorData(float width, float height, String shotBy) {
        super(width, height);
        dataType = "Laser";
        this.shotBy = shotBy;
    }

    public Laser getLaser() {
        return laser;
    }

    public void setLaser(Laser laser) {
        this.laser = laser;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
