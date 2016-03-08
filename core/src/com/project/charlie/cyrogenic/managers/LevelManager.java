package com.project.charlie.cyrogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.project.charlie.cyrogenic.handlers.StageHandler;
import com.project.charlie.cyrogenic.objects.AsteroidLevel;
import com.project.charlie.cyrogenic.objects.LevelJSON;
import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

public class LevelManager {
    static World world;

    public LevelManager() {
    }

    public static void clearBase() {
        if(Gdx.files.local("plr_base.json").exists()) {
            if(Gdx.files.local("plr_base.json").delete())
                Gdx.app.log("LM", "Base deleted.");
            else
                Gdx.app.log("LM", "Base could not be deleted");
        }
    }

    public static StageHandler loadBase(World gameWorld) {
        world = gameWorld;
        StageHandler stage = new StageHandler();
        try {
            FileHandle base = Gdx.files.local("plr_base.json");
            if(!base.exists())
                return stage;

            Json json = new Json();
            LevelJSON levelJSON = json.fromJson(LevelJSON.class, base);

            for (TurretJSON turret : levelJSON.turrets) {
                stage.addTurret(turret);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stage;
    }

    public static StageHandler parseJSONLevel(World gameWorld) {
        world = gameWorld;
        StageHandler stage = new StageHandler();
        try {
            FileHandle fileHandle = Gdx.files.internal("levels/level.json");

            Gdx.app.log("JSON", "Got file..");
            Json json = new Json();
            LevelJSON jsonObj = json.fromJson(LevelJSON.class, fileHandle);
            stage.setAsteriodCount(jsonObj.asteroid.number);
            stage.setAsteriodInterval(jsonObj.asteroid.interval);
            stage.setAsteriodSpeed(jsonObj.asteroid.speed);
            for (TurretJSON turret : jsonObj.turrets) {
                stage.addTurret(turret);
            }

            Gdx.app.log("Done", "Done");

        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.log("JSON", "Exception");
        }

        return stage;

    }

    public static void writeLevel(ArrayList<TurretJSON> turrets) {
        try {
            Gdx.app.log("LM", "Writing level..");

            FileHandle plrBase = Gdx.files.local("plr_base.json");
            Gdx.app.log("LM", "Exists.. " + Gdx.files.local("plr_base.json").exists());
            Json json = new Json();

            LevelJSON lvlJson = new LevelJSON();
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
