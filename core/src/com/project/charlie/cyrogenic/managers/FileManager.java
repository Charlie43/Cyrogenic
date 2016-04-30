package com.project.charlie.cyrogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.project.charlie.cyrogenic.handlers.PlanetHandler;
import com.project.charlie.cyrogenic.objects.AsteroidLevel;
import com.project.charlie.cyrogenic.objects.PlanetJSON;
import com.project.charlie.cyrogenic.objects.PlayerJSON;
import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

public class FileManager {
    static World world;

    public FileManager() {
    }

    public static void clearBase() {
        if (Gdx.files.local("plr_base.json").exists()) {
            if (Gdx.files.local("plr_base.json").delete())
                Gdx.app.log("LM", "Base deleted.");
            else
                Gdx.app.log("LM", "Base could not be deleted");
        }
    }

    public static PlayerJSON loadPlayer() {
        FileHandle currencyFile = Gdx.files.local("plr_currency.json");
        if (!currencyFile.exists())
            return null;

        Json json = new Json();
        PlayerJSON playerJSON = json.fromJson(PlayerJSON.class, currencyFile);
        return playerJSON;
    }

    public static void savePlayer(PlayerJSON playerJSON) {
        FileHandle currencyFile = Gdx.files.local("plr_currency.json");
        if(!currencyFile.exists())
            return;

        Json json = new Json();
        currencyFile.writeString(json.toJson(playerJSON), false);
    }

    public static PlanetHandler loadBase(World gameWorld) {
        world = gameWorld;
        PlanetHandler stage = new PlanetHandler();
        try {
            FileHandle base = Gdx.files.local("plr_base.json");
            if (!base.exists())
                return stage;

            Json json = new Json();
            PlanetJSON levelJSON = json.fromJson(PlanetJSON.class, base);

            for (TurretJSON turret : levelJSON.turrets) {
                stage.addTurret(turret);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stage;
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

            PlanetJSON lvlJson = new PlanetJSON();
            lvlJson.turrets = turrets;
            lvlJson.asteroid = new AsteroidLevel(10, 10, 3);

            plrBase.writeString(json.toJson(lvlJson), false);
            Gdx.app.log("LM", "Written");
            Gdx.app.log("LM", plrBase.readString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
