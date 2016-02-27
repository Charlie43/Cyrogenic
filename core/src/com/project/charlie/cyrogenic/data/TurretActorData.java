package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Turret;

/**
 * Created by Charlie on 22/02/2016.
 */
public class TurretActorData extends ActorData {
    protected float health;
    protected float fireRate;
    public Turret turret;


    public TurretActorData(float width, float height) {
        super(width, height);
        dataType = "Turret";
    }

    public TurretActorData(float width, float height, float health, float fireRate) {
        super(width, height);
        this.health = health;
        dataType = "Turret";
        this.fireRate = fireRate;
    }

    public boolean subHealth(float amount) {
        this.health -= amount;
        return this.health <= 0;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    public float getHealth() {
        return health;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }
}
