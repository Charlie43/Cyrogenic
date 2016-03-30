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
    Constants.TurretType placing;
    GameStage stage;
    ArrayList<TurretJSON> placedTurrets;

    public CreatorHandler(GameStage stage) {
        super(stage);
        this.stage = stage;
        placing = null;
        placedTurrets = new ArrayList<>();
    }

    public void setUpButtons() {
        Skin skin;
        ImageButton.ImageButtonStyle style;
        int padding = 0;
        ImageButton imageButton;

        for (Constants.TurretType type : Constants.TurretType.values()) {
            style = new ImageButton.ImageButtonStyle();
            switch (type) {
                case BURST:
                case MACHINE_GUN:
                    skin = new Skin();
                    skin.add(type.name(), new Texture(Gdx.files.internal(Constants.TURRET_IMAGE_PATH)));
                    style.up = style.down = style.checked = skin.getDrawable(type.name());
                    break;
                case TESLA:
                    skin = new Skin();
                    skin.add(type.name(), new Texture(Gdx.files.internal(Constants.T_TURRET_IMAGE_PATH)));
                    style.up = style.down = style.checked = skin.getDrawable(type.name());
                    break;
                case LASER:
                    skin = new Skin();
                    skin.add(type.name(), new Texture(Gdx.files.internal(Constants.L_TURRET_IMAGE_PATH)));
                    style.up = style.down = style.checked = skin.getDrawable(type.name());
                    break;
            }
            imageButton = new ImageButton(style);
            imageButton.setSize(50, 50);
            imageButton.setPosition(padding, stage.getCamera().viewportHeight * 0.10f);
            imageButton.setBounds(padding, stage.getCamera().viewportHeight * 0.10f, imageButton.getWidth(), imageButton.getHeight());
            imageButton.setName(style.up.toString());
            imageButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    placing = Constants.TurretType.valueOf(event.getListenerActor().getName());
                    Gdx.app.log("Creator", "Button touched Placing: " + placing.toString());
                    return true;
                }
            });
            stage.addActor(imageButton);
            padding += 50;
        }
    }

    Turret temp;

    public void handlePlacement(float x, float y) {
        x = Constants.ConvertToBox(x);
        y = Constants.ConvertToBox(y);
        if (placing == null)
            return;

        switch (placing) {
            case BURST:
            case TESLA:
            case LASER:
            case MACHINE_GUN:
                temp = new Turret(WorldHandler.createTurret(stage.getWorld(), x, y, placing.getWidth(), placing.getHeight()), placing);
                temp.getActorData().turret = temp;
                placedTurrets.add(new TurretJSON(x, y));
                Gdx.app.log("CH", "Turret created, adding to stage at coords " + x + " - " + y);
                stage.addActor(temp);
                stage.addTurret(temp);
                break;
            default:
                break;
        }
    }

    public void setUpCreatorButton() {
        TextButton creatorButton = new TextButton("Create Base", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        creatorButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight * 0.20f);
        creatorButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight * 0.20f, 250, 40);
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
                placing = null;
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
        if (y > (stage.getCamera().viewportHeight * 0.10f) + 60) { // Don't place turrets below the cut off line. todo visualize this
            handlePlacement(x, y);
        }
    }
}
