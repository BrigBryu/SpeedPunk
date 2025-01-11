package com.BrigBryu.SpeedPunk.utils.Tiles;

public class TileData {
    public float x, y;
    public int type;

    // default for json
    public TileData() {}

    public TileData(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
