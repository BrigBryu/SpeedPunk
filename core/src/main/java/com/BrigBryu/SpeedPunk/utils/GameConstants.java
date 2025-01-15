package com.BrigBryu.SpeedPunk.utils;

import com.badlogic.gdx.graphics.Color;

public class GameConstants {
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;

    //File locations
    public static final String PLAYER_STATE_SAVE_LOCATION = "core/saves/playerSave.json";
    public static final String FACTORY_MAP_SAVE_LOCATION = "core/saves/factoryMap.json";

    // Colors (Red, Green, Blue, Alpha)
    public static final Color HOVER_COLOR = new Color(.8f, 0f, 0f, .9f); // Red with transparency

    //Enums
    public enum TileType {
        EMPTY,
        MONEY_MAKER,
        CONVEYOR_BELT,
        NODE_PRODUCER,
        COLLECTOR,
        STORAGE
    }

    public enum DirectionType {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NA
    }

    public enum ResourceType {
        MONEY
    }
}
