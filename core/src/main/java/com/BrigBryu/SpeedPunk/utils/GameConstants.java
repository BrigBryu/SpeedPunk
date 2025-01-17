package com.BrigBryu.SpeedPunk.utils;

import com.badlogic.gdx.graphics.Color;

public class GameConstants {
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;

    //File locations
    public static final String PLAYER_STATE_SAVE_LOCATION = "core/saves/playerSave.json";
    public static final String FACTORY_MAP_SAVE_LOCATION = "core/saves/factoryMap.json";

    // Colors (Red, Green, Blue, Alpha)
    public static final Color HOVER_COLOR_ILLEGAL = new Color(.8f, 0f, 0f, .9f); // Red with transparency
    public static final Color HOVER_COLOR = new Color(0f, 0f, 0.8f, .9f); // Blue with transparency
    public static final Color BUILDING_GHOST_COLOR = new Color(1f, 1f, 1f, .7f); //Default with transparency


    //Enums
    public enum TileType {
        EMPTY("emptyTile"),
        MONEY_MAKER("moneySpawnTile"),
        CONVEYOR_BELT("convayerTile"),
        NODE_PRODUCER("money"),
        COLLECTOR("collector"),
        STORAGE("storage1");

        private final String regionName;

        TileType(String regionName) {
            this.regionName = regionName;
        }

        public String getRegionName() {
            return regionName;
        }
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
