package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class FactoryTile extends Tile implements ClickableFactoryTile, HoverFactoryTile{

    private boolean isHighlighted;
    private GameConstants.TileType tileType;

    public FactoryTile(float x, float y, float width, float height, GameConstants.TileType type, TextureRegion textureRegion) {
        super(x, y, width, height, textureRegion);
        this.isHighlighted = false;
        this.tileType = type;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public GameConstants.TileType getFactoryTileID(){
        return tileType;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (isHighlighted) {
            spriteBatch.setColor(GameConstants.HOVER_COLOR);
        }
        spriteBatch.draw(textureRegion, x, y, width, height);
        spriteBatch.setColor(1f, 1f, 1f, 1f); //default
    }

    @Override
    public void hover() {
        System.out.println("Over");
    }
}
