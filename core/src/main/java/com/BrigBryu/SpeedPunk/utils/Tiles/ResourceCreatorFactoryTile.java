package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.FactoryResourceNode;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class ResourceCreatorFactoryTile extends FactoryTile {
    protected ConveyorBeltTile assignedBelt;
    private GameConstants.ResourceType resource;
    private float productionInterval;
    private float timeSinceLastProduction;

    public ResourceCreatorFactoryTile(float xPixels, float yPixels, float widthPixels, float heightPixels, GameConstants.DirectionType direction, TextureAtlas atlas, float productionInterval, GameConstants.ResourceType resource) {
        super(xPixels, yPixels, widthPixels, heightPixels, direction, GameConstants.TileType.NODE_PRODUCER, atlas);
        this.productionInterval = productionInterval;
        this.timeSinceLastProduction = 0;
        this.resource = resource;
        this.assignedBelt = null;
    }

    public void assignBelt(ConveyorBeltTile belt) {
        this.assignedBelt = belt;
    }

    public void update(float deltaTime) {
        if (assignedBelt != null) {
            timeSinceLastProduction += deltaTime;
            if (timeSinceLastProduction >= productionInterval) {
                FactoryResourceNode newNode = createResourceNode();
                assignedBelt.addItem(newNode);
                timeSinceLastProduction = 0;
            }
        }
    }

    public abstract FactoryResourceNode createResourceNode();
}
