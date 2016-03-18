package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.project.charlie.cyrogenic.data.ActorData;
import com.project.charlie.cyrogenic.data.BaseActor;

/**
 * Created by Charlie on 16/03/2016.
 */
public class Planet extends BaseActor {
    String image;
    Texture texture;

    public Planet(Body body, String image) {
        super(body);
        this.image = image; // todo image based on type?
        texture = new Texture("images/planets/" + image + ".png");
        setTouchable(Touchable.enabled);
    }

    public void bounds() {
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);
        setBounds(screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);
    }

    @Override
    public ActorData getActorData() {
        return actorData;
    }

    public float getX() {
        return screenRectangle.x;
    }

    public float getY() {
        return screenRectangle.y;
    }

    public float getHeight() {
        return screenRectangle.height;
    }
}
