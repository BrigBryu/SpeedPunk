package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EmptyTile extends FactoryTile{
    public EmptyTile(float xPixels, float yPixels, float widthPixels, float heightPixels, TextureAtlas atlas) {
        super(xPixels, yPixels, widthPixels, heightPixels, GameConstants.DirectionType.NA, GameConstants.TileType.EMPTY, atlas);
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this, tileX, tileY);
    }

    @Override
    public void update(float deltaTime) {

    }

//    @Override
//    protected void setRegion() {
//        textureRegion = atlas.findRegion("emptyTile");
//    }
}
