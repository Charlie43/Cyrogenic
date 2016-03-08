package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.actors.Asteroid;
import com.project.charlie.cyrogenic.data.AsteroidActorData;
import com.project.charlie.cyrogenic.data.BulletActorData;
import com.project.charlie.cyrogenic.data.PlayerUserData;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 05/03/2016.
 */
public class ObstacleGameHandler extends GameHandler {
    int asteroidCount;

    public ObstacleGameHandler(GameStage stage) {
        super(stage);
        asteroidCount = 0;

//        MainGameTouchHandler mainGameHandler = new MainGameTouchHandler(this); // todo asteroid touchhandler
    }

    public void createAsteroid() { // todo as we run lower on asteroids, apply affects to players ship to simulate entering a planet
        Asteroid asteroid = new Asteroid(WorldHandler.createObstacle(stage.getWorld(), stage.getStageHandler()));
        AsteroidActorData data = (AsteroidActorData) asteroid.getActorData();
        data.asteroid = asteroid;
        stage.addAsteroid(asteroid);
        stage.addActor(asteroid);
        asteroidCount++;
        Gdx.app.log("COUNT", "AsteroidLevel Count: " + asteroidCount + " / " + stage.getStageHandler().getAsteriodCount());
        if (asteroidCount >= stage.getStageHandler().getAsteriodCount()) {
            Gdx.app.log("COUNT", "AsteroidLevel complete");
            stage.setUpNormalLevel();
        }
    }

    public void setUpObstaclesButton() {
        TextButton obstacleButton = new TextButton("Stage One: Planetary Entrance", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        obstacleButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2);
        obstacleButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2, 250, 40);
        obstacleButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setUpObstacleLevel();
                return true;
            }
        });
        stage.addActor(obstacleButton);
    }

    public void handleContact(Body a, Body b) {
        boolean playerHitAsteroid = (WorldHandler.isAsteroid(a) && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && WorldHandler.isAsteroid(b);
        boolean bulletHitAsteroid = (a.isBullet() && WorldHandler.isAsteroid(b)) ||
                b.isBullet() && WorldHandler.isAsteroid(a);
        Body asteroid;
        Body player;
        Body bullet;
        if (playerHitAsteroid) {
            if (WorldHandler.isAsteroid(a)) {
                asteroid = a;
                player = b;
            } else {
                asteroid = b;
                player = a;
            }
            PlayerUserData p_data = (PlayerUserData) player.getUserData();
            AsteroidActorData a_data = (AsteroidActorData) asteroid.getUserData();
            if (a_data.subHealth(5) <= 0) {
                a_data.isRemoved = true;
                stage.addDead(asteroid);
            }

            if (p_data.subHealth(15) <= 0) {
                // game over
                Gdx.app.log("plr", "plr dead");
            }
            if (infoLabel != null)
                infoLabel.setText("Player HP: " + p_data.getHealth());
        }
        if (bulletHitAsteroid) {
            if (a.isBullet()) {
                bullet = a;
                asteroid = b;
            } else {
                bullet = b;
                asteroid = a;
            }
            BulletActorData b_data = (BulletActorData) bullet.getUserData();
            AsteroidActorData a_data = (AsteroidActorData) asteroid.getUserData();

            b_data.isRemoved = true;
            stage.addDead(bullet);

            if (a_data.subHealth(15) <= 0) {
                stage.addDead(asteroid);
                a_data.isRemoved = true;
            }
        }

    }

}
