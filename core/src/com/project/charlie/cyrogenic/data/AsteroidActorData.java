package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Asteroid;

/**
 * Created by Charlie on 24/02/2016.
 */
public class AsteroidActorData extends ActorData {
    float health;
    public Asteroid asteroid;

    public AsteroidActorData(float width, float height) {
        super(width, height);
        dataType = "Asteroid";
    }

    public AsteroidActorData(float width, float height, float health) {
        super(width, height);
        dataType = "Asteroid";
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public float subHealth(float amount) {
        return health -= amount;
    }
}