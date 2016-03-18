package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.actors.Planet;
import com.project.charlie.cyrogenic.data.PlanetData;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.managers.PlanetManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.PlanetJSON;

import java.util.ArrayList;

/**
 * Created by Charlie on 16/03/2016.
 */
public class StarMapHandler extends GameHandler {
    GameStage stage;

    public StarMapHandler(GameStage stage) {
        super(stage);
        this.stage = stage;
    }

    public void setUpMapButton() {
        TextButton mapButton = new TextButton("Star Map", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        mapButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 140);
        mapButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 140, 250, 40);
        mapButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setUpMap();
                return true;
            }
        });
        stage.addActor(mapButton);
    }

    public void createPlanets(int sector) {
        ArrayList<PlanetJSON> planets = PlanetManager.loadPlanets();
        for (PlanetJSON planet : planets) {
            final Planet planetActor = new Planet(WorldHandler.createPlanet(stage.getWorld(), planet), planet.image);

            stage.addLabel(planet.name, stage.createLabel(planet.name, new Vector3(
                    Constants.ConvertToScreen(planet.x),
                    Constants.ConvertToScreen(planet.y) + planet.size,
                    0), 5, 10, 0, 0.4f));

            stage.addPlanet(planetActor, planet.name);
            planetActor.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.log("Touch", "touch");
                    PlanetData data = (PlanetData) planetActor.getActorData();
                    stage.setUpPlanet(data.getName());
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
}
