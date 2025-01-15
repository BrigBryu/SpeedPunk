package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MoneyMaker extends FactoryTile  {

    public MoneyMaker(float xPixels, float yPixels, float widthPixels, float heightPixels, TextureRegion textureRegion) {
        super(xPixels, yPixels, widthPixels,heightPixels, GameConstants.DirectionType.NA, GameConstants.TileType.MONEY_MAKER, textureRegion);
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this, tileX, tileY);
    }

    public int getAmountToAdd() {
        return 5; //TODO
    }

    /**
     * Called when a new tile is placed next to this tile
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {

    }
}
