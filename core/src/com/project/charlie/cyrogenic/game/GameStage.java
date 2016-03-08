package com.project.charlie.cyrogenic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.project.charlie.cyrogenic.actors.*;
import com.project.charlie.cyrogenic.data.AsteroidActorData;
import com.project.charlie.cyrogenic.data.BulletActorData;
import com.project.charlie.cyrogenic.data.TurretActorData;
import com.project.charlie.cyrogenic.handlers.*;
import com.project.charlie.cyrogenic.managers.LevelManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.ui.GameLabel;

import java.util.ArrayList;

/**
 * Created by Charlie on 12/02/2016.
 */
public class GameStage extends Stage implements ContactListener {
    private Box2DDebugRenderer renderer;
    OrthographicCamera camera;
    World world;

    private GameHandler gameHandler;
    private ObstacleGameHandler obstacleGameHandler;
    private CreatorHandler creatorHandler;
    private LevelManager levelManager;
    private StageHandler stageHandler;
    private Boundary boundaryBottom;
    private Boundary boundaryTop;
    private Boundary boundaryLeft;
    private Boundary boundaryRight;


    private ArrayList<Turret> turrets = new ArrayList<Turret>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    private int bulletsToCreate;
    private float accumulator = 0f;
    private final float TIME_STEP = 1 / 300f;

    static int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    static int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private Vector3 touchPoint;

    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>(); // todo reset arraylists when changing mode

    private ArrayList<Body> dead = new ArrayList<Body>();


    String infoLabelString = "Player HP: %hp%\n%tatkspd%";


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
     * dont over complicate
     * <p/>
     * Way of managing different stages - Base building stage, planet map stage, etc
     * - IE ways of handling touch inputs for each stage (seperate controllers for each stage)
     */


    public GameStage() {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));

        Gdx.input.setInputProcessor(this);
        touchPoint = new Vector3();
        gameHandler = new GameHandler(this);
        obstacleGameHandler = new ObstacleGameHandler(this);
        creatorHandler = new CreatorHandler(this);
