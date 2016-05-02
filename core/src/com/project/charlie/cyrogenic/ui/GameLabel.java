package com.project.charlie.cyrogenic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * Created by Charlie on 25/02/2016.
 */


public class GameLabel extends Actor {

    private Rectangle bounds;
    private BitmapFont font;
    private String text;

    public GameLabel(Rectangle bounds, String text, float size) {
        this.bounds = bounds;
        this.text = text;
        setWidth(bounds.width);
        setHeight(bounds.height);
//        font = new BitmapFont(Gdx.files.internal("new_mainfont.fnt"));// todo dynamic fonts using truetype
//        font.setColor(Color.LIME);
//        font.getData().setScale(scale);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ebrima.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) size;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1f;

        parameter.color = Color.SKY;
//        parameter.shadowOffsetX = 1;
//        parameter.shadowOffsetY = 1;


        font = generator.generateFont(parameter);
        generator.dispose();
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
