package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class StorageFactoryTile extends FactoryTile {

    public StorageFactoryTile(float xPixels, float yPixels, float widthPixels, float heightPixels, GameConstants.TileType type, TextureRegion textureRegion) {
        super(xPixels, yPixels, widthPixels,heightPixels, GameConstants.DirectionType.NA, type, textureRegion);
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this,tileX,tileY);
    }


    @Override
    public void update(float deltaTime) {

    }

    public void addResource(boolean remove) {

    }
}