//        LevelManager.writeLevel(null);

        setUpMenu();

        if (Constants.DEBUG)
            renderer = new Box2DDebugRenderer();

        // todo audio & animation
    }

    public void setUpMenu() {
        clear();
        setUpPreChoice();
        obstacleGameHandler.setUpObstaclesButton();
        gameHandler.setUpNormalButton();
        creatorHandler.setUpCreatorButton();
    }

    public void setUpPreChoice() {
        setUpCamera();
        setUpStage(1);
    }

    public void setUpNormalLevel() {
        gameHandler.gameMode = Constants.GAMEMODE_NORMAL;
        clear();
        setUpStage(1);
        gameHandler.setUpPlayer();
        if (stageHandler == null)
            loadLevel();
        gameHandler.setUpTurrets(stageHandler);
        setUpBoundaries();
        gameHandler.setUpControls();
        gameHandler.setUpStageCompleteLabel();
        gameHandler.setUpInfoText();
    }

    public void setUpObstacleLevel() {
        gameHandler.gameMode = Constants.GAMEMODE_OBSTACLES;
        clear();
        setUpStage(0);
        obstacleGameHandler.setUpPlayer();
        loadLevel();
        setUpBoundaries();
        obstacleGameHandler.setUpControls();
        obstacleGameHandler.setUpInfoText();
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                obstacleGameHandler.createAsteroid();
            }
        }, 4, stageHandler.getAsteriodInterval(), stageHandler.getAsteriodCount());
    }

    public void setUpStageCreator() {
        gameHandler.gameMode = Constants.GAMEMODE_CREATOR;
        clear();
        setUpStage(1);
        setUpBoundaries();
        creatorHandler.loadBase();
        creatorHandler.setUpButtons();
        creatorHandler.setUpFinishButton();
        creatorHandler.setUpResetButton();
    }


    public void loadLevel() {
        stageHandler = LevelManager.parseJSONLevel(world);
//        stageHandler = LevelManager.parseLevel(world);
    }

    public GameLabel createLabel(String text, Vector3 bounds, float width, float height, int fadeOut) {
        Rectangle rect = new Rectangle(bounds.x, bounds.y, width, height);
        GameLabel toReturn = new GameLabel(rect, text, 1f);
        addActor(toReturn);
        if (fadeOut > 0)
            toReturn.addAction(Actions.fadeOut(fadeOut));
        return toReturn;
    }

    public String getDebugText() {
        String text = infoLabelString;
        if (Constants.DEBUG) {
            if (gameHandler.getPlayer() != null)
                text = infoLabelString.replace("%hp%", gameHandler.getPlayer().getActorData().getHealth() + "");
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

    public void setUpStage(int stage) {
        setUpWorld();
        setUpBackground(stage);
    }

    public void setUpWorld() {
        world = WorldHandler.createWorld();
        world.setContactListener(this);
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


    public void setUpBackground(int stage) {
        addActor(new Background(stage));
    }

    public void setUpCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
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
        if (gameHandler.gameMode != Constants.GAMEMODE_CREATOR) {
            if (gameHandler.gameMode == Constants.GAMEMODE_OBSTACLES)
                obstacleGameHandler.createBullets(bulletsToCreate);
            else if (gameHandler.gameMode == Constants.GAMEMODE_NORMAL)
                gameHandler.createBullets(bulletsToCreate);
        }
    }
//    private void createBullets() {
//        if (gameHandler.gameMode != Constants.GAMEMODE_CREATOR) {
//            for (int i = 0; i < bulletsToCreate; i++) {
//                Bullet bullet = new Bullet(WorldHandler.createBullet(world, gameHandler.getPlayer().getX() + 55,
//                        gameHandler.getPlayer().getY() + 15, Constants.PLAYER_ASSET_ID));
//                BulletActorData data = bullet.getActorData();
//                data.bullet = bullet;
//                addActor(bullet);
//                bullets.add(bullet);
//            }
//            bulletsToCreate = 0;
//            if (turrets.size() > 0) {
//                for (Turret turret : turrets) {
//                    //todo define these numbers by difficulty?
//                    boolean checkFireRate = ((System.currentTimeMillis() - turret.getLastFiretime()) / 1000) > turret.getActorData().getFireRate();
//                    if (random.nextFloat() > 0.3 && checkFireRate) {
//                        turret.setLastFiretime(System.currentTimeMillis());
//                        Bullet bullet = new Bullet(WorldHandler.createBullet(world, turret.getX() - 55, turret.getY() + 15, Constants.TURRET_ASSET_ID));
//                        BulletActorData data = bullet.getActorData();
//                        data.bullet = bullet;
//                        addActor(bullet);
//                        bullets.add(bullet);
//                    }
//                }
//            }
//        }
//    }

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


    public Vector3 translateScreenToWorldCoordinates(int x, int y) {
        touchPoint.set(getCamera().unproject(new Vector3(x, y, 0)));
        return touchPoint;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        translateScreenToWorldCoordinates(x, y);
        switch (gameHandler.gameMode) {
            case Constants.GAMEMODE_CREATOR:
                creatorHandler.touchDown(touchPoint.x, touchPoint.y);
                break;
            case Constants.GAMEMODE_NORMAL:
                gameHandler.touchDown(touchPoint.x, touchPoint.y);
                break;
            case Constants.GAMEMODE_OBSTACLES:
                obstacleGameHandler.touchDown(touchPoint.x, touchPoint.y);
                break;
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        translateScreenToWorldCoordinates(screenX, screenY);
        switch (gameHandler.gameMode) {
            case Constants.GAMEMODE_CREATOR:
                creatorHandler.touchDragged(touchPoint.x, touchPoint.y);
                break;
            case Constants.GAMEMODE_NORMAL:
                gameHandler.touchDragged(touchPoint.x, touchPoint.y);
                break;
            case Constants.GAMEMODE_OBSTACLES:
                obstacleGameHandler.touchDragged(touchPoint.x, touchPoint.y);
                break;
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void beginContact(Contact contact) { // todo call separate handlers depending on current game mode?
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (WorldHandler.getDataType(a) == null ||
                WorldHandler.getDataType(b) == null)
            return;

        switch (gameHandler.gameMode) {
            case Constants.GAMEMODE_OBSTACLES:
                obstacleGameHandler.handleContact(a, b);
                break;
            case Constants.GAMEMODE_NORMAL:
                gameHandler.handleContact(a, b);
                break;
            case Constants.GAMEMODE_CREATOR:
                break;
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


    public LevelManager getLevelManager() {
        return levelManager;
    }

    public StageHandler getStageHandler() {
        return stageHandler;
    }



    public void addBullet() {
        bulletsToCreate++;
    }

    public World getWorld() {
        return world;
    }

    public void addTurret(Turret turret) {
        turrets.add(turret);
    }

    public void addAsteroid(Asteroid asteroid) {
        asteroids.add(asteroid);

    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void addDead(Body body) {
        dead.add(body);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void setBulletsToCreate(int bulletsToCreate) {
        this.bulletsToCreate = bulletsToCreate;
    }

    public ArrayList<Turret> getTurrets() {
        return turrets;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}
