package com.BrigBryu.SpeedPunk.FactoryGameObjects;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MoneyResourceNode extends FactoryResourceNode{
    public MoneyResourceNode(TextureRegion textureRegion, int xPixels, int yPixels, int widthPixels, int heightPixels) {
        super(textureRegion, xPixels, yPixels, widthPixels, heightPixels);
    }

    @Override
    public GameConstants.ResourceType getResourceType() {
        return GameConstants.ResourceType.MONEY;
    }

    @Override
    public float getValue(){
        return 10;
    }
}
