package com.project.charlie.cyrogenic.handlers;

import com.badlogic.gdx.Gdx;
import com.project.charlie.cyrogenic.game.GameStage;
import com.project.charlie.cyrogenic.managers.FileManager;
import com.project.charlie.cyrogenic.misc.Constants;
import com.project.charlie.cyrogenic.objects.PlayerJSON;

/**
 * Created by Charlie on 28/04/2016.
 */
public class PlayerHandler extends GameHandler {
    int currentCurrency;
    int damageLevel;
    int speedLevel;
    int multishotLevel;
    int healthLevel;

    // todo load currency from file
    // then add to it from fitness bonus

    public PlayerHandler(GameStage stage) {
        super(stage);
        // Defaults
        currentCurrency = 0;
        damageLevel = 0;
        speedLevel = 0;
        healthLevel = 0;
        multishotLevel = 0;
        loadPlayer();
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

    public void loadPlayer() {
        PlayerJSON playerJSON = FileManager.loadPlayer();
        if(playerJSON != null) {
            damageLevel += playerJSON.damageLevel;
            speedLevel += playerJSON.speedLevel;
            multishotLevel += playerJSON.multishotLevel;
            currentCurrency = playerJSON.currency;
            healthLevel += playerJSON.healthLevel;
        }
    }

    public void saveCurrency() {
        FileManager.savePlayer(new PlayerJSON(currentCurrency, damageLevel, speedLevel, multishotLevel));
    }

    public void setCurrentCurrency(int currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public float getHealth() {
        return Constants.PLAYER_HP_DEFAULT + (Constants.PLAYER_HP_DEFAULT * (healthLevel * 0.1f));
    }

    public float getDamage() {
        return (Constants.PLAYER_DAMAGE_DEFAULT * (1.0f + (damageLevel * 0.1f)));
    }

    public float getSpeed() {
        return Constants.PLAYER_SPEED_DEFAULT + speedLevel;
    }

    public boolean getMultiShot() {
        return multishotLevel > 0;
    }

    public float getDamageUpgradeCost() {
        return 100f * (damageLevel + 0.3f);
    }
    public float getHealthUpgradeCost() {
        return 50f * (healthLevel + 0.3f);
    }

    public float getSpeedUpgradeCost() {
        return 200f * (speedLevel + 0.3f);
    }

    public float getMultiShotUpgradeCost() {
        return 1000f;
    }
}
