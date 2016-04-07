package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.game.Cryogenic;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.ui.GameLabel;

/**
 * Created by Charlie on 19/03/2016.
 */
public class FitnessHandler extends GameHandler {
    Cryogenic cryogenic;

    public FitnessHandler(GameStage stage) {
        super(stage);
    }




/*

todo
connect to api
show available fitness stats
 */


    public void connectToApi(Cryogenic cryogenic) {
        this.cryogenic = cryogenic;
        Gdx.app.log("FitnessHandler", "Connecting to fitness API");
        cryogenic.actionResolver.connectToFitnessApi();
        Gdx.app.log("FitnessHandler", "Connected");
        cryogenic.actionResolver.readData();
    }

    private void buildFitnessClient() {

    }

    public void setUpFitnessText() {
        float width = stage.getCamera().viewportWidth;
        float height = stage.getCamera().viewportHeight;
        int counter = 0;
        stage.addLabel("TotalSteps", new GameLabel(new Rectangle(width * 0.30f, height * 0.70f,
                20, 20), "Steps today - " + cryogenic.actionResolver.readTotalSteps(), 1f));
        for (Integer steps : cryogenic.actionResolver.readWeeklySteps()) {
            stage.addLabel("Day " + counter, new GameLabel(new Rectangle(width * 0.30f,
                    height * (0.50f + counter), 20, 20), counter + " - " + steps.toString(), 0.5f));
            counter++;
        }
    }

    public void setUpFitnessButton() {
        Skin skin = new Skin();
        final ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        skin.add("FitnessButton", new Texture(Gdx.files.internal(Constants.FITNESS_ICON_IMAGE_PATH)));
        style.up = style.down = style.checked = skin.getDrawable("FitnessButton");
        ImageButton imageButton = new ImageButton(style);
        imageButton.setSize(50, 50);
        imageButton.setPosition(70, stage.getCamera().viewportHeight * 0.90f);
        imageButton.setBounds(70, stage.getCamera().viewportHeight * 0.90f, imageButton.getWidth(), imageButton.getHeight());
        imageButton.setName(style.up.toString());
        imageButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Creator", "Fitness Button touched");
                stage.setUpFitnessMenu();
                return true;
            }
        });
        stage.addActor(imageButton);
    }
}
