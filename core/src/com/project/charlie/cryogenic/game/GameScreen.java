package com.project.charlie.cryogenic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Charlie on 12/02/2016.
 */
public class GameScreen implements Screen {

    private GameStage stage;

    Cryogenic cryogenic;

    public GameScreen(final Cryogenic cryogenic) {
        stage = new GameStage(cryogenic);
        this.cryogenic = cryogenic;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear Screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);
        // update stage
        // stage.draw
        // stage.act ?? animation?

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
