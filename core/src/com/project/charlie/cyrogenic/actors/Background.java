package com.project.charlie.cyrogenic.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.project.charlie.cyrogenic.managers.AssetsManager;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 12/02/2016.
 */
public class Background extends Actor {

    private final TextureRegion region;
    private Rectangle regionBounds1;
    private Rectangle regionBounds2;
    private int speed = 100;

    int stage;
    public static final int SPACE = 0;
    public static final int PLANET = 1;
    public static final int MAP = 2;


    public Background(int stage) {
        this.stage = stage;
        if (stage == SPACE || stage == MAP) {
            region = AssetsManager.getTextureRegion(Constants.BACKGROUND_SPACE_ASSET_ID);
        } else if (stage == PLANET) {
            region = AssetsManager.getTextureRegion(Constants.BACKGROUND_ASSET_ID);
        } else {
            region = null;
        }
        regionBounds1 = new Rectangle(0 - Constants.APP_WIDTH / 2,
                0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        regionBounds2 = new Rectangle(Constants.APP_WIDTH / 2,
                0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    @Override
    public void act(float delta) {
        if (stage != MAP) {
            if (leftBoundsReached(delta))
                resetBounds();
            else
                updateXBounds(-delta);
        }
        // if != running, return
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(region, regionBounds1.x, regionBounds1.y,
                Constants.APP_WIDTH, Constants.APP_HEIGHT);
        batch.draw(region, regionBounds2.x, regionBounds2.y,
                Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    private boolean leftBoundsReached(float delta) {
        return (regionBounds2.x - (delta * speed)) <= 0;
    }

    private void resetBounds() {
        regionBounds1 = regionBounds2;
        regionBounds2 = new Rectangle(Constants.APP_WIDTH,
                0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    private void updateXBounds(float delta) {
        regionBounds1.x += delta * speed;
        regionBounds2.x += delta * speed;
    }

}
