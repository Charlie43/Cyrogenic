package com.project.charlie.cryogenic.data;

import com.project.charlie.cryogenic.actors.Asteroid;

/**
 * Created by Charlie on 24/02/2016.
 */
public class AsteroidActorData extends ActorData {
    float health;
    float damage;
    public Asteroid asteroid;

    public AsteroidActorData(float width, float height) {
        super(width, height);
        damage = 8.5f;
        dataType = "Asteroid";
    }

    public AsteroidActorData(float width, float height, float health) {
        super(width, height);
        dataType = "Asteroid";
        this.health = health;
        damage = 8.5f;
    }

    public float getHealth() {
        return health;
    }

    public float subHealth(float amount) {
        return health -= amount;
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
