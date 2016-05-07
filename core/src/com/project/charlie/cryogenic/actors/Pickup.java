package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.data.PickupData;
import com.project.charlie.cryogenic.managers.AssetsManager;

/**
 * Created by Charlie on 01/05/2016.
 */
public class Pickup extends BaseActor {

    String type;

    public Pickup(Body body, String type) {
        super(body);
        this.type = type;
    }

    @Override
    public PickupData getActorData() {
        return (PickupData) actorData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = screenRectangle.x;
        float y = screenRectangle.y;
        float width = screenRectangle.width;

        batch.draw(AssetsManager.getTextureRegion(type), x, y, width, screenRectangle.height);
    }

    public float getX() { return screenRectangle.x; }
    public float getY() { return screenRectangle.y; }
}
