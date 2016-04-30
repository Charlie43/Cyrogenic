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

    public PlayerJSON(int currency) {
        this.currency = currency;
    }

    public PlayerJSON(int currency, int damageLevel, int speedLevel, int multishotLevel) {
        this.currency = currency;
        this.damageLevel = damageLevel;
        this.speedLevel = speedLevel;
        this.multishotLevel = multishotLevel;
    }
}
