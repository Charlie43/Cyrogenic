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
import com.project.charlie.cyrogenic.managers.LevelManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

/**
 * Created by Charlie on 05/03/2016.
 */
public class CreatorHandler extends GameHandler {
    String placing;
    CreateStageTouchHandler touchHandler;
    ArrayList<TurretJSON> placedTurrets;

    public CreatorHandler(GameStage stage) {
        super(stage);
        placing = "";
        touchHandler = new CreateStageTouchHandler(stage, this);
        placedTurrets = new ArrayList<TurretJSON>();
    }

    public void setUpButtons() {
        Skin skin = new Skin();
        skin.add("turret", new Texture(Gdx.files.internal(Constants.TURRET_IMAGE_PATH)));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = style.down = style.checked = skin.getDrawable("turret"); // todo indicate button is pressed

        final ImageButton imageButton = new ImageButton(style);
        imageButton.setSize(50, 50);
        imageButton.setPosition(stage.getCamera().viewportWidth / 6, stage.getCamera().viewportHeight / 6);
        imageButton.setBounds(stage.getCamera().viewportWidth / 6, stage.getCamera().viewportHeight / 6, imageButton.getWidth(), imageButton.getHeight());
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

    public void handlePlacement(float x, float y) {
        Gdx.app.log("CH", "Touch registered");
        if (placing.equals("turret")) {
            Gdx.app.log("CH", "Placing turret..");
            Turret temp = new Turret(WorldHandler.createTurret(stage.getWorld(), x, y, Constants.TURRET_WIDTH, Constants.TURRET_HEIGHT,
                    10)); // todo define firerate based on turret type
            temp.getActorData().turret = temp;
            placedTurrets.add(new TurretJSON(x, y, Constants.TURRET_WIDTH, Constants.TURRET_HEIGHT, 10));
            Gdx.app.log("CH", "Turret created, adding to stage at coords " + x + " - " + y);
            stage.addActor(temp);
            stage.addTurret(temp);
        }
    }

    public void setUpCreatorButton() {
        TextButton creatorButton = new TextButton("Create Level", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
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

    public void setUpFinishButton() {
        TextButton finishButton = new TextButton("Finish Level", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        finishButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200);
        finishButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200, 250, 40);
        finishButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                writeLevel();
                return false;
            }
        });
        stage.addActor(finishButton);
    }

    private void writeLevel() {
        // todo define n of asteroids etc based on difficulty (planet type etc)
        LevelManager.writeLevel(placedTurrets);
    }

    public void touchDown(float x, float y) {
        handlePlacement(x, y);
    }
}
