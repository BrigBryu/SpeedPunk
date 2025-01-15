package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.FactoryResourceNode;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class ResourceCollectorFactoryTile extends FactoryTile {
    protected ConveyorBeltTile inputBelt;
    protected ConveyorBeltTile outputBelt;
    protected StorageFactoryTile outputStorage;

    public ResourceCollectorFactoryTile(float x, float y, float w, float h,
                                        GameConstants.DirectionType direction, TextureRegion textureRegion) {
        super(x, y, w, h, direction, GameConstants.TileType.COLLECTOR, textureRegion);
    }

    public abstract void acceptNode(FactoryResourceNode node);

    public void setInputBelt(ConveyorBeltTile belt) { this.inputBelt = belt; }
    public void setOutputBelt(ConveyorBeltTile belt) { this.outputBelt = belt; }
    public void setOutputStorage(StorageFactoryTile storage) { this.outputStorage = storage; }

    @Override
    public void update(float deltaTime) {
        //TODO pull from inputBelt, process in acceptNode(), then place on outputBelt or outputStorage.
    }
}
