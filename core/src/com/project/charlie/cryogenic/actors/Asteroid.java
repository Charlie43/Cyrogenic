package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.ActorData;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

import java.util.Random;

/**
 * Created by Charlie on 24/02/2016.
 */
public class Asteroid extends BaseActor {

    Random random;
    String assetID;


    public Asteroid(Body body) {
        super(body);
        random = new Random();
        switch(random.nextInt(3)) {
            case 0:
            default:
                assetID = Constants.ASTEROID_ASSET_ID;
                break;
            case 1:
                assetID = Constants.ASTEROID_2_ASSET_ID;
                break;
            case 2:
                assetID = Constants.ASTEROID_3_ASSET_ID;
                break;
        }
    }

    @Override
    public ActorData getActorData() {
        return actorData;
    }

    @SuppressWarnings("Duplicates")
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.2f;
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        batch.draw(AssetsManager.getTextureRegion(assetID), x, y, width, screenRectangle.height);
    }

    public float getX() {
        return screenRectangle.x;
    }

    public float getY() {
        return screenRectangle.y;
    }
}
