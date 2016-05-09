package com.project.charlie.cryogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.project.charlie.cryogenic.actors.*;
import com.project.charlie.cryogenic.data.*;
import com.project.charlie.cryogenic.game.GameStage;
import com.project.charlie.cryogenic.misc.Constants;
import com.project.charlie.cryogenic.objects.TurretJSON;
import com.project.charlie.cryogenic.ui.GameLabel;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

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
    Bar playerHPBar;
    Bar targetBar;
    public Timer.Task damageTimer;
    PlayerHandler playerHandler;
    public ArrayList<Timer.Task> timers = new ArrayList<>();

    public GameHandler(GameStage stage) {
        this.stage = stage;
        random = new Random();
        gameMode = Constants.GAMEMODE_NOTHING;
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

    public void setPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    public void setUpPlayer() {
        // null check
        player = new Player(WorldHandler.createPlayer(stage.getWorld()));
        stage.addActor(player);
    }

    public void setUpTurrets(PlanetHandler planetHandler) { // TODO: handle stages
        if (planetHandler == null)
            planetHandler = stage.getPlanetHandler();

        for (TurretJSON turret : planetHandler.getTurrets()) {
            Constants.TurretType type = Constants.TurretType.valueOf(turret.getType());
            Turret temp = new Turret(WorldHandler.createTurret(stage.getWorld(), turret.getX(), turret.getY(), type.getWidth(), type.getHeight()), type); // TODO "warp in" animation
            temp.getActorData().turret = temp;
            stage.addTurret(temp);
        }
    }

    public Timer.Task pickupTimer;

    public void setUpPickupDrops() {
        Gdx.app.log("GH", "Spawning pick ups in 3...");
        pickupTimer = new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnPickup();
            }
        }, 1, 8);
    }

    public void spawnPickup() {
        Pickup pickup;
        Gdx.app.log("GH", "Spawning pick up.");
        if (random.nextInt(2) == 1) {
            pickup = new Pickup(WorldHandler.createPickup(stage.getWorld(),
                    Constants.ConvertToBox(Constants.APP_WIDTH) * random.nextFloat(), Constants.ConvertToBox(Constants.APP_HEIGHT), Constants.HP_PICKUP_ASSET_ID), Constants.HP_PICKUP_ASSET_ID);
        } else {
            pickup = new Pickup(WorldHandler.createPickup(stage.getWorld(),
                    Constants.ConvertToBox(Constants.APP_WIDTH) * random.nextFloat(), Constants.ConvertToBox(Constants.APP_HEIGHT), Constants.CURRENCY_PICKUP_ASSET_ID), Constants.CURRENCY_PICKUP_ASSET_ID);
        }
        pickup.getActorData().pickup = pickup;
        stage.addPickup(pickup);
    }

    public void setUpInfoText() {
        Rectangle bounds = new Rectangle(stage.getCamera().viewportWidth * 0.01f, stage.getCamera().viewportHeight * 0.90f, 100, 20);
        infoLabel = new GameLabel(bounds, "Currency: " + playerHandler.getCurrentCurrency(), 17f);
        stage.addActor(infoLabel);
    }

    public void updateLabel() {
        infoLabel.setText("Currency: " + playerHandler.getCurrentCurrency());
    }

    public void setUpStageCompleteLabel() {
        stage.addLabel("StageComplete", stage.createLabel("Planet Entry completed", new Vector3(stage.getCamera().viewportWidth / 3,
                stage.getCamera().viewportHeight - 10, 0), 100, 20, 10, 15));
    }

    public void setUpHPBar() {
        GameLabel hpLabel = new GameLabel(new Rectangle(stage.getCamera().viewportWidth * 0.01f, stage.getCamera().viewportHeight * 0.98f, 140, 20), "Player HP", 16f);
        stage.addActor(hpLabel);

        playerHPBar = new Bar(WorldHandler.createBar(stage.getWorld(), stage.getCamera().viewportWidth * 0.07f, stage.getCamera().viewportHeight * 0.92f, 100), Color.RED);
        stage.addActor(playerHPBar);
        Gdx.app.log("GH", "Bar created");
    }

    public void setUpTargetBar() {
        GameLabel targetLabel = new GameLabel(new Rectangle(stage.getCamera().viewportWidth * 0.80f,
                stage.getCamera().viewportHeight, 100, 20), "", 16f);
        stage.addLabel("TargetLabel", targetLabel);

        targetBar = new Bar(WorldHandler.createBar(stage.getWorld(), stage.getCamera().viewportWidth * 0.90f, stage.getCamera().viewportHeight * 0.95f, 100), Color.PINK);
        stage.addActor(targetBar);
        targetBar.addAction(Actions.hide());
    }

    @SuppressWarnings("Duplicates")
    public void handleContact(Body a, Body b) {
        boolean bulletHitTurret = ((WorldHandler.isProjectile(a) && WorldHandler.isTurret(b)) ||
                (WorldHandler.isTurret(a) && WorldHandler.isProjectile(b)));

        boolean bulletHitPlayer = ((WorldHandler.isProjectile(a) && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && WorldHandler.isProjectile(b));

        boolean playerHitPickup = (WorldHandler.isPlayer(a) && WorldHandler.isPickup(b)) ||
                (WorldHandler.isPickup(a) && WorldHandler.isPlayer(b));

        Body pickup;
        final Body bullet;
        final Body turret;
        final Body player;

        if (bulletHitTurret) {
            if (WorldHandler.isProjectile(a)) {
                bullet = a;
                turret = b;
            } else {
                bullet = b;
                turret = a;
            }
            ActorData data = (ActorData) bullet.getUserData();
            final TurretActorData t_data = (TurretActorData) turret.getUserData();

            if (data.getShotBy().equals(Constants.PLAYER_ASSET_ID)) {
                if (WorldHandler.isBullet(bullet)) {
                    data.isRemoved = true;
                    stage.addDead(bullet);
                }
                // Create contact explosion
                final float x = bullet.getPosition().x + 1f;
                final float y = bullet.getPosition().y + 0.7f;
                timers.add(new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        createExplosion(x, y);
                    }
                }, 0.05f));


                if (t_data.subHealth(WorldHandler.getProjectileDamage(bullet))) {
                    // death animation and stuff
                    if (random.nextInt(100) > 70) {
                        timers.add(new Timer().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                createPickup(turret.getPosition().x, turret.getPosition().y);
                            }
                        }, 0.1f));
                    }
                    t_data.isRemoved = true;
                    Body projectile = t_data.getTurret().getProjectile();
                    if (projectile != null && projectile.getUserData() != null) {
                        ((ActorData) projectile.getUserData()).isRemoved = true;
                        stage.addDead(projectile);
                    }
                    stage.addDead(turret);
                }

                if (!targetBar.isVisible())
                    targetBar.addAction(Actions.show());

                playerHandler.addCurrency(1);
                targetBar.setBarWidth((int) t_data.getHealth());
                stage.getLabel("TargetLabel").setText(t_data.getTurretType().toString());
                Gdx.app.log("TURRET", "Turret HP:" + t_data.getHealth());
            }
        } else if (bulletHitPlayer) {
            if (WorldHandler.isProjectile(a)) {
                bullet = a;
                player = b;
            } else {
                player = a;
                bullet = b;
            }
            ActorData b_data = (ActorData) bullet.getUserData();

            PlayerActorData p_data = (PlayerActorData) player.getUserData();
            if (WorldHandler.isBullet(bullet)) {
                b_data.isRemoved = true;
                stage.addDead(bullet);
            }

            if (p_data.subHealth(WorldHandler.getProjectileDamage(bullet)) <= 0)
                playerDead();

            if (WorldHandler.isConstantlyDamaging(bullet)) {
                Gdx.app.log("Constantly", "Hit a constantly damaging projectile.");
                damageTimer = new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        damageTarget(player, WorldHandler.getProjectileDamage(bullet));
                    }
                }, 0, 0.3f);
            }
            playerHPBar.setBarWidth((int) p_data.getHealth());
        } else if (playerHitPickup) {
            if (WorldHandler.isPlayer(a)) {
                player = a;
                pickup = b;
            } else {
                player = b;
                pickup = a;
            }
            PickupData pData = (PickupData) pickup.getUserData();
            PlayerActorData plData = (PlayerActorData) player.getUserData();

            if (pData.getType().equals(Constants.CURRENCY_PICKUP_ASSET_ID))
                playerHandler.addCurrency(100);
            else
                plData.addHealth(20); // todo magic numbers

            pData.isRemoved = true;
            stage.addDead(pickup);
        }
    }

    public void createExplosion(float x, float y) {
        Explosion explosion = new Explosion(WorldHandler.createExplosion(stage.getWorld(), x, y));
        ((ExplosionData) explosion.getActorData()).explosion = explosion;
        stage.addExplosion(explosion);
        stage.scheduleExplosionRemoval(explosion.getBody(), 0.6f, explosion);
    }

    public void playerDead() {
        stage.clear();
        stage.getTurrets().clear();
        pickupTimer.cancel();

        for (Timer.Task timer : timers)
            timer.cancel();
        timers.clear();

        stage.addLabel("Died", stage.createLabel("You have died.\nReturning to main screen.", new Vector3(stage.getCamera().viewportWidth * 0.50f, stage.getCamera().viewportHeight * 0.50f, 0),
                20, 20, 3, 20f));
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                stage.setUpMenu();
            }
        }, 4);
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
        stage.addPickup(pickUpDropped);
    }

    public void endContact(Body a, Body b) {
        boolean bulletHitPlayer = ((WorldHandler.isProjectile(a) && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && WorldHandler.isProjectile(b));
        if (bulletHitPlayer) {
            Body bullet;
            if (WorldHandler.isProjectile(a))
                bullet = a;
            else
                bullet = b;

            if (WorldHandler.isConstantlyDamaging(bullet)) {
                damageTimer.cancel();
            }
        }
    }

    public void damageTarget(Body body, float damage) {
        if (WorldHandler.isPlayer(body) && body.getUserData() != null) {
            PlayerActorData pData = (PlayerActorData) body.getUserData();
            playerHPBar.setBarWidth((int) pData.subHealth(damage));
        }
    }

    public void createBullets(int bulletsToCreate) {
        if (gameMode != Constants.GAMEMODE_CREATOR) {
            if (playerHandler.getMultiShot())
                bulletsToCreate *= 2;
            for (int i = 0; i < bulletsToCreate; i++) {
                Bullet bullet = new Bullet(WorldHandler.createBullet(stage.getWorld(), player.getBodyX() + 1,
                        player.getBodyY(), Constants.PLAYER_ASSET_ID));
                BulletActorData data = bullet.getActorData();
                data.setDamage(playerHandler.getDamage());
                data.bullet = bullet;
                stage.addProjectile(Constants.BULLET_ASSET_ID, bullet);
            }
            stage.setBulletsToCreate(0);
            if (stage.getTurrets().size() > 0) {
                for (final Turret turret : stage.getTurrets()) {

                    final Constants.TurretType type = turret.getActorData().getTurretType();
                    boolean canFire = ((System.currentTimeMillis() - turret.getLastFiretime()) / 1000) > type.getFireRate();

                    if (random.nextFloat() > 0.3 && canFire) {
                        turret.setLastFiretime(System.currentTimeMillis());
                        switch (type) {
                            case MACHINE_GUN:
                                Bullet bullet = new Bullet(WorldHandler.createBullet(stage.getWorld(),
                                        turret.getBodyX() - 2, turret.getBodyY(), Constants.TURRET_ASSET_ID));
                                BulletActorData b_data = bullet.getActorData();
                                b_data.bullet = bullet;
                                b_data.setDamage(type.getDamage());
                                stage.addProjectile(Constants.BULLET_ASSET_ID, bullet);
                                break;
                            case LASER:
                                Laser laser = new Laser(WorldHandler.createLaser(stage.getWorld(),
                                        turret.getBodyX() - Constants.LASER_WIDTH * 0.51f,
                                        turret.getBodyY(), Constants.TURRET_ASSET_ID));
                                LaserActorData l_data = laser.getActorData();
                                l_data.laser = laser;
                                l_data.setDamage(type.getDamage());
                                turret.setProjectile(laser.getBody());
                                stage.addProjectile(Constants.LASER_ASSET_ID, laser);
                                stage.scheduleRemoval(laser.getBody(), Constants.LASER_DELAY);
                                break;
                            case BURST:
                                timers.add(new Timer().scheduleTask(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        Bullet burstBullet = new Bullet(WorldHandler.createBullet(stage.getWorld(),
                                                turret.getBodyX() - 2, turret.getBodyY(), Constants.TURRET_ASSET_ID));
                                        BulletActorData burstData = burstBullet.getActorData();
                                        burstData.bullet = burstBullet;
                                        burstData.setDamage(type.getDamage());
                                        stage.addProjectile(Constants.BULLET_ASSET_ID, burstBullet);
                                    }
                                }, 0.3f, 0.3f, 2));
                                break;
                            case TESLA:
                                for (int x = 0; x < 2; x++) {
                                    Tesla tesla = new Tesla(WorldHandler.createTesla(stage.getWorld(),
                                            turret.getBodyX() + 2f,
                                            turret.getBodyY() + turret.getBodyHeight() / 2, Constants.TURRET_ASSET_ID, x),
                                            turret.getBodyX() + 2f,
                                            turret.getBodyY() + turret.getBodyHeight() / 2);
                                    TeslaActorData teslaData = tesla.getActorData();
                                    teslaData.setTesla(tesla);
                                    teslaData.setDamage(type.getDamage());
                                    turret.setProjectile(tesla.getBody());
                                    stage.addProjectile(Constants.TESLA_ASSET_ID, tesla);
                                    stage.scheduleRemoval(tesla.getBody(), Constants.TESLA_DELAY);
                                }
                                break;
                        }
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
            player.applyForce(touchPad.getKnobPercentX() * playerHandler.getSpeed(),
                    touchPad.getKnobPercentY() * playerHandler.getSpeed());
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

