package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cyrogenic.data.BaseActor;
import com.project.charlie.cyrogenic.data.TurretActorData;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 13/02/2016.
 */
public class Turret extends BaseActor {
    // todo turrets appear after certain interval
    private float stateTime;
    private long lastFiretime;

    public Turret(Body body) {
        super(body);
        stateTime = 0f;
        lastFiretime = System.currentTimeMillis();
    }

    @Override
    public TurretActorData getActorData() {
        return (TurretActorData) actorData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.2f;

        // if running
//        stateTime += Gdx.graphics.getDeltaTime(); // for animation
//        batch.draw(AssetsManager.getTextureRegion(Constants.TURRET_ASSET_ID), x, y,
//                width, screenRectangle.height);
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        batch.draw(AssetsManager.getTextureRegion(Constants.TURRET_ASSET_ID), x, y,
                width, screenRectangle.height);

    }

    public long getLastFiretime() {
        return lastFiretime;
    }

    public void setLastFiretime(long lastFiretime) {
        this.lastFiretime = lastFiretime;
    }

    public float getX() {
        return screenRectangle.x;
    }

    public float getY() {
        return screenRectangle.y;
    }
}
