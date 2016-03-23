package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.project.charlie.cyrogenic.game.Cyrogenic;

/**
 * Created by Charlie on 19/03/2016.
 */
public class FitnessHandler {




/*

todo
connect to api
show available fitness stats
 */


    public void connectToApi(Cyrogenic cyrogenic) {
        Gdx.app.log("FitnessHandler", "Connecting to fitness API");
//        cyrogenic.actionResolver.connectToFitnessApi();
        Gdx.app.log("FitnessHandler", "Connected");
    }

    private void buildFitnessClient() {

    }

}
