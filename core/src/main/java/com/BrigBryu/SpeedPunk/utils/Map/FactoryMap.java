package com.BrigBryu.SpeedPunk.utils.Map;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.PlayerState;
import com.BrigBryu.SpeedPunk.utils.Tiles.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import static com.BrigBryu.SpeedPunk.utils.GameConstants.TileType.EMPTY;
import static com.BrigBryu.SpeedPunk.utils.GameConstants.TileType.MONEY_MAKER;

/**
 * Manages a 2D array of actual Tile objects in memory (the "game world").
 * Also provides saving and loading functionality.
 */
public class FactoryMap {
    private FactoryTile[][] tiles;
    private int rows;
    private int cols;

    private TextureAtlas atlas;
    private final TextureRegion emptyRegion;
    private final TextureRegion moneyMakerRegion;
    private final PlayerState playerState;

    public FactoryMap(int rows, int cols,
                      TextureAtlas atlas, PlayerState playerState) {
        this.rows = rows;
        this.cols = cols;
        this.atlas = atlas;
        this.emptyRegion = atlas.findRegion("emptyTile");
        this.moneyMakerRegion = atlas.findRegion("moneyTile");
        this.playerState = playerState;
        tiles = new FactoryTile[rows][cols];
    }

    public void restMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float x = c * GameConstants.TILE_WIDTH;
                float y = r * GameConstants.TILE_HEIGHT;
                tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, emptyRegion);
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c].draw(spriteBatch);
            }
        }
    }

    //Mark: json
    private MapData toMapData() {
        MapData mapData = new MapData(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                FactoryTile t = tiles[r][c];
                // default EMPTY
                GameConstants.TileType type = EMPTY;

                if (t instanceof MoneyMaker) {
                    type = MONEY_MAKER;
                }

                mapData.tileGrid[r][c] = new TileData(t.rectangle.x, t.rectangle.y, type);
            }
        }
        return mapData;
    }

    public void save(String fileName) {
        MapData mapData = toMapData();
        Json json = new Json();
        String jsonString = json.toJson(mapData);

        FileHandle file = Gdx.files.local(fileName);
        file.writeString(jsonString, false);

        System.out.println("Map saved to " + fileName);
    }

    public static FactoryMap load(String fileName, TextureAtlas atlas, PlayerState playerState) {
        FileHandle file = Gdx.files.local(fileName);
        if (!file.exists()) {
            FactoryMap map = new FactoryMap(100,100,atlas,playerState);
            map.restMap();
            return map;
        }

        String jsonString = file.readString();
        Json json = new Json();
        MapData mapData = json.fromJson(MapData.class, jsonString);

        FactoryMap factoryMap = new FactoryMap(mapData.rows, mapData.cols, atlas, playerState);

        factoryMap.tiles = new FactoryTile[mapData.rows][mapData.cols];

        for (int r = 0; r < mapData.rows; r++) {
            for (int c = 0; c < mapData.cols; c++) {
                TileData tData = mapData.tileGrid[r][c];
                GameConstants.TileType type = tData.type;
                float x = tData.x;
                float y = tData.y;

                switch (type) {
                    case MONEY_MAKER:
                        factoryMap.tiles[r][c] = new MoneyMaker(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, factoryMap.moneyMakerRegion);
                        break;
                    case EMPTY:
                    default:
                        factoryMap.tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, factoryMap.emptyRegion);
                        break;
                }
            }
        }

        System.out.println("Map loaded from " + fileName);
        return factoryMap;
    }

    public void handleHover(int tileX, int tileY) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c] != null) {
                    ((FactoryTile) tiles[r][c]).setHighlighted(false);
                }
            }
        }

        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            if (tiles[tileY][tileX] != null) {
                tiles[tileY][tileX].setHighlighted(true);
            }
        }
    }

    //Mark: Handle clicks on all types of tiles
    public void handleClick(int tileX, int tileY) {
        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            tiles[tileY][tileX].click(this, tileX, tileY);
        }
    }

    public void handleClick(MoneyMaker moneyMaker,int tileX, int tileY) {
        playerState.setResource(GameConstants.ResourceType.MONEY, moneyMaker.getAmountToAdd());
        System.out.println("Current money is now: " + playerState.getResource(GameConstants.ResourceType.MONEY));
    }

    public void handleClick(EmptyTile emptyTile, int tileX, int tileY) {
        System.out.println("Converting empty tile to money tile");
        tiles[tileY][tileX] = new MoneyMaker(emptyTile.rectangle.x, emptyTile.rectangle.y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, moneyMakerRegion);
    }

    public void handleClick(StorageFactoryTile storageFactoryTile, int tileX, int tileY) {
        //TODO show tips
    }

    public FactoryTile getTile(int neighborX, int neighborY) {
        return tiles[neighborY][neighborX];
    }

    public void handleClick(ConveyorBeltTile conveyorBeltTile, int tileX, int tileY) {
    }

    public void handleClick(BasicResourceCollectorTile basicResourceCollectorTile, int tileX, int tileY) {
    }

    //Mark: add buildings to map
    public void handleClick(int tileX, int tileY, GameConstants.TileType selectedType) {
        if (tileX < 0 || tileX >= cols || tileY < 0 || tileY >= rows) {
            return;
        }

        FactoryTile currentTile = tiles[tileY][tileX];
        if (currentTile instanceof EmptyTile) {
            if (selectedType != GameConstants.TileType.EMPTY) {
                placeTile(tileX, tileY, selectedType);
            } else {
                System.out.println("No tile selected; doing nothing.");
            }
        } else {
            currentTile.click(this, tileX, tileY);
        }
    }

    private void placeTile(int tileX, int tileY, GameConstants.TileType type) {
        float x = tileX * GameConstants.TILE_WIDTH;
        float y = tileY * GameConstants.TILE_HEIGHT;

        switch (type) {
            case NODE_PRODUCER:
                tiles[tileY][tileX] = new MoneyMaker(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    moneyMakerRegion
                );
                System.out.println("Placed a MoneyMaker at (" + tileX + ", " + tileY + ")");
                break;

            case CONVEYOR_BELT:
                // You’ll need a conveyor texture:
                TextureRegion conveyorRegion = atlas.findRegion("convayerTile");
                tiles[tileY][tileX] = new ConveyorBeltTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    GameConstants.DirectionType.EAST, // or default direction
                    conveyorRegion
                );
                System.out.println("Placed a Conveyor Belt at (" + tileX + ", " + tileY + ")");
                break;

            case COLLECTOR:
                // You’ll need a collector texture:
                TextureRegion collectorRegion = atlas.findRegion("moneySpawnTile");
                tiles[tileY][tileX] = new BasicResourceCollectorTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    GameConstants.DirectionType.EAST, // or default direction
                    collectorRegion
                );
                System.out.println("Placed a Collector at (" + tileX + ", " + tileY + ")");
                break;

            case STORAGE:
                // You’ll need a storage texture:
                TextureRegion storageRegion = atlas.findRegion("sellTile");
                tiles[tileY][tileX] = new StorageFactoryTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    GameConstants.TileType.STORAGE,
                    storageRegion
                );
                System.out.println("Placed Storage at (" + tileX + ", " + tileY + ")");
                break;

            default:
                // Fallback: place empty or do nothing
                System.out.println("Unhandled tile type: " + type + ". Doing nothing.");
        }
    }


}
