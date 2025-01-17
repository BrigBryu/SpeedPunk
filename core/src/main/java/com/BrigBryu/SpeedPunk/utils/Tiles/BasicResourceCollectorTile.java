package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.FactoryResourceNode;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BasicResourceCollectorTile extends ResourceCollectorFactoryTile {
    public BasicResourceCollectorTile(float x, float y, float w, float h,
                                      GameConstants.DirectionType direction, TextureAtlas atlas) {
        super(x, y, w, h, direction, atlas);
    }

    @Override
    public void acceptNode(FactoryResourceNode node) {
        // Example logic: +1 value
        float newValue = node.getValue() + 1;
        // node.setValue(newValue) if you have such a method
        // Then decide if you place it in storage or back on a belt
        if (outputStorage != null) {
            // outputStorage.addItem(node);
        } else if (outputBelt != null) {
            // outputBelt.addItem(node);
        }
    }

    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this,tileX,tileY);
    }

//    @Override
//    protected void setRegion() {
//        textureRegion = atlas.findRegion("collector");
//    }
}
