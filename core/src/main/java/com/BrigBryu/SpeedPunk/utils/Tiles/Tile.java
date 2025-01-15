package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Tile {
    public Rectangle rectangle;
    protected TextureRegion textureRegion;


    public Tile(float xPixels, float yPixels, float widthPixels, float heightPixels, TextureRegion textureRegion) {
        this.rectangle = new Rectangle(xPixels, yPixels, widthPixels,heightPixels);
        this.textureRegion = textureRegion;
    }


    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
