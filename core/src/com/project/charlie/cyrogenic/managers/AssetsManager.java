package com.project.charlie.cyrogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.project.charlie.cyrogenic.misc.Constants;

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


    }

    public static TextureRegion getTextureRegion(String key) {
        return texturesMap.get(key);
    }
}