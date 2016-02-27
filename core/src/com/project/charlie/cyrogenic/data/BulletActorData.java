package com.project.charlie.cyrogenic.data;

import com.project.charlie.cyrogenic.actors.Bullet;

/**
 * Created by Charlie on 22/02/2016.
 */
public class BulletActorData extends ActorData {
    public Bullet bullet;

    public BulletActorData(float width, float height) {
        super(width, height);
        dataType = "Bullet";
    }

    public BulletActorData(float width, float height, Bullet bullet) {
        super(width, height);
        this.bullet = bullet;
        dataType = "Bullet";
    }

}
