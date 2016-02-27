package com.project.charlie.cyrogenic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.project.charlie.cyrogenic.actors.*;
import com.project.charlie.cyrogenic.data.AsteroidActorData;
import com.project.charlie.cyrogenic.data.BulletActorData;
import com.project.charlie.cyrogenic.data.PlayerUserData;
import com.project.charlie.cyrogenic.data.TurretActorData;
import com.project.charlie.cyrogenic.handlers.StageHandler;
import com.project.charlie.cyrogenic.handlers.WorldHandler;
import com.project.charlie.cyrogenic.managers.LevelManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.ui.GameLabel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Charlie on 12/02/2016.
 */
public class GameStage extends Stage implements ContactListener {
    private Box2DDebugRenderer renderer;
    OrthographicCamera camera;
    World world;
    Random random = new Random();

    private LevelManager levelManager;
    private StageHandler stageHandler;
    private Player player;
    private Boundary boundaryBottom;
    private Boundary boundaryTop;
    private Boundary boundaryLeft;
    private Boundary boundaryRight;


    TextButton obstacleButton;
    TextButton normalButton;

    private ArrayList<Turret> turrets = new ArrayList<Turret>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    private int bulletsToCreate;
    private float accumulator = 0f;
    private final float TIME_STEP = 1 / 300f;

    static int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    static int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private Vector3 touchPoint;
    private Touchpad touchPad;

    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    int asteroidCount = 0;

    private ArrayList<Body> dead = new ArrayList<Body>();


    String infoLabelString = "Player HP: %hp%\n%tatkspd%";
    GameLabel infoLabel;
    GameLabel displayedLabel;


    /**
     * todo
     * touch based placement of turrets
     * same view as playing mode
     * ability to switch stages
     * just use touchpoint to spawn turrets in place, and write to XML file
     * <p/>
     * <p/>
     * EVERYTHING is just a background and actors
     * planet to attack map, etc
     * dont overomcplicate
     */


    public GameStage() {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        Gdx.input.setInputProcessor(this);
        touchPoint = new Vector3();

        setUpCamera();
        setUpStage(1);
        setUpMenu();
        if (Constants.DEBUG)
            renderer = new Box2DDebugRenderer();

        // set up camera, stage, etc
        // audio
    }


    public void setUpMenu() {
        setUpObstaclesButton();
        setUpNormalButton();
    }

