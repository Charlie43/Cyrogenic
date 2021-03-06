package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.data.TeslaActorData;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

/**
 * Created by Charlie on 27/03/2016.
 */
public class Tesla extends BaseActor {
    float originX;
    float originY;
    public Tesla(Body body, float originX, float originY) {
        super(body);
        this.originX = originX;
        this.originY = originY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        setRotation(getActorData().getRotation());
        batch.draw(AssetsManager.getTextureRegion(Constants.TESLA_ASSET_ID), screenRectangle.x, screenRectangle.y,
                originX, originY,
                screenRectangle.width, screenRectangle.height, 1f, 1f, getRotation());
    }

    @Override
    public TeslaActorData getActorData() {
        return (TeslaActorData) actorData;
    }
}
