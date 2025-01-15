package com.BrigBryu.SpeedPunk.utils.factories;

import com.BrigBryu.SpeedPunk.utils.GameConstants;
import com.BrigBryu.SpeedPunk.utils.Map.FactoryMap;
import com.BrigBryu.SpeedPunk.utils.Tiles.*;

public class TileRelationshipManager {

    /**
     * Called whenever a new tile is placed or an existing tile changes.
     *
     * @param factoryMap The global map of tiles.
     * @param tileX X position of the placed/updated tile.
     * @param tileY Y position of the placed/updated tile.
     */
    public void updateTileRelationships(FactoryMap factoryMap, int tileX, int tileY) {
        // 1. Get the center tile.
        FactoryTile centerTile = factoryMap.getTile(tileX, tileY);
        if (centerTile == null) return;

        // 2. For each neighbor, figure out if a relationship should be formed or broken.
        int[][] neighborOffsets = {
            {0, 1},  // North
            {1, 0},  // East
            {0, -1}, // South
            {-1, 0}, // West
            // (add diagonals if you want them)
        };

        for (int[] offset : neighborOffsets) {
            int nx = tileX + offset[0];
            int ny = tileY + offset[1];
            FactoryTile neighborTile = factoryMap.getTile(nx, ny);
            if (neighborTile != null) {
                updateRelationship(centerTile, neighborTile, offset);
                updateRelationship(neighborTile, centerTile, new int[] {-offset[0], -offset[1]});
            }
        }
    }

