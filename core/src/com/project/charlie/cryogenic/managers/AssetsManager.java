package com.project.charlie.cryogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.project.charlie.cryogenic.misc.Constants;

import java.util.HashMap;

/**
 * Created by Charlie on 12/02/2016.
 */
public class AssetsManager {
    public static final String TAG = "AssetsManager";

    private static HashMap<String, TextureRegion> texturesMap = new HashMap<String, TextureRegion>();

    public static void loadAssets() {
        texturesMap.put(Constants.BACKGROUND_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_SPACE_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_SPACE_IMAGE_PATH))));

        texturesMap.put(Constants.PLAYER_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.PLAYER_IMAGE_PATH))));

        texturesMap.put(Constants.TURRET_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.TURRET_IMAGE_PATH))));

        texturesMap.put(Constants.BLOCK_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BLOCK_ASSET_PATH))));

        texturesMap.put(Constants.BOX_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BOX_ASSET_PATH))));

        texturesMap.put(Constants.BULLET_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BULLET_IMAGE_PATH))));

        texturesMap.put(Constants.ASTEROID_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.ASTEROID_IMAGE_PATH))));

        texturesMap.put(Constants.ASTEROID_2_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.ASTEROID_2_IMAGE_PATH))));

        texturesMap.put(Constants.ASTEROID_3_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.ASTEROID_3_IMAGE_PATH))));

        texturesMap.put(Constants.LASER_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.LASER_IMAGE_PATH))));

        texturesMap.put(Constants.TESLA_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.TESLA_IMAGE_PATH))));

        texturesMap.put(Constants.HEALTH_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.HEALTH_IMAGE_PATH))));

        texturesMap.put(Constants.T_TURRET_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.T_TURRET_IMAGE_PATH))));

        texturesMap.put(Constants.L_TURRET_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.L_TURRET_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_DESERT_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_DESERT_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_FOREST_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_FOREST_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_CITY_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_CITY_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_MENU_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_MENU_IMAGE_PATH))));

        texturesMap.put(Constants.BACKGROUND_ICE_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.BACKGROUND_ICE_IMAGE_PATH))));

        texturesMap.put(Constants.HP_PICKUP_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.HP_PICKUP_IMAGE_PATH))));

        texturesMap.put(Constants.CURRENCY_PICKUP_ASSET_ID, new TextureRegion(
                new Texture(Gdx.files.internal(Constants.CURRENCY_PICKUP_IMAGE_PATH))));

    }

    public static TextureRegion getTextureRegion(String key) {
        return texturesMap.get(key);
    }
}
