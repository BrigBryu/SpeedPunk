package com.BrigBryu.SpeedPunk.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.EnumMap;
import java.util.Map;

public class PlayerState {

    // Map to serialize all resources
    private Map<GameConstants.ResourceType, Float> resources;

    // Default constructor for JSON
    public PlayerState() {
        // Initialize the map with default values
        resources = new EnumMap<>(GameConstants.ResourceType.class);
        for (GameConstants.ResourceType type : GameConstants.ResourceType.values()) {
            resources.put(type, 0.0f); // Default value for each resource
        }
    }

    //Mark: Resource management
    public Map<GameConstants.ResourceType, Float> getResources() {
        return resources;
    }

    public void setResources(Map<GameConstants.ResourceType, Float> resources) {
        this.resources = resources;
    }

    public float getResource(GameConstants.ResourceType type) {
        return resources.getOrDefault(type, 0.0f);
    }

    public void setResource(GameConstants.ResourceType type, float value) {
        resources.put(type, value);
    }

    public void increaseResource(GameConstants.ResourceType type, float value){
        setResource(type, getResource(type) + value);
    }

    public void reduceResource(GameConstants.ResourceType type, float value){
        float result = getResource(type) - value;
        if(result < 0) {
            result = 0;
        }
        setResource(type, result);
    }

    /**
     * @return if there is more or the same of value type of resource
     */
    public boolean checkResource(GameConstants.ResourceType type, float value){
        return getResource(type) >= value;
    }

    //Mark: Json storage
    public void saveToFile(String fileName) {
        Json json = new Json();
        String jsonString = json.toJson(this);

        FileHandle file = Gdx.files.local(fileName);
        file.writeString(jsonString, false);

        System.out.println("PlayerState saved: " + jsonString);
    }

    public static PlayerState loadFromFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);

        if (!file.exists()) {
            return new PlayerState();
        }

        String jsonString = file.readString();
        Json json = new Json();

        PlayerState loadedState = json.fromJson(PlayerState.class, jsonString);

        System.out.println("PlayerState loaded: " + jsonString);

        return loadedState;
    }
}
