package com.project.charlie.cyrogenic.data;

/**
 * Created by Charlie on 12/02/2016.
 */
public class PlayerUserData extends ActorData {
    float health;

    public PlayerUserData(float width, float height) {
        super(width, height);
        dataType = "Player";
        this.health = 100f;
    }

    public float getHealth() {
        return health;
    }

    public float subHealth(float amount) {
        return this.health -= amount;
    }
}
