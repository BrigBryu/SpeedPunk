package com.BrigBryu.SpeedPunk.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {

    // Fields to serialize
    private int money;


    //need default for json
    public PlayerState() {
        this.money = 0;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }



    // --- JSON Save and Load Methods ---

    /**
     * Saves this PlayerState to a JSON file.
     */
    public void saveToFile(String fileName) {
        Json json = new Json();
        String jsonString = json.toJson(this);

        // Write
        FileHandle file = Gdx.files.local(fileName);
        file.writeString(jsonString, false);

        System.out.println("PlayerState saved: " + jsonString);
    }

    /**
     * Static method to load PlayerState from a JSON file.
     */
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
