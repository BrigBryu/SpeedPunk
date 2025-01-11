package com.BrigBryu.SpeedPunk.screens;

import com.BrigBryu.SpeedPunk.SpeedPunk;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A screen that shows our factory-building map.
 * It uses a WorldMap to create/render tiles and handle interactions.
 */
public class FactoryScreen implements Screen {

    private static final float VIRTUAL_WIDTH = 1280f;
    private static final float VIRTUAL_HEIGHT = 720f;

    private SpeedPunk game;              // Reference to our main Game class
    private OrthographicCamera camera;
    private Viewport viewport;

    private TextureAtlas atlas;          // Holds tile images (e.g., "empty", "factory", "moneyMaker")
    private FactoryMap worldMap;           // The 2D grid of tiles

    public FactoryScreen(SpeedPunk game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Load your texture atlas (make sure "tiles.atlas" is in your assets)
        atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));

        // Create the WorldMap with 10 rows, 10 columns, and references to the tile regions
        worldMap = new FactoryMap(
            10, 10,
            atlas.findRegion("empty"),
            atlas.findRegion("factory"),
            atlas.findRegion("moneyMaker")
        );

        // Populate an initial layout of tiles
        worldMap.createInitialMap();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        handleInput();
        checkHover();

        game.spriteBatch.begin();
        worldMap.draw(game.spriteBatch);
        game.spriteBatch.end();
    }

    /**
     * Check for clicks on tiles, or press S to save, L to load, etc.
     */
    private void handleInput() {
        // mouse click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

            worldMap.handleClick(screenX, screenY);
        }

        // Press S to save the map
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            worldMap.saveMap("factoryMap.json");
        }

        // Press L to load the map
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            worldMap.loadMap("factoryMap.json");
        }
    }

    private void checkHover() {
        int screenX = Gdx.input.getX();
        int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

        worldMap.handleHover(screenX, screenY);
    }


    @Override
    public void resize(int width, int height) {
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
