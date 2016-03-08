package com.project.charlie.cyrogenic.handlers;

import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

/**
 * Created by Charlie on 24/02/2016.
 */
public class StageHandler {

    private int id;
    private int asteriodCount;
    private float asteriodInterval;
    private float asteriodSpeed;
    private ArrayList<TurretJSON> turrets;

    public StageHandler() {
        turrets = new ArrayList<TurretJSON>();
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

    public ArrayList<TurretJSON> getTurrets() {
        return turrets;
    }

    public void setTurrets(ArrayList<TurretJSON> turrets) {
        this.turrets = turrets;
    }

    public void addTurret(TurretJSON turret) {
        this.turrets.add(turret);
    }
}
