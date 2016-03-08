package com.project.charlie.cyrogenic.objects;


/**
 * Created by Charlie on 07/03/2016.
 */

public class AsteroidLevel {
    public AsteroidLevel(int number, int speed, float interval) {
        this.number = number;
        this.speed = speed;
        this.interval = interval;
    }

    public AsteroidLevel() {
    }

    public int number;
    public int speed;
    public float interval;
}
