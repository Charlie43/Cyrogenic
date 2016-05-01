package com.project.charlie.cyrogenic.objects;

/**
 * Created by Charlie on 30/04/2016.
 */
public class PlayerJSON {
    public int currency;
    public int damageLevel;
    public int speedLevel;
    public int multishotLevel;
    public int healthLevel;

    public PlayerJSON() {
    }

    public PlayerJSON(int currency, int damageLevel, int speedLevel, int multishotLevel) {
        this.currency = currency;
        this.damageLevel = damageLevel;
        this.speedLevel = speedLevel;
        this.multishotLevel = multishotLevel;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = damageLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public int getMultishotLevel() {
        return multishotLevel;
    }

    public void setMultishotLevel(int multishotLevel) {
        this.multishotLevel = multishotLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(int healthLevel) {
        this.healthLevel = healthLevel;
    }
}
