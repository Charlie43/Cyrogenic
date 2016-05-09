package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.data.PlayerActorData;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

/**
 * Created by Charlie on 12/02/2016.
 */
public class Player extends BaseActor {
    private float stateTime;
    private boolean moving;

    float forceX;
    float forceY;
    private float damage;
    private float maxHealth; // todo
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

        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        if (getDirection() == 0) {
            batch.draw(AssetsManager.getTextureRegion(Constants.PLAYER_L_ASSET_ID), x, y);
        } else if (getDirection() == 1) {
            batch.draw(AssetsManager.getTextureRegion(Constants.PLAYER_R_ASSET_ID), x, y);
        } else {
            batch.draw(AssetsManager.getTextureRegion(Constants.PLAYER_ASSET_ID), x, y);
        }
    }

    public int getDirection() {
        if (forceY > 0) return 0; // up
        else if(forceY < 0) return 1; // down
        else return 2; // forward
    }

    public void applyForce(float forceX, float forceY) {
        this.forceX = forceX;
        this.forceY = forceY;
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

    public float getBodyX() {
        return getBody().getPosition().x;
    }

    public float getBodyY() {
        return getBody().getPosition().y;
    }

    public void rotateToTouch(float angle) {
//        affine2 = new Affine2();
//        affine2.rotate(angle);
    }

}
