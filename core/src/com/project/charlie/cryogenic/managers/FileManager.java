package com.project.charlie.cryogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.project.charlie.cryogenic.handlers.PlanetHandler;
import com.project.charlie.cryogenic.objects.AsteroidLevel;
import com.project.charlie.cryogenic.objects.PlanetJSON;
import com.project.charlie.cryogenic.objects.PlayerJSON;
import com.project.charlie.cryogenic.objects.TurretJSON;

import java.util.ArrayList;

public class FileManager {
    static World world;

    static Preferences preferences = Gdx.app.getPreferences("Player");

    public FileManager() {
    }

    public static PlayerJSON loadPlayer() {
        Gdx.app.log("FM", "loading player " + preferences.getInteger("Currency"));
        return new PlayerJSON(preferences.getInteger("Currency"), preferences.getInteger("Damage"),
                preferences.getInteger("Speed"), preferences.getInteger("Multishot"));
    }

    public static void savePlayer(PlayerJSON playerJSON) {
        preferences.putInteger("Currency", playerJSON.currency);
        preferences.putInteger("Damage", playerJSON.damageLevel);
        preferences.putInteger("Speed", playerJSON.speedLevel);
        preferences.putInteger("Multishot", playerJSON.multishotLevel);
        preferences.putInteger("Health", playerJSON.healthLevel);
        Gdx.app.log("FM", "saving currency " + playerJSON.currency);
    }

    public static ArrayList<PlanetJSON> loadPlanets() { // todo sector handling
        ArrayList<PlanetJSON> planets = new ArrayList<PlanetJSON>();
        try {
            FileHandle[] files = Gdx.files.internal("planets/").list();
            Json json = new Json();
            for (FileHandle file : files) {
                planets.add(json.fromJson(PlanetJSON.class, file));
                Gdx.app.log("DEBUG", planets.get(planets.size() - 1).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planets;
    }

    public static PlanetHandler loadPlanet(String planet) {
        PlanetHandler planetHandler = new PlanetHandler();
        try {
            FileHandle[] files = Gdx.files.internal("planets/").list();
            Json json = new Json();

            for (FileHandle file : files) {
                if (file.name().contains(planet)) {
                    PlanetJSON planetJSON = json.fromJson(PlanetJSON.class, file);
                    planetHandler.setName(planetJSON.name);
                    planetHandler.setType(planetJSON.type);
                    planetHandler.setAsteriodCount(planetJSON.asteroid.number);
                    planetHandler.setAsteriodInterval(planetJSON.asteroid.interval);
                    planetHandler.setAsteriodSpeed(planetJSON.asteroid.speed);

                    for (TurretJSON turret : planetJSON.turrets) {
                        planetHandler.addTurret(turret);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return planetHandler;
    }

    public static PlanetHandler parsePlanet(World gameWorld) {
        world = gameWorld;
        PlanetHandler planet = new PlanetHandler();
        try {
            FileHandle fileHandle = Gdx.files.internal("planets/level.json");

            Gdx.app.log("JSON", "Got file..");
            Json json = new Json();
            PlanetJSON jsonObj = json.fromJson(PlanetJSON.class, fileHandle);
            planet.setName(jsonObj.name);
            planet.setType(jsonObj.type);
            planet.setAsteriodCount(jsonObj.asteroid.number);
            planet.setAsteriodInterval(jsonObj.asteroid.interval);
            planet.setAsteriodSpeed(jsonObj.asteroid.speed);

            for (TurretJSON turret : jsonObj.turrets) {
                planet.addTurret(turret);
            }

            Gdx.app.log("Done", "Done. Loaded planet: " + planet.getName());

        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.log("JSON", "Exception");
        }
        return planet;
    }

    public static void writePlanet(ArrayList<TurretJSON> turrets) {
        try {
            Gdx.app.log("LM", "Writing level..");

            FileHandle plrBase = Gdx.files.local("plr_base.json");
            Gdx.app.log("LM", "Exists.. " + Gdx.files.local("plr_base.json").exists());
            Json json = new Json();

            PlanetJSON planetJSON = new PlanetJSON();
            planetJSON.turrets = turrets;
            planetJSON.asteroid = new AsteroidLevel(10, 10, 3);

            plrBase.writeString(json.toJson(planetJSON), false);
            Gdx.app.log("LM", "Written");
            Gdx.app.log("LM", plrBase.readString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
