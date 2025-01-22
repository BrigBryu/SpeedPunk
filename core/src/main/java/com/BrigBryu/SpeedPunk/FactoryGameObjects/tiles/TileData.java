// TileData.java
package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;

public class TileData {
    public float x, y;
    public GameConstants.TileType type;

    public TileData() {}

    public TileData(float x, float y, GameConstants.TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
