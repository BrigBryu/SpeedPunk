package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Tile {
    public Rectangle rectangle;
    protected TextureRegion textureRegion;
    protected TextureAtlas atlas;


    public Tile(float xPixels, float yPixels, float widthPixels, float heightPixels, TextureAtlas atlas) {
        this.rectangle = new Rectangle(xPixels, yPixels, widthPixels,heightPixels);
        this.atlas = atlas;
        setRegion();
    }


    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected abstract void setRegion();
}
