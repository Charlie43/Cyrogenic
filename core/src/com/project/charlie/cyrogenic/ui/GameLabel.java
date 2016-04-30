package com.project.charlie.cyrogenic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Charlie on 25/02/2016.
 */
public class GameLabel extends Actor {

    private Rectangle bounds;
    private BitmapFont font;
    private String text;

    public GameLabel(Rectangle bounds, String text, float scale) {
        this.bounds = bounds;
        this.text = text;
        setWidth(bounds.width);
        setHeight(bounds.height);
        font = new BitmapFont(Gdx.files.internal("new_mainfont.fnt"));// todo dynamic fonts using truetype
        font.setColor(Color.LIME);
        font.getData().setScale(scale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, text, bounds.x, bounds.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
