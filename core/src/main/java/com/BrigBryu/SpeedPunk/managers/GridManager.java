package com.BrigBryu.SpeedPunk.managers;

import com.badlogic.gdx.utils.Json;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GridManager {
    private int[][] grid;

    public GridManager(int width, int height) {
        grid = new int[width][height];
    }

    public void placeBuilding(int x, int y, int type) {
        grid[x][y] = type;
    }

    public void saveGrid(String filePath) {
        Json json = new Json();
        try (FileWriter writer = new FileWriter(filePath)) {
            String jsonData = json.toJson(grid);
            writer.write(jsonData);
            System.out.println("Grid saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGrid(String filePath) {
        Json json = new Json();
        try (FileReader reader = new FileReader(filePath)) {
            grid = json.fromJson(int[][].class, reader);
            System.out.println("Grid loaded from " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printGrid() {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
