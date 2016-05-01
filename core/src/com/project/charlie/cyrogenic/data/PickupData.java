package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Pickup;

/**
 * Created by Charlie on 01/05/2016.
 */
public class PickupData extends ActorData {
    String type;
    public Pickup pickup;

    public PickupData(float width, float height, String type) {
        super(width, height);
        dataType = "Pickup";
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
