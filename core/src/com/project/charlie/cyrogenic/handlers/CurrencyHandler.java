package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.managers.FileManager;

/**
 * Created by Charlie on 28/04/2016.
 */
public class CurrencyHandler extends GameHandler {
    int currentCurrency;

    // todo load currency from file
    // then add to it from fitness bonus

    public CurrencyHandler(GameStage stage) {
        super(stage);
        currentCurrency = 0;
        loadCurrency();
    }

    public void addCurrency(int amount) {
        currentCurrency += amount;
        Gdx.app.log("CURR", "Currency Gained: " + amount);
    }

    public void removeCurrency(int amount) {
        currentCurrency -= amount;
    }

    public int getCurrentCurrency() {
        return currentCurrency;
    }
    public void loadCurrency() {
        currentCurrency = FileManager.loadCurrency();
    }

    public void saveCurrency() {

    }


}
