package etf.backgammon.bn140314d.logic;

import java.util.ArrayList;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public interface ITable {

    /**
     * Total number of fields on the table
     */
    int NUMBER_OF_FIELDS = 24;

    /**
     * Total number of checkers per player
     */
    int NUMBER_OF_CHECKERS_PER_PLAYER = 15;

    /**
     * Gets the field at the specified index
     * Player 1 moves towards 23, player 2 moves towards 0
     * @param index Index of field
     * @return IField object at the index
     */
    IField getField(int index);

    /**
     * Moves one chip
     * @param index Index of the chip to be moved
     * @param numberOfFields Number of fields chip is moved
     * @return True if the move was successfull, false otherwise
     */
    boolean moveChip(int index, int numberOfFields);

    /**
     * Spawns one chip from bar
     * @param playerId Player ID
     * @param numberOfFields Number of fields chip is moved from the beggining
     * @return True if the spawn was successfull, false otherwise
     */
    boolean spawnFromBar(PlayerId playerId, int numberOfFields);

    /**
     * Makes a deep copy of the table, and returns it
     * @return Deep copy of the table
     */
    ITable makeCopy();

    /**
     * Calculates the number of points for the given player
     * @param playerId Player ID
     * @return Number of points of the player
     */
    int calculateNumberOfPoints(PlayerId playerId);

    int getPlayerOneBar();

    int getPlayerTwoBar();

    int getPlayerOneOff();

    int getPlayerTwoOff();

    ArrayList<Integer> getAllIndicesWithPlayer(PlayerId playerId);
}
