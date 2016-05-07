package com.project.charlie.cryogenic.objects;

import java.util.ArrayList;

public class PlanetJSON {
    public String name;
    public String type;
    public float size;
    public String image;
    public float x;
    public float y;
    public AsteroidLevel asteroid;
    public ArrayList<TurretJSON> turrets;

    @Override
    public String toString() {
        String string = "";
        string = string + name;
        string = string + " " + type;
        string = string + " " + size;
        string = string + " " + image;
        for (TurretJSON turret : turrets) {
            string = string + turret.toString();
        }
        return string;
    }
}
