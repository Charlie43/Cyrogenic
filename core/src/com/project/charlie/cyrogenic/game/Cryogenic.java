package com.project.charlie.cyrogenic.game;

import com.badlogic.gdx.Game;
import com.project.charlie.cyrogenic.android.ActionResolver;
import com.project.charlie.cyrogenic.managers.AssetsManager;

public class Cryogenic extends Game {
    public ActionResolver actionResolver;

    public Cryogenic(ActionResolver _actionResolver) {
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
