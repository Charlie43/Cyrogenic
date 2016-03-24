package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Turret;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 22/02/2016.
 */
public class TurretActorData extends ActorData {
    protected float health;
    Constants.TurretType turretType;
    public Turret turret;


    public TurretActorData(float width, float height) {
        super(width, height);
        dataType = "Turret";
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

    public void setHealth(float health) {
        this.health = health;
    }

    public Constants.TurretType getTurretType() {
        return turretType;
    }

    public void setTurretType(Constants.TurretType turretType) {
        this.turretType = turretType;
        this.health = turretType.getHealth();
    }

    public Turret getTurret() {
        return turret;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }
}
