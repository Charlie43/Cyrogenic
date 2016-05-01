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
        body.setUserData(new PlayerActorData(
                Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createBullet(World world, float createX, float createY, String shotBy) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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
        body.setUserData(new BulletActorData(Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT, 5f, shotBy));
        body.setLinearVelocity(
                (shotBy.equals(Constants.PLAYER_ASSET_ID) ? Constants.BULLET_SPEED : -Constants.BULLET_SPEED), 0);

        return body;
    }

    public static Body createPickup(World world, float createX, float createY, String type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(createX, createY));

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PICKUP_WIDTH / 2, Constants.PICKUP_HEIGHT / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = Constants.BULLET_ENTITY;
        fixtureDef.filter.maskBits = Constants.DEFAULT_ENTITY;
//        fixtureDef.density = Constants.DEFAULT_DENSITY;


        body.createFixture(fixtureDef); // crashing??
        shape.dispose();

        body.setGravityScale(Constants.DEFUALT_GRAVITY);
        body.resetMassData();

        Gdx.app.log("WH", "Pickup setup, setting user data..");
        body.setUserData(new PickupData(Constants.PICKUP_WIDTH, Constants.PICKUP_HEIGHT, type));

        body.setLinearVelocity(0, -0.9f);
        return body;
    }

    public static Body createTesla(World world, float createX, float createY, String shotBy, int number) {
        float rotation;
        if (number == 0) {
            rotation = 170f;
        } else {
            rotation = 190f;
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(createX, createY));

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.TESLA_WIDTH / 2, Constants.TESLA_HEIGHT / 2);
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
        body.setUserData(new TeslaActorData(Constants.TESLA_WIDTH, Constants.TESLA_HEIGHT, rotation, shotBy));
        return body;
    }

    public static Body createLaser(World world, float createX, float createY, String shotBy) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(createX, createY));

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.LASER_WIDTH / 2, Constants.LASER_HEIGHT / 2);
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
        body.setUserData(new LaserActorData(Constants.LASER_WIDTH, Constants.LASER_HEIGHT, shotBy));
        return body;
    }

    public static Body createTurret(World world, float x, float y, float width, float height) {
        if (x == 0 && y == 0) {
            x = Constants.TURRET_X;
            y = Constants.TURRET_Y;
        }

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

        body.setUserData(new TurretActorData(width, height));
        body.resetMassData();

        shape.dispose();
        return body;
    }

    public static Body createAsteroid(World world, PlanetHandler planetHandler) {
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

    public static Body createBar(World world, float x, float y, float max) {
        float width = 1.2f;
        float height = 0.4f;
        x = Constants.ConvertToBox(x);
        y = Constants.ConvertToBox(y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        fixtureDef.shape = shape;
        shape.dispose();

        fixtureDef.isSensor = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new BarData(width, height, 100, max));
        return body;
    }


    public static String getDataType(Body body) {
        if (body == null)
            return null;
        ActorData data = (ActorData) body.getUserData();
        return (data != null) ? data.getDataType() : null;
    }

    public static boolean isAsteroid(Body body) {
        if (body == null || body.getUserData() == null) return false;

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
        if (body == null || body.getUserData() == null) return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Turret");
    }

    public static boolean isPlanet(Body body) {
        if (body == null || body.getUserData() == null) return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Planet");
    }

    public static boolean isProjectile(Body body) {
        return isBullet(body) || isLaser(body) || isTesla(body);
    }

    public static boolean isTesla(Body body) {
        if (body == null || body.getUserData() == null) return false;
        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Tesla");
    }

    public static boolean isBullet(Body body) {
        if (body == null || body.getUserData() == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Bullet");
    }

    public static boolean isLaser(Body body) {
        if (body == null || body.getUserData() == null)
            return false;

        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Laser");
    }

    public static boolean isConstantlyDamaging(Body body) {
        if (body == null || body.getUserData() == null)
            return false;

        return isLaser(body) || isTesla(body);
    }

    public static float getProjectileDamage(Body body) {
        if (body == null || body.getUserData() == null)
            return 0f;

        ActorData data = (ActorData) body.getUserData();
        switch (data.getDataType()) {
            case "Laser":
                return ((LaserActorData) data).getDamage(); // todo convert to interface to avoid unnecessary casting
            case "Bullet":
                return ((BulletActorData) data).getDamage();
            case "Tesla":
                return ((TeslaActorData) data).getDamage();
            default:
                return 0f;
        }
    }

    public static boolean isPickup(Body body) {
        if(body == null || body.getUserData() == null) 
            return false;
        
        ActorData data = (ActorData) body.getUserData();
        return data.getDataType().equals("Pickup");
    }
    
    public static boolean getPickupType() {
        return false;// // TODO: 01/05/2016  
    }
}
