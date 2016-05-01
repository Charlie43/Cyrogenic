package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.managers.FileManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.TurretJSON;

import java.util.ArrayList;

/**
 * Created by Charlie on 05/03/2016.
 */
public class CreatorHandler extends GameHandler {
    Constants.TurretType placing;
    GameStage stage;
    ArrayList<TurretJSON> placedBuildings;
    PlayerHandler playerHandler;
//    Turret temp;

    public CreatorHandler(GameStage stage) {
        super(stage);
        this.stage = stage;
        this.playerHandler = stage.getPlayerHandler();
        placing = null;
        placedBuildings = new ArrayList<>();
    }

    public TextButton createButton(String text) {
        return new TextButton(text, new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
    }

    public void setUpLabels() {
        float screenHeight = stage.getCamera().viewportHeight;
        float screenWidth = stage.getCamera().viewportWidth;

        stage.addLabel("CurrencyLabel", stage.createLabel("Currency: " + playerHandler.getCurrentCurrency(), new Vector3(screenWidth * 0.20f, screenHeight * 0.95f, 0), 10, 1, 0, 1f));
        stage.addLabel("HealthLabel", stage.createLabel("Health: " + playerHandler.getHealth(), new Vector3(screenWidth * 0.20f, screenHeight * 0.90f, 0), 10, 1, 0, 1f));
        stage.addLabel("DamageLabel", stage.createLabel("Damage: " + playerHandler.getDamage(), new Vector3(screenWidth * 0.20f, screenHeight * 0.85f, 0), 10, 1, 0, 1f));
        stage.addLabel("SpeedLabel", stage.createLabel("Speed: " + playerHandler.getSpeed(), new Vector3(screenWidth * 0.20f, screenHeight * 0.80f, 0), 10, 1, 0, 1f));
        stage.addLabel("MultiShotLabel", stage.createLabel("MultiShot: " + playerHandler.getMultiShot(), new Vector3(screenWidth * 0.20f, screenHeight * 0.75f, 0), 10, 1, 0, 1f));
    }

    public void setUpButtons() {
        TextButton health = createButton("Increase Health: " + String.format("%d", (long) playerHandler.getHealthUpgradeCost()));
        TextButton speed = createButton("Increase Speed: " + String.format("%d", (long) playerHandler.getSpeedUpgradeCost()));
        TextButton damage = createButton("Increase Damage: " + String.format("%d", (long) playerHandler.getDamageUpgradeCost()));
        TextButton multiShot = createButton("Gain Multi-Shot: " + String.format("%d", (long) playerHandler.getMultiShotUpgradeCost()));

        float screenHeight = stage.getCamera().viewportHeight;
        float screenWidth = stage.getCamera().viewportWidth;

        multiShot.setPosition(screenWidth * 0.50f, screenHeight * 0.80f);
        multiShot.setBounds(screenWidth * 0.50f, screenHeight * 0.80f, 250, 40);
        health.setPosition(screenWidth * 0.50f, screenHeight * 0.70f);
        health.setBounds(screenWidth * 0.50f, screenHeight * 0.70f, 250, 40);
        damage.setPosition(screenWidth * 0.50f, screenHeight * 0.60f);
        damage.setBounds(screenWidth * 0.50f, screenHeight * 0.60f, 250, 40);
        speed.setPosition(screenWidth * 0.50f, screenHeight * 0.50f);
        speed.setBounds(screenWidth * 0.50f, screenHeight * 0.50f, 250, 40);

        if (playerHandler.multishotLevel > 0)
            multiShot.setDisabled(true);

        multiShot.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (playerHandler.getCurrentCurrency() > playerHandler.getMultiShotUpgradeCost()) {
                    playerHandler.removeCurrency((int) playerHandler.getMultiShotUpgradeCost());
                    playerHandler.multishotLevel++;
                    stage.clear();
                    stage.setUpStageCreator();
                }
                return true;
            }
        });

        damage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (playerHandler.getCurrentCurrency() > playerHandler.getDamageUpgradeCost()) {
                    playerHandler.removeCurrency((int) playerHandler.getDamageUpgradeCost());
                    playerHandler.damageLevel++;
                    stage.clear();
                    stage.setUpStageCreator();
                }
                return true;
            }
        });

        speed.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (playerHandler.getCurrentCurrency() > playerHandler.getSpeedUpgradeCost()) {
                    playerHandler.removeCurrency((int) playerHandler.getSpeedUpgradeCost());
                    playerHandler.speedLevel++;
                    stage.clear();
                    stage.setUpStageCreator();
                }
                return true;
            }
        });

        health.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (playerHandler.getCurrentCurrency() > playerHandler.getHealthUpgradeCost()) {
                    playerHandler.removeCurrency((int) playerHandler.getHealthUpgradeCost());
                    playerHandler.healthLevel++;
                    stage.clear();
                    stage.setUpStageCreator();
                }
                return true;
            }
        });

        stage.addActor(health);
        stage.addActor(damage);
        stage.addActor(multiShot);
        stage.addActor(speed);
    }

    public void setUpCreatorButton() {
        TextButton creatorButton = new TextButton("View Available Upgrades", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
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

    private void writeLevel() {
        FileManager.writePlanet(placedBuildings);
    }

    public void touchDown(float x, float y) {

    }
}
