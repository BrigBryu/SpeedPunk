package com.BrigBryu.SpeedPunk.utils.Map;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Tiles.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
/**
 * Manages a 2D array of actual Tile objects in memory (the "game world").
 * Also provides saving and loading functionality.
 */
public class FactoryMap {
    private Tile[][] tiles;
    private int rows;
    private int cols;

    // We might store references to TextureRegions for easy re-creation after loading
    private TextureRegion emptyRegion;
    private TextureRegion factoryRegion;
    private TextureRegion moneyMakerRegion;

    public FactoryMap(int rows, int cols,
                    TextureRegion emptyRegion,
                    TextureRegion factoryRegion,
                    TextureRegion moneyMakerRegion) {
        this.rows = rows;
        this.cols = cols;
        this.emptyRegion = emptyRegion;
        this.factoryRegion = factoryRegion;
        this.moneyMakerRegion = moneyMakerRegion;

        tiles = new Tile[rows][cols];
    }

    /**
     * Create an initial map with random or preset tile patterns.
     */
    public void createInitialMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float x = c * GameConstants.TILE_WIDTH;
                float y = r * GameConstants.TILE_HEIGHT;
                if ((r + c) % 2 == 0) {
                    tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, emptyRegion);
                } else {
                    tiles[r][c] = new MoneyMaker(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, moneyMakerRegion);
                }
            }
        }
    }

    /**
     * Draw all tiles on the map.
     */
    public void draw(SpriteBatch spriteBatch) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c].draw(spriteBatch);
            }
        }
    }

    /**
     * Handle a click at screen coordinates
     * Convert screen coords to tile indices, then call click() if it's a ClickableTile.
     */
    public void handleClick(int screenX, int screenY) {
        int tileX = screenX / GameConstants.TILE_WIDTH;
        int tileY = screenY / GameConstants.TILE_HEIGHT;

        // Check bounds
        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            Tile tile = tiles[tileY][tileX];
            if (tile instanceof ClickableTile) {
                ((ClickableTile) tile).click();
            } else if (tile instanceof FactoryTile) {
                ((FactoryTile) tile).setHighlighted(true);
            }
        }
    }

    /**
     * Convert the current in-memory Tile array to a MapData for JSON saving.
     */
    private MapData toMapData() {
        MapData mapData = new MapData(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile t = tiles[r][c];
                int type = 0; // default to empty
                if (t instanceof FactoryTile) {
                    type = 1;
                } else if (t instanceof MoneyMaker) {
                    type = 2;
                }
                mapData.tileGrid[r][c] = new TileData(t.x, t.y, type);
            }
        }
        return mapData;
    }

    /**
     * Save the current map to a JSON file.
     */
    public void saveMap(String fileName) {
        MapData mapData = toMapData();
        Json json = new Json();
        String jsonString = json.toJson(mapData);

        FileHandle file = Gdx.files.local(fileName);
        file.writeString(jsonString, false);

        System.out.println("Map saved to " + fileName);
    }

    /**
     * Load a map from JSON, rebuild the Tile array.
     */
    public void loadMap(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        if (!file.exists()) {
            System.out.println("No map file found at " + fileName);
            return;
        }

        String jsonString = file.readString();
        Json json = new Json();
        MapData mapData = json.fromJson(MapData.class, jsonString);

        this.rows = mapData.rows;
        this.cols = mapData.cols;
        this.tiles = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                TileData tData = mapData.tileGrid[r][c];
                int type = tData.type;
                float x = tData.x;
                float y = tData.y;

                // Rebuild tile classes from 'type'
                switch (type) {
                    case 1:
                        tiles[r][c] = new MoneyMaker(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, moneyMakerRegion);
                        break;
                    default:
                        tiles[r][c] = new EmptyTile(x, y, GameConstants.TILE_WIDTH, GameConstants.TILE_HEIGHT, emptyRegion);
                        break;
                }
            }
        }
        System.out.println("Map loaded from " + fileName);
    }

    public void handleHover(int mouseX, int mouseY) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c] instanceof FactoryTile) {
                    ((FactoryTile) tiles[r][c]).setHighlighted(false);
                }
            }
        }

        int tileX = mouseX / GameConstants.TILE_WIDTH;
        int tileY = mouseY / GameConstants.TILE_HEIGHT;

        if (tileX >= 0 && tileX < cols && tileY >= 0 && tileY < rows) {
            if (tiles[tileY][tileX] instanceof FactoryTile) {
                FactoryTile factory = (FactoryTile) tiles[tileY][tileX];
                factory.setHighlighted(true);
            }
        }
    }

}
