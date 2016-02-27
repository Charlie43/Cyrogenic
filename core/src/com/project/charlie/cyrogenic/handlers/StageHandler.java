package com.project.charlie.cyrogenic.handlers;

import java.util.ArrayList;

/**
 * Created by Charlie on 24/02/2016.
 */
public class StageHandler {

    private int id;
    private int asteriodCount;
    private float asteriodInterval;
    private float asteriodSpeed;
    private ArrayList<TurretTemplate> turrets;

    public StageHandler() {
        turrets = new ArrayList<TurretTemplate>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsteriodCount() {
        return asteriodCount;
    }

    public void setAsteriodCount(int asteriodCount) {
        this.asteriodCount = asteriodCount;
    }

    public float getAsteriodInterval() {
        return asteriodInterval;
    }

    public void setAsteriodInterval(float asteriodInterval) {
        this.asteriodInterval = asteriodInterval;
    }

    public float getAsteriodSpeed() {
        return asteriodSpeed;
    }

    public void setAsteriodSpeed(float asteriodSpeed) {
        this.asteriodSpeed = asteriodSpeed;
    }

    public ArrayList<TurretTemplate> getTurrets() {
        return turrets;
    }

    public void setTurrets(ArrayList<TurretTemplate> turrets) {
        this.turrets = turrets;
    }

    public void addTurret(TurretTemplate turret) {
        this.turrets.add(turret);
    }


    public class TurretTemplate {
        float x;
        float y;
        float fireRate;
        float health;
        float damage;

        public TurretTemplate(float x, float y, float fireRate) {
            this.x = x;
            this.y = y;
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
    }
}
