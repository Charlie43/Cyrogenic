package com.project.charlie.cryogenic.data;

import com.project.charlie.cryogenic.actors.Bullet;
import com.project.charlie.cryogenic.actors.Explosion;

/**
 * Created by Charlie on 22/02/2016.
 */
public class ExplosionData extends ActorData {
    public Explosion explosion;

    public ExplosionData(float width, float height, Explosion explosion) {
        super(width, height);
        this.explosion = explosion;
        dataType = "Explosion";
    }

    public Explosion getExplosion() {
        return explosion;
    }

    public void setExplosion(Explosion explosion) {
        this.explosion = explosion;
    }
}
