package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cyrogenic.data.BaseActor;
import com.project.charlie.cyrogenic.data.BulletActorData;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 21/02/2016.
 */
public class Bullet extends BaseActor {

    public Bullet(Body body) {
        super(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.2f;
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);
        batch.draw(AssetsManager.getTextureRegion(Constants.BULLET_ASSET_ID), x, y, width, screenRectangle.height);

    }

    @Override
    public BulletActorData getActorData() {
        return (BulletActorData) actorData;
    }

    public float getX() {
        return screenRectangle.x;
    }

    public float getWidth() {
        return screenRectangle.width;
    }
}
