package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.actors.Turret;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.managers.PlanetManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

/**
 * Created by Charlie on 05/03/2016.
 */
public class CreatorHandler extends GameHandler {
    String placing;
    GameStage stage;
    ArrayList<TurretJSON> placedTurrets;

    public CreatorHandler(GameStage stage) {
        super(stage);
        this.stage = stage;
        placing = "";
        placedTurrets = new ArrayList<TurretJSON>();
    }

    public void setUpButtons() {
        Skin skin = new Skin();
        skin.add("turret", new Texture(Gdx.files.internal(Constants.TURRET_IMAGE_PATH)));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = style.down = style.checked = skin.getDrawable("turret"); // todo indicate button is pressed

        final ImageButton imageButton = new ImageButton(style);
        imageButton.setSize(50, 50);
        imageButton.setPosition(stage.getCamera().viewportWidth / 9, stage.getCamera().viewportHeight / 8);
        imageButton.setBounds(stage.getCamera().viewportWidth / 9, stage.getCamera().viewportHeight / 8, imageButton.getWidth(), imageButton.getHeight());
        imageButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Creator", "Button touched at coords " + imageButton.getX() + " - " + imageButton.getY());
                placing = "turret";
                return true;
            }
        });
        stage.addActor(imageButton);
    }

    Turret temp;

    public void handlePlacement(float x, float y) {
        x = Constants.ConvertToBox(x);
        y = Constants.ConvertToBox(y);
        if (placing.equals("turret")) {
            temp = new Turret(WorldHandler.createTurret(stage.getWorld(), x, y, Constants.TURRET_WIDTH, Constants.TURRET_HEIGHT,
                    10)); // todo define firerate based on turret type
            temp.getActorData().turret = temp;
            placedTurrets.add(new TurretJSON(x, y, Constants.TURRET_WIDTH, Constants.TURRET_HEIGHT, 10));
            Gdx.app.log("CH", "Turret created, adding to stage at coords " + x + " - " + y);
            stage.addActor(temp);
            stage.addTurret(temp);
        }
    }

    public void setUpCreatorButton() {
        TextButton creatorButton = new TextButton("Create Base", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        creatorButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 100);
        creatorButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 100, 250, 40);
        creatorButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setUpStageCreator();
                return true;
            }
        });
        stage.addActor(creatorButton);
    }

    public void setUpResetButton() {
        TextButton resetButton = new TextButton("Reset Base", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        resetButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 220);
        resetButton.setBounds(resetButton.getX(), resetButton.getY(), 250, 40);
        resetButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlanetManager.clearBase();
                placedTurrets.clear();
                if (stage.getPlanetHandler() != null)
                    stage.getPlanetHandler().setTurrets(null);
                stage.setUpStageCreator();
                return false;
            }
        });
        stage.addActor(resetButton);

    }

    public void setUpFinishButton() {
        TextButton finishButton = new TextButton("Finish Level", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        finishButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 190);
        finishButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 190, 250, 40);
        finishButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                placing = "";
                writeLevel();
                stage.setUpMenu();
                return true;
            }
        });
        stage.addActor(finishButton);
    }

    public void loadBase() {
        setUpTurrets(PlanetManager.loadBase(stage.getWorld()));
    }

    private void writeLevel() {
        // todo define n of asteroids etc based on difficulty (planet type etc)
        PlanetManager.writePlanet(placedTurrets);
    }

    public void touchDown(float x, float y) {
        handlePlacement(x, y);
    }
}
