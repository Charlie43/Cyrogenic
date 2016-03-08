package com.project.charlie.cyrogenic.objects;

import java.util.ArrayList;

public class LevelJSON {
    public AsteroidLevel asteroid;
    public ArrayList<TurretJSON> turrets;

    @Override
    public String toString() {
        String string = "";
        for (TurretJSON turret : turrets) {
            string = string + turret.toString();
        }
        return string;
    }
}
