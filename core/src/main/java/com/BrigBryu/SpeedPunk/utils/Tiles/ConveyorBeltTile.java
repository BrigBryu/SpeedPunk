package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.FactoryResourceNode;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ConveyorBeltTile extends FactoryTile{
    private float speed;
    private List<FactoryResourceNode> resourceNodeList;
    private StorageFactoryTile storage;

    public ConveyorBeltTile(float xPixels, float yPixels, float widthPixels, float heightPixels, GameConstants.DirectionType direction, TextureRegion textureRegion) {
        super(xPixels, yPixels, widthPixels,heightPixels, direction, GameConstants.TileType.CONVEYOR_BELT, textureRegion);
        resourceNodeList = new ArrayList<>();
        storage = null;
    }

    public void addItem(FactoryResourceNode node){
        resourceNodeList.add(node);
    }

    public void update(float deltaTime) {
        for (FactoryResourceNode node : resourceNodeList) {
            switch (direction) {
                case NORTH:
                    node.rectangle.y += speed * deltaTime; // Move along Y-axis
                    node.rectangle.x = this.rectangle.x + (this.rectangle.width - node.rectangle.width) / 2; // Center on X-axis
                    break;
                case SOUTH:
                    node.rectangle.y -= speed * deltaTime; // Move along Y-axis
                    node.rectangle.x = this.rectangle.x + (this.rectangle.width - node.rectangle.width) / 2; // Center on X-axis
                    break;
                case EAST:
                    node.rectangle.x += speed * deltaTime; // Move along X-axis
                    node.rectangle.y = this.rectangle.y + (this.rectangle.height - node.rectangle.height) / 2; // Center on Y-axis
                    break;
                case WEST:
                    node.rectangle.x -= speed * deltaTime; // Move along X-axis
                    node.rectangle.y = this.rectangle.y + (this.rectangle.height - node.rectangle.height) / 2; // Center on Y-axis
                    break;
            }

        }
        for(FactoryResourceNode node: resourceNodeList){
            if(hasReachedEndOfConveyor(node)){
                storage.addResource(resourceNodeList.remove(node));
            }
        }
    }

    private boolean hasReachedEndOfConveyor(FactoryResourceNode node) {
        Rectangle beltRectangle = this.rectangle; // Conveyor's rectangle
        Rectangle nodeRectangle = node.rectangle; // Node's rectangle

        switch (direction) {
            case NORTH: // Moving up
                return nodeRectangle.y >= beltRectangle.y + beltRectangle.height;
            case SOUTH: // Moving down
                return nodeRectangle.y + nodeRectangle.height <= beltRectangle.y;
            case EAST: // Moving right
                return nodeRectangle.x >= beltRectangle.x + beltRectangle.width;
            case WEST: // Moving left
                return nodeRectangle.x + nodeRectangle.width <= beltRectangle.x;
            default:
                return false;
        }
    }



    @Override
    public void click(FactoryMap factoryMap, int tileX, int tileY) {
        factoryMap.handleClick(this,tileX,tileY);
    }

    public void setOutputStorage(StorageFactoryTile storage) {
        this.storage = storage;
    }
}
