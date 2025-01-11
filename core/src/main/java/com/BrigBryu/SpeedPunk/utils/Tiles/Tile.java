package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
    public float x;
    public float y;
    protected float width;
    protected float height;
    protected TextureRegion textureRegion;


    public Tile(float x, float y, float width, float height, TextureRegion textureRegion) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureRegion = textureRegion;
    }


    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, x, y, width, height);
    }
}
