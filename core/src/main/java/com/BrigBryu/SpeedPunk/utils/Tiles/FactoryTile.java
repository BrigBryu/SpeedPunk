package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class FactoryTile extends Tile{

    private boolean isHighlighted;
    private int factoryTileID;

    public FactoryTile(float x, float y, float width, float height, int factoryTileID, TextureRegion textureRegion) {
        super(x, y, width, height, textureRegion);
        this.isHighlighted = false;
        this.factoryTileID = factoryTileID;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public int getFactoryTileID(){
        return factoryTileID;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (isHighlighted) {
            spriteBatch.setColor(GameConstants.HOVER_COLOR);
        }
        spriteBatch.draw(textureRegion, x, y, width, height);
        spriteBatch.setColor(1f, 1f, 1f, 1f); //default
    }
}
