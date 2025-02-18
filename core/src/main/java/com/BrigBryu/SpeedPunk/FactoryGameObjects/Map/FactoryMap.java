package com.BrigBryu.SpeedPunk.utils.Map;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.tiles.EmptyTile;
import com.BrigBryu.SpeedPunk.FactoryGameObjects.tiles.FactoryTile;
import com.BrigBryu.SpeedPunk.FactoryGameObjects.tiles.TileData;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.PlayerState;
import com.BrigBryu.SpeedPunk.utils.Tiles.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    private final PlayerState playerState;

    public FactoryMap(int rows, int cols,
                      TextureAtlas atlas, PlayerState playerState) {
        this.rows = rows;
        this.cols = cols;
        this.atlas = atlas;
        this.playerState = playerState;
        tiles = new FactoryTile[rows][cols];
    }

    public void restMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float x = (c + GameConstants.FACTORY_MAP_TILE_OFF_SET.x)* GameConstants.TILE_WIDTH;
                float y = (r + GameConstants.FACTORY_MAP_TILE_OFF_SET.y) * GameConstants.TILE_HEIGHT;
                tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, atlas);
            }
        }
    }

    public void update(float deltaTime) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c].update(deltaTime);
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

                if (t instanceof MoneyResetter) {
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
            FactoryMap map = new FactoryMap(25,25,atlas,playerState);
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
                        factoryMap.tiles[r][c] = new MoneyResetter(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, atlas);
                        break;
                    case EMPTY:
                    default:
                        factoryMap.tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, atlas);
                        break;
                }
            }
        }

        System.out.println("Map loaded from " + fileName);
        return factoryMap;
    }

    //Mark: getters
    public Rectangle getTileRectangle(){
        return new Rectangle(
            GameConstants.FACTORY_MAP_TILE_OFF_SET.x, GameConstants.FACTORY_MAP_TILE_OFF_SET.y,
            tiles.length, tiles[0].length);
    }

    public Rectangle getPixleRectangle(){
        return new Rectangle(
            GameConstants.FACTORY_MAP_TILE_OFF_SET.x * GameConstants.TILE_HEIGHT,
            GameConstants.FACTORY_MAP_TILE_OFF_SET.y * GameConstants.TILE_HEIGHT,
            tiles.length * GameConstants.TILE_HEIGHT,
            tiles[0].length * GameConstants.TILE_HEIGHT);
    }



    public Vector2 getCenter() {
        return new Vector2(cols - tiles[0].length/2f, rows - tiles.length/2f);
    }

    public int getWidth(){
        return cols;
    }

    public int getHeight(){
        return rows;
    }


    public void handleHover(int tileX, int tileY,
                            GameConstants.TileType selectedTileType,
                            GameConstants.DirectionType selectedDirection) {
        tileX -= GameConstants.FACTORY_MAP_TILE_OFF_SET.x;
        tileY -= GameConstants.FACTORY_MAP_TILE_OFF_SET.y;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c].setHighlighted(null, null);
            }
        }

        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            FactoryTile hoveredTile = tiles[tileY][tileX];

            //selected tile means that selected tile type == EMPTY
            if (selectedTileType == null || selectedTileType == GameConstants.TileType.EMPTY) {
                // If hovered tile is empty highlight
                if (hoveredTile.getFactoryTileType() == GameConstants.TileType.EMPTY) {
                    hoveredTile.setHighlighted(GameConstants.TileType.EMPTY, selectedDirection);
                } else {
                    hoveredTile.setHighlighted(null, GameConstants.DirectionType.NA);
                }

                //building selected
            } else {
                if (hoveredTile.getFactoryTileType() == GameConstants.TileType.EMPTY) {
                    // Valid spot ghosted building in blue
                    hoveredTile.setHighlighted(selectedTileType, selectedDirection);
                } else {
                    // red highlight
                    hoveredTile.setHighlighted(null, GameConstants.DirectionType.NA);
                }
            }
        }
    }

    //Mark: Handle clicks on all types of tiles
    public void handleClick(int tileX, int tileY) {
        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            tileX -= GameConstants.FACTORY_MAP_TILE_OFF_SET.x;
            tileY -= GameConstants.FACTORY_MAP_TILE_OFF_SET.y;
            tiles[tileY][tileX].click(this, tileX, tileY);
        }
    }

    public void handleClick(MoneyResetter moneyResetter, int tileX, int tileY) {
        playerState.setResource(GameConstants.ResourceType.MONEY, 0);
        System.out.println("Current money is now: " + playerState.getResource(GameConstants.ResourceType.MONEY));
    }

    public void handleClick(EmptyTile emptyTile, int tileX, int tileY) {
        System.out.println("empty tile do nothing");
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
    public void handleClick(int tileX, int tileY,
                            GameConstants.TileType selectedType,
                            GameConstants.DirectionType selectedDirection) {
        if (tileX < 0 || tileX >= cols || tileY < 0 || tileY >= rows) {
            return;
        }

        FactoryTile currentTile = tiles[tileY][tileX];
        if (currentTile instanceof EmptyTile) {
            if (selectedType != GameConstants.TileType.EMPTY) {
                placeTile(tileX, tileY, selectedType, selectedDirection);
            } else {
                System.out.println("No tile selected; doing nothing.");
            }
        } else {
            // If it's not empty, just do the usual "click" on that tile
            currentTile.click(this, tileX, tileY);
        }
    }

    private void placeTile(int tileX, int tileY, GameConstants.TileType type,
                           GameConstants.DirectionType direction) {
        float x = tileX * GameConstants.TILE_WIDTH;
        float y = tileY * GameConstants.TILE_HEIGHT;

        if(type == null){
            return;
        }
        switch (type) {
            case NODE_PRODUCER:
                tiles[tileY][tileX] = new MoneyCreatorFactoryTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    direction,
                    atlas
                );
                System.out.println("Placed a MoneyMaker at (" + tileX + ", " + tileY + ")");
                break;

            case CONVEYOR_BELT:
                // Updated to use the direction parameter
                tiles[tileY][tileX] = new ConveyorBeltTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    direction,
                    atlas
                );
                System.out.println("Placed a Conveyor Belt at (" + tileX + ", " + tileY + ")");
                break;

            case COLLECTOR:
                tiles[tileY][tileX] = new BasicResourceCollectorTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    direction,
                    atlas
                );
                System.out.println("Placed a Collector at (" + tileX + ", " + tileY + ")");
                break;

            case STORAGE:
                // For simplicity, start with “empty” storage region or level 1
                tiles[tileY][tileX] = new MoneyStorageFactoryTile(
                    x, y,
                    GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT,
                    GameConstants.TileType.STORAGE,
                    atlas,
                    GameConstants.ResourceType.MONEY
                );
                System.out.println("Placed Storage at (" + tileX + ", " + tileY + ")");
                break;

            default:
                // ...
        }
    }
}
