package com.project.charlie.cryogenic.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.project.charlie.cryogenic.handlers.PlanetHandler;
import com.project.charlie.cryogenic.managers.AssetsManager;
import com.project.charlie.cryogenic.misc.Constants;

/**
 * Created by Charlie on 12/02/2016.
 */
public class Background extends Actor {

    private TextureRegion region;
    private Rectangle regionBounds1;
    private Rectangle regionBounds2;
    private int speed = 100;

    int stage;
    public static final int SPACE = 0;
    public static final int PLANET = 1;
    public static final int MAP = 2;
    public static final int MENU = 3;
    public static final int UPGRADES = 4;


    public Background(int stage, PlanetHandler planetHandler) {
        this.stage = stage;
        if (stage == SPACE || stage == MAP) {
            region = AssetsManager.getTextureRegion(Constants.BACKGROUND_SPACE_ASSET_ID);
        } else if(stage == MENU) {
            region = AssetsManager.getTextureRegion(Constants.BACKGROUND_MENU_ASSET_ID);
        } else if(stage == UPGRADES) {
            region = AssetsManager.getTextureRegion(Constants.BACKGROUND_MENU_DARKENED_ASSET_ID);
        } else if (stage == PLANET) {
            if (planetHandler == null)
                region = AssetsManager.getTextureRegion(Constants.BACKGROUND_ASSET_ID);
            else {
                switch (planetHandler.getType()) {
                    case "Desert":
                        region = AssetsManager.getTextureRegion(Constants.BACKGROUND_DESERT_ASSET_ID);
                        break;
                    case "Normal":
                        region = AssetsManager.getTextureRegion(Constants.BACKGROUND_ASSET_ID);
                        break;
                    case "Forest":
                        region = AssetsManager.getTextureRegion(Constants.BACKGROUND_FOREST_ASSET_ID);
                        break;
                    case "City":
                        region = AssetsManager.getTextureRegion(Constants.BACKGROUND_CITY_ASSET_ID);
                        break;
                    case "Water":
                        Gdx.app.log("GDX", "Water region found.");
                        region = AssetsManager.getTextureRegion(Constants.BACKGROUND_ICE_ASSET_ID);
                    default:
                        region = null;
                        break;
                }
            }
        } else {
            region = null;
        }
        if(stage != MENU && stage != MAP && stage != UPGRADES) {
            regionBounds1 = new Rectangle(0 - Constants.APP_WIDTH / 2,
                    0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
            regionBounds2 = new Rectangle(Constants.APP_WIDTH / 2,
                    0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        } else {
            regionBounds1 = new Rectangle(0,
                    0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
            regionBounds2 = new Rectangle(0-Constants.APP_WIDTH ,
                    0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }
    }

    @Override
    public void act(float delta) {
        if (stage != MAP && stage != MENU && stage != UPGRADES) {
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
        if(region != null) {
            batch.draw(region, regionBounds1.x, regionBounds1.y,
                    Constants.APP_WIDTH, Constants.APP_HEIGHT);
            batch.draw(region, regionBounds2.x, regionBounds2.y,
                    Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }
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
