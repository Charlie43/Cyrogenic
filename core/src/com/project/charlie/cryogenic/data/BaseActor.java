package com.project.charlie.cryogenic.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.project.charlie.cryogenic.handlers.WorldHandler;
import com.project.charlie.cryogenic.misc.Constants;


/**
 * Created by Charlie on 13/02/2016.
 */
public abstract class BaseActor extends Actor {

    protected Body body;
    protected ActorData actorData;
    protected Rectangle screenRectangle;
    protected boolean bullet;

    public BaseActor(Body body) {
        this.body = body;
        this.actorData = (ActorData) body.getUserData();
        screenRectangle = new Rectangle();
        this.bullet = body.isBullet();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // if paused -- return

        if (body.getUserData() != null) {
            if (body.getType() == BodyDef.BodyType.StaticBody) {
                if (!WorldHandler.isPlanet(body))
                    updateBoundaryRectangle();
                else
                    updateRectangle();
            } else
                updateRectangle();
        } else {
            // delete
        }
    }

    public abstract ActorData getActorData();

    private void updateBoundaryRectangle() {
        screenRectangle.x = Constants.ConvertToScreen(body.getPosition().x -
                actorData.getWidth());
        screenRectangle.y = Constants.ConvertToScreen(body.getPosition().y -
                actorData.getHeight());
        screenRectangle.width = Constants.ConvertToScreen(actorData.getWidth());
        screenRectangle.height = Constants.ConvertToScreen(actorData.getHeight());
        setColor(Color.GOLD);
    }


    private void updateRectangle() {
        screenRectangle.x = Constants.ConvertToScreen(body.getPosition().x -
                actorData.getWidth() / 2);
        screenRectangle.y = Constants.ConvertToScreen(body.getPosition().y -
                actorData.getHeight() / 2);
        screenRectangle.width = Constants.ConvertToScreen(actorData.getWidth());
        screenRectangle.height = Constants.ConvertToScreen(actorData.getHeight());
        setColor(Color.GOLD);
    }

    public Body getBody() {
        return body;
    }

    public boolean isBullet() {
        return bullet;
    }

}
