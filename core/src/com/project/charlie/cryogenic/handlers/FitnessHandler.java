package com.project.charlie.cryogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cryogenic.game.Cryogenic;
import com.project.charlie.cryogenic.game.GameStage;
import com.project.charlie.cryogenic.misc.Constants;
import com.project.charlie.cryogenic.ui.GameLabel;

import java.util.ArrayList;

public class FitnessHandler extends GameHandler {
    Cryogenic cryogenic;
    public boolean achieved;

    public FitnessHandler(GameStage stage) {
        super(stage);
        achieved = false;
    }

    /*
    Todo
    Unit tests - Connected to API, correct currency conversion, handling erroneous step counts (null, negative numbers)
     */

    public void setCryogenic(Cryogenic cryogenic) {
        this.cryogenic = cryogenic;
    }

    public boolean connectToApi() {
        return cryogenic.actionResolver.connectToFitnessApi();
    }

    public void signIn() {
        cryogenic.actionResolver.signIn();
    }
    public int getDailySteps() {
        return cryogenic.actionResolver.readTotalSteps();
    }

    public void setUpFitnessText() {
        float width = stage.getCamera().viewportWidth;
        float height = stage.getCamera().viewportHeight;
        float counter = 0;

        float LEFT_PADDING = 0.15f;

        stage.addLabel("Title", new GameLabel(new Rectangle(width * 0.30f, height * 0.85f, 20, 20), "Fitness Report", 30f));

        stage.addLabel("TotalSteps", new GameLabel(new Rectangle(width * LEFT_PADDING, height * 0.75f, 20, 20),
                "Steps today:   " + cryogenic.actionResolver.readTotalSteps(), 17f));

        stage.addLabel("CurrencyEarned", new GameLabel(new Rectangle(width * LEFT_PADDING, height * 0.70f, 10, 10),
                "Currency Gained:   $" + calculateCurrencyGain(), 17f));

        stage.addLabel("FitnessLabel", stage.createLabel("Previous 7 Days Step Count", new Vector3(width * LEFT_PADDING, height * 0.65f, 0), 20, 20, 0, 17f));


        ArrayList<Integer> weeklySteps = cryogenic.actionResolver.readWeeklySteps();
        if(weeklySteps != null) {
            for (Integer steps : weeklySteps) {
                String count = String.valueOf(counter).substring(0,1);
                stage.addLabel("Day " + counter, new GameLabel(new Rectangle(width * LEFT_PADDING, height * (0.60f - (counter * 0.065f)), 20, 20),
                        count + "     " + steps.toString(), 16.5f));
                counter = counter + 1f;
            }
        } else {
            stage.addLabel("CouldntLoadDays", stage.createLabel("Couldn't load previous weeks steps.", new Vector3(width * 0.30f,
                    height * 0.60f, 0), 20, 20, 0, 16f));
        }
        if(!hasAchievedTarget()) {
            stage.addLabel("StepTarget", stage.createLabel("Daily Step Objective: 5000", new Vector3(width * 0.60f, height * 0.75f, 0),
                    20, 20, 0, 17f));
        } else {
            stage.addLabel("StepTargetAchieved", stage.createLabel("Daily Step Objective Achieved\nGained 1000 credits.", new Vector3(width * 0.60f, height * 0.75f, 0),
                    20, 20, 0, 17f));
        }
    }



    public boolean hasAchievedTarget() {
        return (getDailySteps() > 5000);
    }

    public void setUpFitnessButton() {
        Skin skin = new Skin();
        final ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        skin.add("FitnessButton", new Texture(Gdx.files.internal(Constants.FITNESS_ICON_IMAGE_PATH)));
        style.up = style.down = style.checked = skin.getDrawable("FitnessButton");
        ImageButton imageButton = new ImageButton(style);
        imageButton.setSize(50, 50);
        imageButton.setPosition(stage.getCamera().viewportWidth * 0.03f, stage.getCamera().viewportHeight * 0.87f);
        imageButton.setBounds(stage.getCamera().viewportWidth * 0.03f, stage.getCamera().viewportHeight * 0.87f, imageButton.getWidth(), imageButton.getHeight());
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
    public float calcCurrency(float amount) {
        return amount * 0.05f;
    }
}
