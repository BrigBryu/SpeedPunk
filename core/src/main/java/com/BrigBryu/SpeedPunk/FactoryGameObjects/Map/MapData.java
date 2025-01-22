package com.BrigBryu.SpeedPunk.utils.Map;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.tiles.TileData;

public class MapData {
    public int rows;
    public int cols;
    public TileData[][] tileGrid;  // 2D array of tile data

    public MapData() {} // Required for JSON reflection

    public MapData(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tileGrid = new TileData[rows][cols];
    }
}
