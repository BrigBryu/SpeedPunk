package com.BrigBryu.SpeedPunk.FactoryGameObjects;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public abstract class FactoryResourceNode {
    private TextureRegion textureRegion;
    public Rectangle rectangle;

    public FactoryResourceNode(TextureRegion textureRegion, int xPixels, int yPixels, int widthPixels, int heightPixels) {
        this.textureRegion = textureRegion;
        this.rectangle = new Rectangle(xPixels, yPixels, widthPixels,heightPixels);
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public abstract GameConstants.ResourceType getResourceType();

    public abstract float getValue();
}