    public void setUpObstaclesButton() {
        obstacleButton = new TextButton("Stage One: Planetary Entrance", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        obstacleButton.setPosition(getCamera().viewportWidth / 3, getCamera().viewportHeight / 2);
        obstacleButton.setBounds(getCamera().viewportWidth / 3, getCamera().viewportHeight / 2, 250, 40);
        obstacleButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setUpObstacleLevel();
                return true;
            }
        });
        addActor(obstacleButton);
    }

    public void setUpNormalButton() {
        normalButton = new TextButton("Stage Two: Planet Assault", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        normalButton.setPosition(getCamera().viewportWidth / 3, getCamera().viewportHeight / 2 - 60f);
        normalButton.setBounds(getCamera().viewportWidth / 3, getCamera().viewportHeight / 2 - 60f, 250, 40);
        normalButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setUpNormalLevel();
                return true;
            }
        });
        addActor(normalButton);
    }

    public void loadLevel() {
        stageHandler = LevelManager.parseLevel(world);
    }

    public void setUpObstacleLevel() {
        clear();
        setUpStage(0);
        setUpPlayer();
        loadLevel();
        setUpBoundaries();
        setUpControls();
        setUpInfoText();
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                createAsteroid();
            }
        }, 4, stageHandler.getAsteriodInterval(), stageHandler.getAsteriodCount());

    }

    public void setUpNormalLevel() {
        clear();
        setUpStage(1);
        setUpPlayer();
        if (stageHandler == null)
            loadLevel();
        setUpTurrets();
        setUpBoundaries();
        setUpControls();
        setUpStageCompleteLabel();
        setUpInfoText();
    }

    public void setUpStageCompleteLabel() {
        Rectangle bounds = new Rectangle(getCamera().viewportWidth / 2.7f, getCamera().viewportHeight / 1.2f, 100, 20);
        displayedLabel = new GameLabel(bounds, "Planet Entry complete", 1f);
        addActor(displayedLabel);
        displayedLabel.addAction(Actions.fadeOut(10));
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                displayedLabel.addAction(Actions.removeActor());
                Gdx.app.log("LABEL", "removed");
            }
        }, 10, 100);

    }


    public String getDebugText() {
        String text = infoLabelString.replace("%hp%", player.getActorData().getHealth() + "");
        if (Constants.DEBUG) {
            if (turrets.size() > 0) { // todo correct way of checking current stage (gamemanager)
                String tempText = "";
                for (Turret turret : turrets) {
                    tempText = tempText + String.format("\nTurret %d HP: %f SPD: %f", turrets.indexOf(turret),
                            turret.getActorData().getHealth(), turret.getActorData().getFireRate());

                }

                text = text.replace("%tatkspd%", tempText);
            } else
                text = text.replace("%tatkspd%", "");
        } else
            text = text.replace("%tatkspd%", "");

        return text;
    }


    public void setUpInfoText() {
        Rectangle bounds = new Rectangle(getCamera().viewportWidth / 2.7f, getCamera().viewportHeight / 1.5f, 100, 20);
        infoLabel = new GameLabel(bounds, getDebugText(), 0.7f);
        addActor(infoLabel);
    }

    public void setUpStage(int stage) {
        setUpWorld();
        setUpBackground(stage);
    }

    public void setUpControls() {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground_new.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob_new.png"));
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
        addActor(touchPad);
    }

    public void setUpBoundaries() {
        boundaryTop = new Boundary(WorldHandler.createBoundary(world, Constants.TOP));
        boundaryBottom = new Boundary(WorldHandler.createBoundary(world, Constants.BOTTOM));
        boundaryLeft = new Boundary(WorldHandler.createHorizontalBoundary(world, 0));
        boundaryRight = new Boundary(WorldHandler.createHorizontalBoundary(world, 1));
        addActor(boundaryTop);
        addActor(boundaryBottom);
        addActor(boundaryLeft);
        addActor(boundaryRight);
    }

    public void setUpWorld() {
        world = WorldHandler.createWorld();
        world.setContactListener(this);
    }

    public void setUpBackground(int stage) {
        addActor(new Background(stage));
    }

    public void setUpCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    public void setUpPlayer() {
        // null check
        player = new Player(WorldHandler.createPlayer(world));
        addActor(player);
    }

    public void setUpTurrets() { // TODO: handle stages
        for (StageHandler.TurretTemplate turret : stageHandler.getTurrets()) {
            Turret temp = new Turret(WorldHandler.createTurret(world, turret.getX(), turret.getY(), turret.getFireRate()));
            temp.getActorData().turret = temp;
            addActor(temp);
            turrets.add(temp);
        }
    }

    public void createAsteroid() { // todo as we run lower on asteroids, apply affects to players ship to simulate entering a planet
        Asteroid asteroid = new Asteroid(WorldHandler.createObstacle(world, stageHandler));
        AsteroidActorData data = (AsteroidActorData) asteroid.getActorData();
        asteroids.add(asteroid);
        data.asteroid = asteroid;
        addActor(asteroid);
        asteroidCount++;
        Gdx.app.log("COUNT", "Asteroid Count: " + asteroidCount + " / " + stageHandler.getAsteriodCount());
        if (asteroidCount >= stageHandler.getAsteriodCount()) {
            Gdx.app.log("COUNT", "Asteroid complete");
            setUpNormalLevel();
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        // if paused -> return
        // Fixed timestep
        createBullets(); // force all actor creation to occur before stepping
        accumulator += delta;
        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
        if (Constants.DEBUG)
            renderer.render(world, camera.combined);

        checkBounds();

        removeDeadBodies();
    }

    private void createBullets() {
        for (int i = 0; i < bulletsToCreate; i++) {
            Bullet bullet = new Bullet(WorldHandler.createBullet(world, player.getX() + 55, player.getY() + 15, Constants.PLAYER_ASSET_ID));
            BulletActorData data = bullet.getActorData();
            data.bullet = bullet;
            addActor(bullet);
            bullets.add(bullet);
        }
        bulletsToCreate = 0;
        if (turrets.size() > 0) {
            for (Turret turret : turrets) {
                //todo define these numbers by difficulty?
                boolean checkFireRate = ((System.currentTimeMillis() - turret.getLastFiretime()) / 1000) > turret.getActorData().getFireRate();
                if (random.nextFloat() > 0.3 && checkFireRate) {
                    turret.setLastFiretime(System.currentTimeMillis());
                    Bullet bullet = new Bullet(WorldHandler.createBullet(world, turret.getX() - 55, turret.getY() + 15, Constants.TURRET_ASSET_ID));
                    BulletActorData data = bullet.getActorData();
                    data.bullet = bullet;
                    addActor(bullet);
                    bullets.add(bullet);
                }
            }
        }
    }

    private void checkBounds() {
        for (Bullet bullet : bullets) {
            if (bullet.getX() + bullet.getWidth() > VIEWPORT_WIDTH) {
                dead.add(bullet.getBody());
                bullet.getActorData().isRemoved = true;
            }
        }
        for (Asteroid asteroid : asteroids) {
            boolean leftBounds = (asteroid.getX() + asteroid.getWidth() < 0);
            boolean bottomBounds = (asteroid.getY() + asteroid.getHeight() < 0);
            boolean topBounds = (asteroid.getY() + asteroid.getHeight() > VIEWPORT_HEIGHT);
            if (leftBounds || bottomBounds || topBounds) {
                dead.add(asteroid.getBody());
                asteroid.getActorData().isRemoved = true;
            }
        }
    }

    private synchronized void removeDeadBodies() {
        for (int j = 0; j < dead.size(); j++) {
            Body body = dead.get(j);
            if (body != null && !world.isLocked()) {
                if (body.isBullet()) {
                    BulletActorData b_data = (BulletActorData) body.getUserData();
                    if (b_data != null && b_data.isRemoved) {
                        if (bullets.contains(b_data.bullet))
                            bullets.remove(b_data.bullet);
                        b_data.bullet.addAction(Actions.removeActor());
                        world.destroyBody(body);
                        body.setUserData(null);
                        dead.remove(body);
                        body = null;
                    }
                } else if (WorldHandler.isTurret(body)) {
                    TurretActorData t_data = (TurretActorData) body.getUserData();
                    if (t_data != null && t_data.isRemoved && turrets.contains(t_data.turret)) {
                        turrets.remove(t_data.turret);
                        t_data.turret.addAction(Actions.removeActor());
                        world.destroyBody(body);
                        body.setUserData(null);
                        dead.remove(body);
                        body = null;
                    }
                } else if (WorldHandler.isAsteroid(body)) {
                    AsteroidActorData a_data = (AsteroidActorData) body.getUserData();
                    if (a_data != null && a_data.isRemoved && asteroids.contains(a_data.asteroid)) {
                        asteroids.remove(a_data.asteroid);
                        a_data.asteroid.addAction(Actions.removeActor());
                        world.destroyBody(body);
                        body.setUserData(null);
                        body = null;
                    }
                }
            }
        }
    }

//    // todo implement rotate
//    angle = (float) Math.atan2(touchPoint.y - player.getY(), touchPoint.x - player.getX());
//    angle = (float) (angle * (180/Math.PI));

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        translateScreenToWorldCoordinates(x, y);

        int activeTouch = 0;
        for (int i = 0; i < 2; i++) {
            if (Gdx.input.isTouched(i)) activeTouch++;
        }

        if (player != null) {
            player.applyForce(touchPad.getKnobPercentX() * 10,
                    touchPad.getKnobPercentY() * 10);
            if (activeTouch > 1) {
                bulletsToCreate++;
            }
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        translateScreenToWorldCoordinates(x, y);
        if (player != null) {
            player.applyForce(touchPad.getKnobPercentX() * 10, touchPad.getKnobPercentY() * 10);
        }

        return super.touchDragged(x, y, pointer);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        int activeTouch = 0;
        for (int i = 0; i < 2; i++) {
            if (Gdx.input.isTouched(i)) activeTouch++;
        }
        if (player != null && player.isMoving() && activeTouch == 0) { // If we've still got a finger on the screen, we've just been shooting + moving and stopped shooting
            player.setMoving(false);
            player.stopMoving();
        }
        return super.touchUp(x, y, pointer, button);

    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (WorldHandler.getDataType(a) == null ||
                WorldHandler.getDataType(b) == null)
            return;

        Body bullet;
        Body turret;
        Body asteroid;
        Body player;

        boolean bulletHitTurret = ((a.isBullet() && WorldHandler.isTurret(b)) ||
                (WorldHandler.isTurret(a) && b.isBullet()));
        boolean playerHitAsteroid = (WorldHandler.isAsteroid(a) && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && WorldHandler.isAsteroid(b);
        boolean bulletHitAsteroid = (a.isBullet() && WorldHandler.isAsteroid(b)) ||
                b.isBullet() && WorldHandler.isAsteroid(a);
        boolean bulletHitPlayer = ((a.isBullet() && WorldHandler.isPlayer(b)) ||
                WorldHandler.isPlayer(a) && b.isBullet());

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
            dead.add(bullet);

            TurretActorData t_data = (TurretActorData) turret.getUserData();
            if (t_data.subHealth(10)) {
                t_data.isRemoved = true;
                dead.add(turret);
                // death animation and stuff
            }
            Gdx.app.log("TURRET", "Turret HP:" + t_data.getHealth());
        }
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
                dead.add(asteroid);
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
            dead.add(bullet);

            if (a_data.subHealth(15) <= 0) {
                dead.add(asteroid);
                a_data.isRemoved = true;
            }
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

            dead.add(bullet);

            if (p_data.subHealth(7) <= 0) {
                Gdx.app.log("PLAYER", "You died!");
            }
            infoLabel.setText(getDebugText());
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
