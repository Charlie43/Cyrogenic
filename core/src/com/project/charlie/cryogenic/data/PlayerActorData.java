package com.project.charlie.cryogenic.data;

/**
 * Created by Charlie on 12/02/2016.
 */
public class PlayerActorData extends ActorData {
    float health;

    public PlayerActorData(float width, float height) {
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

    public float addHealth(float amount) {
        return this.health += amount;
    }
}
