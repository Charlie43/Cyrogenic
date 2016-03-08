package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.project.charlie.cyrogenic.actors.Bullet;
import com.project.charlie.cyrogenic.actors.Player;
import com.project.charlie.cyrogenic.actors.Turret;
import com.project.charlie.cyrogenic.data.BulletActorData;
import com.project.charlie.cyrogenic.data.PlayerUserData;
import com.project.charlie.cyrogenic.data.TurretActorData;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.TurretJSON;
import com.project.charlie.cyrogenic.ui.GameLabel;

import java.util.Random;

/**
 * Created by Charlie on 05/03/2016.
 */
public class GameHandler {

    GameStage stage;
    public int gameMode;
    private Player player;
    private Touchpad touchPad;
    GameLabel infoLabel;
    Random random;

    public GameHandler(GameStage stage) {
        this.stage = stage;
        random = new Random();
    }


    public void setUpControls() {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture(Constants.TOUCH_BG_PATH));
        touchpadSkin.add("touchKnob", new Texture(Constants.TOUCH_KNOB_PATH));
        // Style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        // create drawables
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        // Apply drawables
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchPad = new Touchpad(10, touchpadStyle);
        touchPad.setBounds(10, 10, 120, 120);
        stage.addActor(touchPad);
    }


    public void setUpPlayer() {
        // null check
        player = new Player(WorldHandler.createPlayer(stage.getWorld()));
        stage.addActor(player);
        Gdx.app.log("GH", "Player created");
    }

    public void setUpTurrets(StageHandler stageHandler) { // TODO: handle stages
        if (stageHandler == null)
            stageHandler = stage.getStageHandler();

        Gdx.app.log("GH", String.format("Setting up %d turrets.", stageHandler.getTurrets().size()));

        for (TurretJSON turret : stageHandler.getTurrets()) {
            Turret temp = new Turret(WorldHandler.createTurret(stage.getWorld(), turret.getX(), turret.getY(), turret.getWidth(), turret.getHeight(),
                    turret.getFireRate()));
            temp.getActorData().turret = temp;
            stage.addActor(temp);
            stage.addTurret(temp);
        }

    }

    public void setUpInfoText() {
        Rectangle bounds = new Rectangle(stage.getCamera().viewportWidth / 2.7f, stage.getCamera().viewportHeight / 1.5f, 100, 20);
        infoLabel = new GameLabel(bounds, stage.getDebugText(), 0.7f);
        stage.addActor(infoLabel);
    }

    public void setUpStageCompleteLabel() {
        GameLabel label = stage.createLabel("Planet Entry completed", new Vector3(stage.getCamera().viewportWidth / 3,
                stage.getCamera().viewportHeight / 3, 0), 100, 20, 10);
        label.addAction(Actions.fadeOut(10));

//        new Timer().scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//                displayedLabel.addAction(Actions.removeActor());
//                Gdx.app.log("LABEL", "removed");
//            }
//        }, 10, 100);
    }

    public void setUpNormalButton() {
        TextButton normalButton = new TextButton("Stage Two: Planet Assault", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        normalButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 60f);
        normalButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 60f, 250, 40);
        normalButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setUpNormalLevel();
                return true;
            }
        });
        stage.addActor(normalButton);
    }

    public void handleContact(Body a, Body b) {
        boolean bulletHitTurret = ((a.isBullet() && WorldHandler.isTurret(b)) ||
                (WorldHandler.isTurret(a) && b.isBullet()));

        boolean bulletHitPlayer = ((a.isBullet() && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && b.isBullet());
        Body bullet;
        Body turret;
        Body player;

        if (bulletHitTurret) {
            if (a.isBullet()) {
                bullet = a;
                turret = b;
            } else {
                bullet = b;
                turret = a;
            }
            BulletActorData data = (BulletActorData) bullet.getUserData();
            data.isRemoved = true;
            stage.addDead(bullet);

            TurretActorData t_data = (TurretActorData) turret.getUserData();
            if (t_data.subHealth(10)) {
                t_data.isRemoved = true;
                stage.addDead(turret);
                // death animation and stuff
            }
            Gdx.app.log("TURRET", "Turret HP:" + t_data.getHealth());
        }
        if (bulletHitPlayer) {
            if (a.isBullet()) {
                bullet = a;
                player = b;
            } else {
                player = a;
                bullet = b;
            }
            BulletActorData b_data = (BulletActorData) bullet.getUserData();
            PlayerUserData p_data = (PlayerUserData) player.getUserData();
            b_data.isRemoved = true;

            stage.addDead(bullet);

            if (p_data.subHealth(7) <= 0) {
                Gdx.app.log("PLAYER", "You died!");
            }
            infoLabel.setText(stage.getDebugText());
        }

    }

    public void createBullets(int bulletsToCreate) {
        if (gameMode != Constants.GAMEMODE_CREATOR) {
            for (int i = 0; i < bulletsToCreate; i++) {
                Bullet bullet = new Bullet(WorldHandler.createBullet(stage.getWorld(), player.getX() + 55,
                        player.getY() + 15, Constants.PLAYER_ASSET_ID));
                BulletActorData data = bullet.getActorData();
                data.bullet = bullet;
                stage.addActor(bullet);
                stage.addBullet(bullet);
            }
            stage.setBulletsToCreate(0);
            if (stage.getTurrets().size() > 0) {
                for (Turret turret : stage.getTurrets()) {
                    //todo define these numbers by difficulty?
                    boolean checkFireRate = ((System.currentTimeMillis() - turret.getLastFiretime()) / 1000) > turret.getActorData().getFireRate();
                    if (random.nextFloat() > 0.3 && checkFireRate) {
                        turret.setLastFiretime(System.currentTimeMillis());
                        Bullet bullet = new Bullet(WorldHandler.createBullet(stage.getWorld(),
                                turret.getX() - 55, turret.getY() + 15, Constants.TURRET_ASSET_ID));
                        BulletActorData data = bullet.getActorData();
                        data.bullet = bullet;
                        stage.addActor(bullet);
                        stage.addBullet(bullet);
                    }
                }
            }
        }
    }


    public void touchDown(float x, float y) {
        int activeTouch = 0;
        for (int i = 0; i < 2; i++) {
            if (Gdx.input.isTouched(i)) activeTouch++;
        }

        if (player != null) {
            player.applyForce(touchPad.getKnobPercentX() * 10,
                    touchPad.getKnobPercentY() * 10);
            if (activeTouch > 1) {
                stage.addBullet();
            }
        }
    }

    public void touchDragged(float x, float y) {
        if (player != null) {
            player.applyForce(touchPad.getKnobPercentX() * 10,
                    touchPad.getKnobPercentY() * 10);
        }
    }

    public Player getPlayer() {
        return player;
    }
}

