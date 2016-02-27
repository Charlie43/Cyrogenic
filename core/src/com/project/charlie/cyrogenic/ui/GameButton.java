package com.project.charlie.cyrogenic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 23/02/2016.
 */
public class GameButton extends Button {

    protected Rectangle bounds;
    private Skin skin;

    public GameButton(Rectangle bounds) {
        this.bounds = bounds;
        setWidth(bounds.width);
        setHeight(bounds.height);
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);

        skin = new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH));

    }

    public Skin getSkin() {
        return skin;
    }


}
