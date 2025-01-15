package com.BrigBryu.SpeedPunk.utils.factories;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileCreationData {
    public float x, y, width, height, productionInterval;
    public GameConstants.DirectionType direction;
    public TextureRegion texture;

    public TileCreationData(float x, float y, float width, float height, GameConstants.DirectionType direction, TextureRegion texture, float productionInterval) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = direction;
        this.texture = texture;
        this.productionInterval = productionInterval;
    }
}
