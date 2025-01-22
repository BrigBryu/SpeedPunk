package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class FactoryTile extends Tile implements ClickableFactoryTile, HoverFactoryTile{

    private GameConstants.TileType highlightTilePrePlace;
    private GameConstants.DirectionType highlightDirectionPrePlace;
    private GameConstants.TileType tileType;
    public GameConstants.DirectionType direction;


    public FactoryTile(float xPixels,
                       float yPixels,
                       float widthPixels,
                       float heightPixels,
                       GameConstants.DirectionType direction,
                       GameConstants.TileType type,
                       TextureAtlas atlas) {
        super(xPixels, yPixels, widthPixels, heightPixels, atlas, type);
        this.direction = direction;
        this.tileType = type;
    }

    /**
     * if direction is na that means the tile is occupied so set color to highlight red
     */
    public void setHighlighted(GameConstants.TileType highlightTilePrePlace, GameConstants.DirectionType highlightDirectionPrePlace) {
        if(highlightDirectionPrePlace == GameConstants.DirectionType.NA) {
            this.highlightTilePrePlace = null;
            this.highlightDirectionPrePlace = highlightDirectionPrePlace;
        } else {
            this.highlightTilePrePlace = highlightTilePrePlace;
            this.highlightDirectionPrePlace = highlightDirectionPrePlace;
        }
    }

    public GameConstants.TileType getFactoryTileType(){
        return tileType;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        // 1) Draw the base tile as normal
        float rotationDegrees = 0;
        switch (direction) {
            case EAST:  rotationDegrees = 90;  break;
            case NORTH: rotationDegrees = 0;   break;
            case WEST:  rotationDegrees = 270; break;
            case SOUTH: rotationDegrees = 180; break;
            default:    rotationDegrees = 0;
        }
        float originX = rectangle.width / 2f;
        float originY = rectangle.height / 2f;

        spriteBatch.draw(
            textureRegion,
            rectangle.x,
            rectangle.y,
            originX,
            originY,
            rectangle.width,
            rectangle.height,
            1f,
            1f,
            rotationDegrees
        );

        // 2) If we have *any* highlight info
        if (highlightDirectionPrePlace != null) {
            // CASE A: highlightDirectionPrePlace == NA => Occupied or invalid => Red overlay
            if (highlightDirectionPrePlace == GameConstants.DirectionType.NA) {
                spriteBatch.setColor(GameConstants.HOVER_COLOR_ILLEGAL); // red
                spriteBatch.draw(
                    textureRegion,
                    rectangle.x,
                    rectangle.y,
                    originX,
                    originY,
                    rectangle.width,
                    rectangle.height,
                    1f,
                    1f,
                    rotationDegrees
                );
                spriteBatch.setColor(1f, 1f, 1f, 1f); // reset

            }
            // CASE B: highlightTilePrePlace != null => We want to ghost a tile
            else if (highlightTilePrePlace != null) {
                // Distinguish between "EMPTY tile" (no building selected) vs. an actual building
                if (highlightTilePrePlace == GameConstants.TileType.EMPTY) {
                    // “No building selected” scenario => Use the existing tile's texture, tinted blue
                    spriteBatch.setColor(GameConstants.HOVER_COLOR); // blue
                    spriteBatch.draw(
                        textureRegion,
                        rectangle.x,
                        rectangle.y,
                        originX,
                        originY,
                        rectangle.width,
                        rectangle.height,
                        1f,
                        1f,
                        rotationDegrees
                    );
                    spriteBatch.setColor(1f, 1f, 1f, 1f);

                } else {
                    // “Building selected” => ghost the building
                    TextureAtlas.AtlasRegion highlightRegion = atlas.findRegion(highlightTilePrePlace.getRegionName());

                    // Rotation for the *ghosted building*, not for the tile underneath
                    float previewRotation = 0;
                    switch (highlightDirectionPrePlace) {
                        case EAST:  previewRotation = 90;  break;
                        case NORTH: previewRotation = 0;   break;
                        case WEST:  previewRotation = 270; break;
                        case SOUTH: previewRotation = 180; break;
                        default:    previewRotation = 0;   break;
                    }

                    // Draw the ghost building in BUILDING_GHOST_COLOR
                    spriteBatch.setColor(GameConstants.BUILDING_GHOST_COLOR);
                    spriteBatch.draw(
                        highlightRegion,
                        rectangle.x,
                        rectangle.y,
                        originX,
                        originY,
                        rectangle.width,
                        rectangle.height,
                        1f,
                        1f,
                        previewRotation
                    );
                    spriteBatch.setColor(1f, 1f, 1f, 1f); // reset
                }
            }
        }
    }

    @Override
    public void hover() {
        System.out.println("Over");
    }


    public abstract void update(float deltaTime);

    }
