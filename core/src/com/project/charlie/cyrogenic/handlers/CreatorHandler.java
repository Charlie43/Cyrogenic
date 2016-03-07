package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.charlie.cyrogenic.actors.Turret;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.misc.Constants;

/**
 * Created by Charlie on 05/03/2016.
 */
public class CreatorHandler extends GameHandler {

    String placing;
    CreateStageTouchHandler touchHandler;

    public CreatorHandler(GameStage stage) {
        super(stage);
        placing = "";
        touchHandler = new CreateStageTouchHandler(stage, this);
    }

    public void setUpButtons() {
        Skin skin = new Skin();
        skin.add("turret", new Texture(Gdx.files.internal(Constants.TURRET_IMAGE_PATH)));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = style.down = style.checked = skin.getDrawable("turret"); // todo indicate button is pressed

        ImageButton button = new ImageButton(style);
        button.setSize(30, 30);
        button.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 4);
        button.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 4, 30, 30);
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Creator", "Button touched");
                placing = "turret";
                return true;
            }
        });
        stage.addActor(button);
    }

    public void handlePlacement(float x, float y) {
        if (placing.equals("turret")) {
            Turret temp = new Turret(WorldHandler.createTurret(stage.getWorld(), x, y,
                    10)); // todo define firerate based on turret type
            temp.getActorData().turret = temp;
            stage.addActor(temp);
            stage.addTurret(temp);
        }
    }

    public void setUpCreatorButton() {
        TextButton creatorButton = new TextButton("Create Level", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        creatorButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200);
        creatorButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200, 250, 40);
        creatorButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setUpStageCreator();
                Gdx.input.setInputProcessor(touchHandler);
                return true;
            }
        });
        stage.addActor(creatorButton);
    }

    public void setUpFinishButton() {
        TextButton creatorButton = new TextButton("Finish Level", new Skin(Gdx.files.internal(Constants.BUTTONS_SKIN_PATH)), "default");
        creatorButton.setPosition(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200);
        creatorButton.setBounds(stage.getCamera().viewportWidth / 3, stage.getCamera().viewportHeight / 2 - 200, 250, 40);
        creatorButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // todo write xml file
                return false;
            }
        });
    }
}
