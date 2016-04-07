package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cyrogenic.data.BaseActor;
import com.project.charlie.cyrogenic.data.PlayerActorData;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 12/02/2016.
 */
public class Player extends BaseActor {
    private float stateTime;
    private boolean moving;
//    Affine2 affine2;


    public Player(Body body) {
        super(body);
        moving = false;
    }

    @Override
    public PlayerActorData getActorData() {
        return (PlayerActorData) actorData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.0f;

        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);
        batch.draw(AssetsManager.getTextureRegion(Constants.PLAYER_ASSET_ID), x, y);

//affine 2 for rotation?

    }

    public void applyForce(float forceX, float forceY) {
        body.setLinearVelocity(new Vector2(forceX, forceY));
    }

    public void stopMoving() {
        body.setLinearVelocity(new Vector2(0, 0));
        moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public float getX() {
        return screenRectangle.x;
    }

    public float getY() {
        return screenRectangle.y;
    }

    public float getBodyX() { return getBody().getPosition().x; }
    public float getBodyY() { return getBody().getPosition().y; }

    public void rotateToTouch(float angle) {
//        affine2 = new Affine2();
//        affine2.rotate(angle);
    }

}
