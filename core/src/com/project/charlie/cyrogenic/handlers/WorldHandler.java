package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.project.charlie.cyrogenic.data.*;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.PlanetJSON;

import java.util.Random;

/**
 * Created by Charlie on 12/02/2016.
 */
public class WorldHandler {
    static Random random = new Random();

    public static World createWorld() {
        return new World(Constants.GRAVITY, true);
    }

    public static Body createPlayer(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.PLAYER_X,
                Constants.PLAYER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.PLAYER_GRAVITY);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.PLAYER_ENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY | Constants.BOUNDARY_ENTITY | Constants.ASTERIODENTITY | Constants.TURRET_BULLET_ENTITY;
        body.createFixture(fixtureDef);

        body.resetMassData();
        body.setUserData(new PlayerUserData(
                Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createBullet(World world, float createX, float createY, String shotBy) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        createX = Constants.ConvertToBox(createX);
        createY = Constants.ConvertToBox(createY);
        bodyDef.position.set(new Vector2(createX, createY));

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.BULLET_WIDTH / 2, Constants.BULLET_HEIGHT / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        if (shotBy.equals(Constants.PLAYER_ASSET_ID)) {
            fixtureDef.filter.categoryBits = Constants.BULLET_ENTITY;
            fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY | Constants.ASTERIODENTITY;
        } else {
            fixtureDef.filter.categoryBits = Constants.TURRET_BULLET_ENTITY;
            fixtureDef.filter.maskBits = Constants.ASTERIODENTITY | Constants.PLAYER_ENTITY;
        }

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setGravityScale(Constants.DEFUALT_GRAVITY);
        body.setBullet(true);


        body.resetMassData();
        body.setUserData(new BulletActorData(Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT));
        body.setLinearVelocity(
                (shotBy.equals(Constants.PLAYER_ASSET_ID) ? Constants.BULLET_SPEED : -Constants.BULLET_SPEED), 0);

        return body;
    }

    public static Body createTurret(World world, float x, float y, float width, float height, float fireRate) {
        if (x == 0 && y == 0) {
            x = Constants.TURRET_X;
            y = Constants.TURRET_Y;
        }

        Gdx.app.log("TURRET", String.format("Creating turret with X: %f Y: %f Width: %f Height: %f", x, y, width, height));
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(x, y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        Body body = world.createBody(bodyDef);

        body.setGravityScale(Constants.DEFUALT_GRAVITY);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.DEFAULT_ENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY | Constants.BOUNDARY_ENTITY
                | Constants.PLAYER_ENTITY | Constants.BULLET_ENTITY;

        fixtureDef.density = Constants.TURRET_DENSITY;
        body.createFixture(fixtureDef);

        body.setUserData(new TurretActorData(width, height, 100f, fireRate));
        body.resetMassData();

        shape.dispose();
        return body;
    }

    public static Body createObstacle(World world, PlanetHandler planetHandler) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        float randomNum = random.nextFloat();

        bodyDef.position.set(Constants.ConvertToBox(Constants.APP_WIDTH),
                Constants.ConvertToBox((Constants.APP_HEIGHT) * randomNum));

        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.ASTEROID_WIDTH / 2, Constants.ASTEROID_HEIGHT / 2);
        fixtureDef.shape = shape;
        shape.dispose();

        fixtureDef.filter.categoryBits = Constants.ASTERIODENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY | Constants.BULLET_ENTITY | Constants.PLAYER_ENTITY;
        fixtureDef.density = Constants.ASTEROID_DENSITY;


        body.createFixture(fixtureDef);
        body.setUserData(new AsteroidActorData(Constants.ASTEROID_WIDTH, Constants.ASTEROID_HEIGHT, 50f));
        body.resetMassData();
        body.setGravityScale(0);

        body.setLinearVelocity(-planetHandler.getAsteriodSpeed(), 0f);
        return body;
    }

    public static Body createHorizontalBoundary(World world, int pos) {
        float height = Constants.APP_HEIGHT;
        float x = (pos == 0) ? 0 : Constants.ConvertToBox(Constants.APP_WIDTH / 2); // Camera is viewing APP_WIDTH / 2 (400) not the full 800.

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, 0);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(.1f, height / 2, new Vector2(x, Constants.APP_HEIGHT / 2), 0);

        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = Constants.BOUNDARY_ENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        polygonShape.dispose();
        return body;
    }

    public static Body createBoundary(World world, int pos) {

        float width = Gdx.graphics.getWidth();
        float height = (pos == Constants.BOTTOM) ? 0 : Constants.ConvertToBox(Constants.APP_HEIGHT);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        FixtureDef fixtureDef = new FixtureDef();
        EdgeShape edgeShape = new EdgeShape();

        edgeShape.set(-width / 2, height, width / 2, height);
        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = Constants.BOUNDARY_ENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        edgeShape.dispose();
        return body;
    }

    public static Body createPlanet(World world, PlanetJSON planet) {
        float width = planet.size;
        float height = planet.size;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(planet.x, planet.y);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width / 2, height / 2);
        fixtureDef.shape = shape;
        shape.dispose();

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new PlanetData(width, height, planet.name, planet.type));
        return body;
    }


    public static String getDataType(Body body) {
        if (body == null)
            return null;
        ActorData data = (ActorData) body.getUserData();
        return (data != null) ? data.getDataType() : null;
    }

    public static boolean isAsteroid(Body body) {
        if (body == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Asteroid");
    }

    public static boolean isPlayer(Body body) {
        if (body == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Player");
    }

    public static boolean isTurret(Body body) {
        if (body == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Turret");
    }

    public static boolean isPlanet(Body body) {
        if (body == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Planet");
    }
}
