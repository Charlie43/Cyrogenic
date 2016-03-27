package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cyrogenic.data.BaseActor;
import com.project.charlie.cyrogenic.data.LaserActorData;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 24/03/2016.
 */
public class Laser extends BaseActor {
    public Laser(Body body) {
        super(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        batch.draw(AssetsManager.getTextureRegion(Constants.LASER_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);
    }

    @Override
    public LaserActorData getActorData() {
        return (LaserActorData) actorData;
    }

    public float getX() {
        return screenRectangle.x;
    }


}
