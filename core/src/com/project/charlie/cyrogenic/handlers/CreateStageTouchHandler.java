package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.InputProcessor;
import com.project.charlie.cyrogenic.game.GameStage;

/**
 * Created by Charlie on 05/03/2016.
 */
public class CreateStageTouchHandler implements InputProcessor {

    GameStage stage;

    CreatorHandler creatorHandler;

    public CreateStageTouchHandler(GameStage stage, CreatorHandler creatorHandler) {
        this.stage = stage;
        this.creatorHandler = creatorHandler;
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        Vector3 touch = stage.translateScreenToWorldCoordinates(screenX, screenY);
//        if(creatorHandler.placing )
//        creatorHandler.handlePlacement(touch.x, touch.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
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
