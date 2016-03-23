package com.project.charlie.cyrogenic.game;

import com.badlogic.gdx.Game;
import com.project.charlie.cyrogenic.android.ActionResolver;
import com.project.charlie.cyrogenic.managers.AssetsManager;

public class Cyrogenic extends Game {
    public static ActionResolver actionResolver;

    public Cyrogenic(ActionResolver _actionResolver) {
        actionResolver = _actionResolver;
    }

    @Override
    public void create() {
        AssetsManager.loadAssets();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        // dispose assets
    }
}