    /**
     * Decides how one tile relates to another based on offset/direction.
     *
     * @param sourceTile The tile we are checking from.
     * @param targetTile The neighbor tile we want to form/break relationship with.
     * @param offset     The (dx, dy) from sourceTile to targetTile.
     */
    private void updateRelationship(FactoryTile sourceTile, FactoryTile targetTile, int[] offset) {
        // Convert (dx, dy) to a DirectionType: direction from SOURCE => TARGET
        GameConstants.DirectionType dir = getDirectionFromOffset(offset);
        if (dir == null) return;

        // ------------------------------------------------
        // 1) RESOURCE CREATOR -> CONVEYOR BELT
        // ------------------------------------------------
        if (sourceTile instanceof ResourceCreatorFactoryTile && targetTile instanceof ConveyorBeltTile) {
            ResourceCreatorFactoryTile creator = (ResourceCreatorFactoryTile) sourceTile;
            ConveyorBeltTile conveyor = (ConveyorBeltTile) targetTile;

            // If the creator is facing 'dir' (meaning it outputs in that direction),
            // then it can deposit items onto the conveyor if the conveyor is exactly
            // in front or if it’s perpendicular (depending on your game rules).
            if (creator.direction == dir
                || isPerpendicular(creator.direction, dir))
            {
                creator.assignBelt(conveyor);
            }
        }

        // ------------------------------------------------
        // 2) RESOURCE CREATOR -> STORAGE or COLLECTOR
        // (Less common, but if your design allows a creator
        //  to directly deposit into storage or collector)
        // ------------------------------------------------
        if (sourceTile instanceof ResourceCreatorFactoryTile && targetTile instanceof StorageFactoryTile) {
            ResourceCreatorFactoryTile creator = (ResourceCreatorFactoryTile) sourceTile;
            StorageFactoryTile storage = (StorageFactoryTile) targetTile;

            // If the creator is facing dir, it can directly drop items into the storage
            // (Optional logic, depends on your design)
            if (creator.direction == dir) {
                // e.g. creator.assignOutputStorage(storage);
                // or storage.addInput(creator);
            }
        }
        // (Similarly if you have a separate "CollectorFactoryTile", do the same check)

        // ------------------------------------------------
        // 3) CONVEYOR BELT -> CONVEYOR BELT
        // ------------------------------------------------
        if (sourceTile instanceof ConveyorBeltTile && targetTile instanceof ConveyorBeltTile) {
            ConveyorBeltTile sourceBelt = (ConveyorBeltTile) sourceTile;
            ConveyorBeltTile targetBelt = (ConveyorBeltTile) targetTile;

            // If sourceBelt.direction == dir, that means the source belt is pointed
            // exactly at the target belt’s tile. So sourceBelt can deposit into targetBelt.
            // Often you also check if targetBelt is facing the "opposite" direction
            // (e.g. WEST vs EAST), to chain them seamlessly—this depends on your game logic.
            if (sourceBelt.direction == dir) {
                // e.g. sourceBelt.setNextBelt(targetBelt);
            }
        }

        // ------------------------------------------------
        // 4) CONVEYOR BELT -> STORAGE
        // ------------------------------------------------
        if (sourceTile instanceof ConveyorBeltTile && targetTile instanceof StorageFactoryTile) {
            ConveyorBeltTile belt = (ConveyorBeltTile) sourceTile;
            StorageFactoryTile storage = (StorageFactoryTile) targetTile;

            // If the belt’s facing 'dir', it outputs to storage
            if (belt.direction == dir) {
                // e.g. belt.setOutputStorage(storage);
            }
        }

        // -------------------------------------------------------
        // RESOURCE COLLECTOR => BELT OR STORAGE (OUTPUT)
        // The collector outputs along its facing direction.
        // If dir == collector's facing direction, the target is "in front" of the collector.
        // -------------------------------------------------------
        if (sourceTile instanceof ResourceCollectorFactoryTile) {
            ResourceCollectorFactoryTile collector = (ResourceCollectorFactoryTile) sourceTile;

            // 1) Output to a conveyor belt
            if (targetTile instanceof ConveyorBeltTile) {
                ConveyorBeltTile belt = (ConveyorBeltTile) targetTile;

                // If the offset direction == collector's facing direction,
                // that means the belt is "in front" of the collector.
                if (collector.direction == dir) {
                    // Collector outputs items onto this belt
                    collector.setOutputBelt(belt);
                }
            }

            // 2) Output to storage
            if (targetTile instanceof StorageFactoryTile) {
                StorageFactoryTile storage = (StorageFactoryTile) targetTile;

                // If the offset direction == collector's facing direction,
                // that means the storage is "in front" of the collector.
                if (collector.direction == dir) {
                    // Collector outputs items into storage
                    collector.setOutputStorage(storage);
                }
            }
        }

        // -------------------------------------------------------
        // BELT OR STORAGE => RESOURCE COLLECTOR (INPUT)
        // The collector pulls items from behind, i.e. opposite the collector’s facing direction.
        // So from the collector’s perspective, the input tile is the tile that, from collector => tile, is the OPPOSITE direction.
        // -------------------------------------------------------
        if (targetTile instanceof ResourceCollectorFactoryTile) {
            ResourceCollectorFactoryTile collector = (ResourceCollectorFactoryTile) targetTile;

            // We want to see if the SOURCE tile is in the "opposite direction" from the collector.
            // That means: if from the collector => source is `oppositeDirection(collector.direction)`,
            // then the collector can pull from that tile.
            // BUT we currently have "dir" = (source->target).
            // So from (target->source) perspective, it's the negative offset or reversed direction.

            // The easiest approach: we do the same two-way call to updateRelationship, meaning
            // if (targetTile, sourceTile, offsetReversed).
            // However, we can also inline-check:

            // If dir == getOppositeDirection( collector.direction ), that means:
            // "from source => collector" is the same as "collector is behind source",
            // which implies from collector => source is the collector’s opposite direction.
            if (dir == getOppositeDirection(collector.direction)) {

                // So if the SOURCE is a belt, it means this belt is behind the collector
                if (sourceTile instanceof ConveyorBeltTile) {
                    ConveyorBeltTile belt = (ConveyorBeltTile) sourceTile;
                    // The collector pulls from belt
                    collector.setInputBelt(belt);
                }
                // If you allow pulling from storage, you'd handle that similarly:
                // if (sourceTile instanceof StorageFactoryTile) { ... }
            }
        }
    }

    private boolean isPerpendicular(GameConstants.DirectionType d1, GameConstants.DirectionType d2) {
        boolean d1Vertical = (d1 == GameConstants.DirectionType.NORTH || d1 == GameConstants.DirectionType.SOUTH);
        boolean d2Vertical = (d2 == GameConstants.DirectionType.NORTH || d2 == GameConstants.DirectionType.SOUTH);
        return (d1Vertical && !d2Vertical) || (!d1Vertical && d2Vertical);
    }

    private GameConstants.DirectionType getDirectionFromOffset(int[] offset) {
        int dx = offset[0];
        int dy = offset[1];

        if (dx == 0 && dy == 1)  return GameConstants.DirectionType.NORTH;
        if (dx == 1 && dy == 0)  return GameConstants.DirectionType.EAST;
        if (dx == 0 && dy == -1) return GameConstants.DirectionType.SOUTH;
        if (dx == -1 && dy == 0) return GameConstants.DirectionType.WEST;

        return null;
    }

    private GameConstants.DirectionType getOppositeDirection(GameConstants.DirectionType dir) {
        switch (dir) {
            case NORTH: return GameConstants.DirectionType.SOUTH;
            case SOUTH: return GameConstants.DirectionType.NORTH;
            case EAST: return GameConstants.DirectionType.WEST;
            case WEST: return GameConstants.DirectionType.EAST;
            default: return null;
        }
    }

}
