package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.project.charlie.cyrogenic.actors.Asteroid;
import com.project.charlie.cyrogenic.actors.Pickup;
import com.project.charlie.cyrogenic.data.ActorData;
import com.project.charlie.cyrogenic.data.AsteroidActorData;
import com.project.charlie.cyrogenic.data.PickupData;
import com.project.charlie.cyrogenic.data.PlayerActorData;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.misc.Constants;

import java.util.Random;

/**
 * Created by Charlie on 05/03/2016.
 */
public class ObstacleGameHandler extends GameHandler {
    int asteroidCount;
    boolean triggered;
    Random random;

    public ObstacleGameHandler(GameStage stage) {
        super(stage);
        asteroidCount = 0;
        triggered = false;
        random = new Random();
    }

    public void createAsteroid() { // todo as we run lower on asteroids, apply affects to players ship to simulate entering a planet
        Asteroid asteroid = new Asteroid(WorldHandler.createAsteroid(stage.getWorld(), stage.getPlanetHandler()));
        AsteroidActorData data = (AsteroidActorData) asteroid.getActorData();
        data.asteroid = asteroid;
        stage.addAsteroid(asteroid);
        asteroidCount++;
        Gdx.app.log("COUNT", "AsteroidLevel Count: " + asteroidCount + " / " + stage.getPlanetHandler().getAsteriodCount());
        if (asteroidCount == stage.getPlanetHandler().getAsteriodCount() && !triggered) {
            triggered = true;
            Gdx.app.log("COUNT", "AsteroidLevel complete");
            asteroidCount = 0;
            stage.setUpNormalLevel();
        }
        if (asteroidCount > stage.getPlanetHandler().getAsteriodCount()) {
            asteroid.getActorData().isRemoved = true;
            stage.addDead(asteroid.getBody());
        }
    }

    @Override
    public void updateLabel() {
        super.updateLabel();
    }

    public void setUpLabels(String name) {
        stage.addLabel("PlanetName", stage.createLabel(name,
                new Vector3(stage.getCamera().viewportWidth * 0.50f, stage.getCamera().viewportHeight - 10, 0), 20, 20, 0, 0.4f));
    }

    @SuppressWarnings("Duplicates")
    public void handleContact(Body a, Body b) {
        boolean playerHitAsteroid = (WorldHandler.isAsteroid(a) && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && WorldHandler.isAsteroid(b);
        boolean bulletHitAsteroid = (a.isBullet() && WorldHandler.isAsteroid(b)) ||
                b.isBullet() && WorldHandler.isAsteroid(a);
        boolean playerHitPickup = (WorldHandler.isPlayer(a) && WorldHandler.isPickup(b)) ||
                (WorldHandler.isPickup(a) && WorldHandler.isPlayer(b));

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
            PlayerActorData p_data = (PlayerActorData) player.getUserData();
            AsteroidActorData a_data = (AsteroidActorData) asteroid.getUserData();
            if (a_data.subHealth(5) <= 0) {
                a_data.isRemoved = true;
                stage.addDead(asteroid);
            }
            asteroid.applyForceToCenter(new Vector2(0, 50f), true);

            if (p_data.subHealth(a_data.getDamage()) <= 0) {
                // game over
                Gdx.app.log("plr", "plr dead"); // todo player death
            }
            if (infoLabel != null)
                infoLabel.setText("Player HP: " + p_data.getHealth());
            playerHPBar.setBarWidth((int) p_data.getHealth());
        }
        if (bulletHitAsteroid) {
            if (a.isBullet()) {
                bullet = a;
                asteroid = b;
            } else {
                bullet = b;
                asteroid = a;
            }
            ActorData b_data = (ActorData) bullet.getUserData();
            AsteroidActorData a_data = (AsteroidActorData) asteroid.getUserData();

            b_data.isRemoved = true;
            stage.addDead(bullet);

            if (a_data.subHealth(WorldHandler.getProjectileDamage(bullet)) <= 0) {
                Gdx.app.log("OGH", "Asteroid dead - Spawn pickup?");
//                if (random.nextInt(100) > 70) {
                final Body finalAsteroid = asteroid;
                new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        createPickup(finalAsteroid.getPosition().x, finalAsteroid.getPosition().y);
                    }
                }, 2);
//                }

                stage.addDead(asteroid);
                a_data.isRemoved = true;
            }
        }
        Body pickup;
        if (playerHitPickup) {
            if (WorldHandler.isPlayer(a)) {
                player = a;
                pickup = b;
            } else {
                player = b;
                pickup = a;
            }
            PickupData pData = (PickupData) pickup.getUserData();
            PlayerActorData plData = (PlayerActorData) player.getUserData();

            pData.isRemoved = true;
            stage.addDead(pickup);

            if (pData.getType().equals(Constants.CURRENCY_PICKUP_ASSET_ID))
                playerHandler.addCurrency(100);
            else
                plData.addHealth(50); // todo magic numbers
        }
        // todo if player hits pickup
    }

    public void createPickup(float x, float y) {
        Pickup pickUpDropped;
        if (random.nextInt(2) == 1) {
            pickUpDropped = new Pickup(WorldHandler.createPickup(stage.getWorld(), x,
                    y, Constants.CURRENCY_PICKUP_ASSET_ID), Constants.CURRENCY_PICKUP_ASSET_ID);
        } else {
            pickUpDropped = new Pickup(WorldHandler.createPickup(stage.getWorld(), x,
                    y, Constants.HP_PICKUP_ASSET_ID), Constants.HP_PICKUP_ASSET_ID);
        }
        pickUpDropped.getActorData().pickup = pickUpDropped;
        stage.addActor(pickUpDropped);
    }
}
