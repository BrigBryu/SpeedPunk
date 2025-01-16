package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class StorageFactoryTile extends FactoryTile {

    protected GameConstants.ResourceType resourceType;
    public StorageFactoryTile(float xPixels, float yPixels, float widthPixels, float heightPixels,
                              GameConstants.TileType type, TextureAtlas atlas, GameConstants.ResourceType resourceType) {
        super(xPixels, yPixels, widthPixels,heightPixels, GameConstants.DirectionType.NA, type, atlas);
        this.resourceType = resourceType;
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this,tileX,tileY);
    }

    public abstract void addResource(boolean remove);
}
