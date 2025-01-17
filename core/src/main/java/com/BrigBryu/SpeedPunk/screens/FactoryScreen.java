package com.BrigBryu.SpeedPunk.screens;

import com.BrigBryu.SpeedPunk.SpeedPunk;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.BrigBryu.SpeedPunk.utils.PlayerState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A screen that shows our factory-building map.
 * It uses a WorldMap to create/render tiles and handle interactions.
 * x 16
 */
public class FactoryScreen implements Screen {

    private static final float VIRTUAL_WIDTH = 640;
    private static final float VIRTUAL_HEIGHT = 360;

    private SpeedPunk game;
    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private TextureAtlas atlas;

    //Game models
    private FactoryMap worldMap;
    private PlayerState playerState;

    private GameConstants.TileType selectedTileType;
    private GameConstants.DirectionType selectedDirection = GameConstants.DirectionType.EAST;

    public FactoryScreen(SpeedPunk game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        // Center camera
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        this.playerState = PlayerState.loadFromFile(GameConstants.PLAYER_STATE_SAVE_LOCATION);
        this.atlas = new TextureAtlas(Gdx.files.internal("atlases/simpleFactory.atlas"));

        atlas.getTextures().first().setFilter(
            Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest
        );


        worldMap = FactoryMap.load(GameConstants.FACTORY_MAP_SAVE_LOCATION, atlas, playerState);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = (int) camera.position.x;
        camera.position.y = (int) camera.position.y;
        camera.update();
        worldMap.update(delta);
        game.spriteBatch.setProjectionMatrix(camera.combined);

        handleInput();
        checkHover();

        game.spriteBatch.begin();
        worldMap.draw(game.spriteBatch);
        game.spriteBatch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if(selectedTileType == GameConstants.TileType.MONEY_MAKER) {
                selectedTileType = null;
            } else {
                selectedTileType = GameConstants.TileType.MONEY_MAKER;
                System.out.println("Selected tile: MONEY_MAKER");
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if(selectedTileType == GameConstants.TileType.CONVEYOR_BELT) {
                selectedTileType = null;
            } else {
                selectedTileType = GameConstants.TileType.CONVEYOR_BELT;
                System.out.println("Selected tile: CONVEYOR_BELT");
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if(selectedTileType == GameConstants.TileType.COLLECTOR) {
                selectedTileType = null;
            } else {
                selectedTileType = GameConstants.TileType.COLLECTOR;
                System.out.println("Selected tile: COLLECTOR");
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if(selectedTileType == GameConstants.TileType.STORAGE) {
                    selectedTileType = null;
            } else {
                    selectedTileType = GameConstants.TileType.STORAGE;
                    System.out.println("Selected tile: STORAGE");
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedDirection = GameConstants.DirectionType.NORTH;
            System.out.println("Direction: NORTH");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            selectedDirection = GameConstants.DirectionType.EAST;
            System.out.println("Direction: EAST");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedDirection = GameConstants.DirectionType.SOUTH;
            System.out.println("Direction: SOUTH");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            selectedDirection = GameConstants.DirectionType.WEST;
            System.out.println("Direction: WEST");
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(worldCoords);

            int tileX = (int)(worldCoords.x / GameConstants.TILE_WIDTH);
            int tileY = (int)(worldCoords.y / GameConstants.TILE_HEIGHT);

            worldMap.handleClick(tileX, tileY, selectedTileType, selectedDirection);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            worldMap.save(GameConstants.FACTORY_MAP_SAVE_LOCATION);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            worldMap = FactoryMap.load(GameConstants.FACTORY_MAP_SAVE_LOCATION, atlas, playerState);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            playerState.saveToFile(GameConstants.PLAYER_STATE_SAVE_LOCATION);
            worldMap.save(GameConstants.FACTORY_MAP_SAVE_LOCATION);
            Gdx.app.exit();
        }
    }

    private void checkHover() {
        Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(worldCoords);

        int tileX = (int)(worldCoords.x / GameConstants.TILE_WIDTH);
        int tileY = (int)(worldCoords.y / GameConstants.TILE_HEIGHT);

        worldMap.handleHover(tileX, tileY, selectedTileType, selectedDirection);
    }


    @Override
    public void resize(int width, int height) {
        System.out.println(width + ", " + height);
        viewport.update(width, height);

    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
