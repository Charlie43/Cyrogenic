package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.project.charlie.cyrogenic.data.ActorData;
import com.project.charlie.cyrogenic.data.BaseActor;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 27/03/2016.
 */
public class Bar extends BaseActor {

    int width;
    int height;
    NinePatch health;

    public Bar(Body body, Color color) {
        super(body);
        this.width = 100;
        this.height = (int) screenRectangle.height;
        health = new NinePatch(AssetsManager.getTextureRegion(Constants.HEALTH_ASSET_ID), 0, 0, 0, 0); // todo texture based on color
    }

    @Override
    public ActorData getActorData() {
        return actorData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        health.draw(batch, screenRectangle.x, screenRectangle.y, width, screenRectangle.height);
        if (Constants.DEBUG)
            batch.draw(AssetsManager.getTextureRegion(Constants.BOX_ASSET_ID), screenRectangle.x, screenRectangle.y, screenRectangle.width, screenRectangle.height);

    }

    public int getBarWidth() {
        return width;
    }

    public void setBarWidth(int width) {
        this.width = width;
        Gdx.app.log("Bar", "Bar Width: " + width + " Scale " + getScaleY() + " Width " + screenRectangle.width);
    }

    public int getBarHeight() {
        return height;
    }

    public void setBarHeight(int height) {
        this.height = height;
    }
}
