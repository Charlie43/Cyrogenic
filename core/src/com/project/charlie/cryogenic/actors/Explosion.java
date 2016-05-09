package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.project.charlie.cryogenic.data.ActorData;
import com.project.charlie.cryogenic.data.BaseActor;

/**
 * Created by Charlie on 09/05/2016.
 */
public class Explosion extends BaseActor {

    float stateTime = 0;

    private TextureAtlas textureAtlas;
    private Animation animation;

    public Explosion(Body body) {
        super(body);
        textureAtlas = new TextureAtlas(Gdx.files.internal("images/effects/explosion/spritesheet.txt"));
        animation = new Animation(1 / 15f, textureAtlas.getRegions());
    }

    @Override
    public ActorData getActorData() {
        return actorData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = screenRectangle.x;
        float y = screenRectangle.y;
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw(animation.getKeyFrame(stateTime, false), x, y, 20, 20);
    }

}
