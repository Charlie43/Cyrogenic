package com.project.charlie.cyrogenic.objects;

/**
 * Created by Charlie on 07/03/2016.
 */


public class TurretJSON {
    float x;
    float y;
    float width;
    float height;
    float fireRate;
    float health;
    float damage;
    String type;

    public TurretJSON() {

    }

    public TurretJSON(float x, float y, float width, float height, float fireRate) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
//            this.health = health;
        this.fireRate = fireRate;
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

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
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
                ", width=" + width +
                ", height=" + height +
                ", fireRate=" + fireRate +
                ", health=" + health +
                ", damage=" + damage +
                '}';
    }
}

