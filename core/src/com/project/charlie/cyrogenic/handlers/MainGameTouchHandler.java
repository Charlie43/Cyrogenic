package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.InputProcessor;
import com.project.charlie.cyrogenic.game.GameStage;

/**
 * Created by Charlie on 01/03/2016.
 */
public class MainGameTouchHandler implements InputProcessor {
    GameStage stage;

    public MainGameTouchHandler(GameStage stage) {
        this.stage = stage;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        stage.translateScreenToWorldCoordinates(x, y);

        return stage.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        stage.translateScreenToWorldCoordinates(x, y);

        return stage.touchDragged(x, y, pointer);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        int activeTouch = 0;
//        for (int i = 0; i < 2; i++) {
//            if (Gdx.input.isTouched(i)) activeTouch++;
//        }
//        if (stage.getPlayer() != null && stage.getPlayer().isMoving() && activeTouch == 0) { // If we've still got a finger on the screen, we've just been shooting + moving and stopped shooting
//            stage.getPlayer().setMoving(false);
//            stage.getPlayer().stopMoving();
//        }
        return stage.touchUp(x, y, pointer, button);

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
