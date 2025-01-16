package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MoneyStorageFactoryTile extends StorageFactoryTile {
    private TextureRegion[] storageLevels;
    private int capacity;
    private final int maxCapacity = 120;

    public MoneyStorageFactoryTile(
        float xPixels, float yPixels,
        float widthPixels, float heightPixels,
        GameConstants.TileType type,
        TextureAtlas atlas,
        GameConstants.ResourceType resourceType
    ) {
        super(xPixels, yPixels, widthPixels, heightPixels, type, atlas, resourceType);
        this.capacity = 0;
    }

    @Override
    public void addResource(boolean remove) {
        if (!remove) {
            capacity += 10;
        } else {
            capacity -= 10;
        }
        // Clamp
        capacity = Math.max(0, Math.min(capacity, maxCapacity));
        updateTextureForCapacity();
    }

    private void updateTextureForCapacity() {
        int index = 0;
        if (capacity >= 100) {
            index = 5;
        } else if (capacity >= 80) {
            index = 4;
        } else if (capacity >= 60) {
            index = 3;
        } else if (capacity >= 40) {
            index = 2;
        } else if (capacity >= 20) {
            index = 1;
        } else {
            index = 0;
        }

        if (storageLevels[index] != null) {
            this.textureRegion = storageLevels[index];
        }
    }

    @Override
    public void update(float deltaTime) {
        updateTextureForCapacity();
    }


    @Override
    protected void setRegion() {
        if(storageLevels == null) {
            initStorageLevelsTextureRegions();
        }
        updateTextureForCapacity();
    }

    private void initStorageLevelsTextureRegions() {
        storageLevels = new TextureRegion[6];
        storageLevels[0] = atlas.findRegion("storage1");
        storageLevels[1] = atlas.findRegion("storage2");
        storageLevels[2] = atlas.findRegion("storage3");
        storageLevels[3] = atlas.findRegion("storage4");
        storageLevels[4] = atlas.findRegion("storage5");
        storageLevels[5] = atlas.findRegion("storage6");
    }
}
