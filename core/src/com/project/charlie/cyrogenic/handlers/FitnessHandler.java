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

public class FitnessHandler extends GameHandler {
    Cryogenic cryogenic;

    public FitnessHandler(GameStage stage) {
        super(stage);
    }

    /*
    Todo
    Unit tests - Connected to API, correct currency conversion, handling erroneous step counts (null, negative numbers)
     */

    public void setCryogenic(Cryogenic cryogenic) {
        this.cryogenic = cryogenic;
    }

    public void connectToApi() {
        cryogenic.actionResolver.connectToFitnessApi();
        cryogenic.actionResolver.readData();
    }

    public int getDailySteps() {
        return cryogenic.actionResolver.readTotalSteps();
    }

    public void setUpFitnessText() {
        float width = stage.getCamera().viewportWidth;
        float height = stage.getCamera().viewportHeight;
        float counter = 0;
        stage.addLabel("CurrencyEarned", new GameLabel(new Rectangle(width * 0.30f, height * 0.75f, 10, 10),
                "Currency Gained: " + calculateCurrencyGain(), 12f));
        stage.addLabel("TotalSteps", new GameLabel(new Rectangle(width * 0.30f, height * 0.70f, 20, 20),
                "Steps today - " + cryogenic.actionResolver.readTotalSteps(), 12f));
        for (Integer steps : cryogenic.actionResolver.readWeeklySteps()) {
            stage.addLabel("Day " + counter, new GameLabel(new Rectangle(width * 0.30f, height * (0.60f - counter), 20, 20),
                    counter + " - " + steps.toString(), 8f));
            counter = counter + 0.05f;
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

    public float calculateCurrencyGain() {
        return cryogenic.actionResolver.readTotalSteps() * 0.05f; // todo dont add today twice. check if what we've already added is lower than the new value, and add the difference

    }
}
