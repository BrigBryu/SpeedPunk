package com.BrigBryu.SpeedPunk;

import com.BrigBryu.SpeedPunk.screens.FactoryScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpeedPunk extends Game {
    public SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        setScreen(new FactoryScreen(this));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
