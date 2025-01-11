package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class ClickableTile extends Tile{

    public ClickableTile(float x, float y, float width, float height, TextureRegion textureRegion) {
        super(x, y, width, height, textureRegion);
    }

    public abstract void click();
}
