package com.project.charlie.cryogenic.actors;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.ActorData;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

/**
 * Created by Charlie on 13/02/2016.
 */
public class Boundary extends BaseActor {
    public Boundary(Body body) {
        super(body);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width;
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        batch.draw(AssetsManager.getTextureRegion(Constants.BLOCK_ASSET_ID), x, y, width, screenRectangle.height);
    }

    @Override
    public ActorData getActorData() {
        return actorData;
    }

}
