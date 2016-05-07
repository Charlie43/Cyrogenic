package com.project.charlie.cryogenic.data;

import com.project.charlie.cryogenic.actors.Bullet;

/**
 * Created by Charlie on 22/02/2016.
 */
public class BulletActorData extends ActorData {
    public Bullet bullet;
    float damage;



    public BulletActorData(float width, float height, float damage, String shotBy) {
        super(width, height);
        dataType = "Bullet";
        this.damage = damage;
        this.shotBy = shotBy;
    }

    public BulletActorData(float width, float height, Bullet bullet, float damage) {
        super(width, height);
        this.bullet = bullet;
        dataType = "Bullet";
        this.damage = damage;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public String getShotBy() {
        return shotBy;
    }
}
