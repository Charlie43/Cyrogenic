package com.project.charlie.cyrogenic.android;

/**
 * Created by Charlie on 19/03/2016.
 */
public interface ActionResolver {

    void connectToFitnessApi();
    void signIn();
    void signOut();
    boolean isSignedIn();


}
