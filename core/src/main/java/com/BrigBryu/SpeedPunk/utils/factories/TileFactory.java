package com.BrigBryu.SpeedPunk.utils.Tiles;

import com.BrigBryu.SpeedPunk.FactoryGameObjects.*;
import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.factories.TileCreationData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.function.Function;

public class TileFactory {
    private final HashMap<GameConstants.TileType, Function<TileCreationData, FactoryTile>> tileCreators;

    public TileFactory() {
        tileCreators = new HashMap<>();

        // Register tile types
        tileCreators.put(GameConstants.TileType.CONVEYOR_BELT, data -> new ConveyorBeltTile(data.x, data.y, data.width, data.height, data.direction, data.texture));
        tileCreators.put(GameConstants.TileType.NODE_PRODUCER, data -> new MoneyCreatorFactoryTile(data.x, data.y, data.width, data.height, data.direction, data.texture));
        // Add more tiles as needed...
    }

    public FactoryTile createTile(GameConstants.TileType tileType, TileCreationData data) {
        if (tileCreators.containsKey(tileType)) {
            return tileCreators.get(tileType).apply(data);
        }
        throw new IllegalArgumentException("Unsupported tile type: " + tileType);
    }
}
