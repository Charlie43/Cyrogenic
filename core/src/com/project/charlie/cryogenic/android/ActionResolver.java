package com.project.charlie.cryogenic.android;

import java.util.ArrayList;

/**
 * Created by Charlie on 19/03/2016.
 */
public interface ActionResolver {

    boolean connectToFitnessApi();
    void readData();
    void signIn();
    void signOut();
    int readTotalSteps();
    ArrayList<Integer> readWeeklySteps();
    boolean isSignedIn();


}
