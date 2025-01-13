package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EmptyTile extends FactoryTile{
    public EmptyTile(float x, float y, float width, float height, TextureRegion textureRegion) {
        super(x, y, width, height, GameConstants.TileType.EMPTY, textureRegion);
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this, tileX, tileY);
    }
}
