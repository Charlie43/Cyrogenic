package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.charlie.cryogenic.data.BaseActor;
import com.project.charlie.cryogenic.data.BulletActorData;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

/**
 * Created by Charlie on 21/02/2016.
 */
public class Bullet extends BaseActor {

    float stateTime = 0;
    TextureAtlas textureAtlas;
    Animation animation;

    public Bullet(Body body) {
        super(body);
        textureAtlas = new TextureAtlas(Gdx.files.internal("images/effects/laser/spritesheet.txt"));
        animation = new Animation(1 / 3f, textureAtlas.getRegions());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = screenRectangle.x;
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.2f;
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw(animation.getKeyFrame(stateTime), x, y, width, screenRectangle.height);
//        batch.draw(AssetsManager.getTextureRegion(Constants.BULLET_ASSET_ID), x, y, width, screenRectangle.height);

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
