package com.project.charlie.cyrogenic.game;

import com.badlogic.gdx.Game;
import com.project.charlie.cyrogenic.managers.AssetsManager;

public class Cyrogenic extends Game {
    @Override
    public void create() {
        AssetsManager.loadAssets();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        // dispose assets
    }
}
