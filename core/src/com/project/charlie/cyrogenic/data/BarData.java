package com.project.charlie.cyrogenic.data;

/**
 * Created by Charlie on 27/03/2016.
 */
public class BarData extends ActorData {
    private float currentAmount;
    private float max;

    public BarData(float currentAmount, float max) {
        this.currentAmount = currentAmount;
        this.max = max;
    }

    public BarData(float width, float height, float currentAmount, float max) {
        super(width, height);
        this.currentAmount = currentAmount;
        this.max = max;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
