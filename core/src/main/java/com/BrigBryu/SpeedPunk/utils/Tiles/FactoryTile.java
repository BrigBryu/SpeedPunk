package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class FactoryTile extends Tile implements ClickableFactoryTile, HoverFactoryTile{

    private boolean isHighlighted;
    private GameConstants.TileType tileType;
    public GameConstants.DirectionType direction;


    public FactoryTile(float xPixels, float yPixels, float widthPixels, float heightPixels, GameConstants.DirectionType direction, GameConstants.TileType type, TextureAtlas atlas) {
        super(xPixels, yPixels, widthPixels,heightPixels, atlas);
        this.isHighlighted = false;
        this.direction = direction;
        this.tileType = type;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public GameConstants.TileType getFactoryTileType(){
        return tileType;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (isHighlighted) {
            spriteBatch.setColor(GameConstants.HOVER_COLOR);
        }
        spriteBatch.draw(textureRegion, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        spriteBatch.setColor(1f, 1f, 1f, 1f); //default
    }

    @Override
    public void hover() {
        System.out.println("Over");
    }


    public abstract void update(float deltaTime);

    }
