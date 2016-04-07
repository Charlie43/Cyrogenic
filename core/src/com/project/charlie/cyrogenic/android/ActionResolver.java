package com.project.charlie.cyrogenic.android;

import java.util.ArrayList;

/**
 * Created by Charlie on 19/03/2016.
 */
public interface ActionResolver {

    void connectToFitnessApi();
    void readData();
    void signIn();
    void signOut();
    int readTotalSteps();
    ArrayList<Integer> readWeeklySteps();
    boolean isSignedIn();


}
