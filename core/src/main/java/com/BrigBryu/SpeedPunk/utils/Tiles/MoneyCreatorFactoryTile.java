package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.FactoryResourceNode;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MoneyCreatorFactoryTile extends ResourceCreatorFactoryTile{

    public MoneyCreatorFactoryTile(float xPixels, float yPixels, float widthPixels, float heightPixels, GameConstants.DirectionType direction, TextureAtlas atlas) {
        super(xPixels, yPixels, widthPixels, heightPixels, direction, atlas, 1, GameConstants.ResourceType.MONEY);
    }

    @Override
    public FactoryResourceNode createResourceNode() {
        return null;
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {

    }

//    @Override
//    protected void setRegion() {
//        textureRegion = atlas.findRegion("moneySpawnTile");
//    }
}
