package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public interface IGame {

    int STARTING_NUMBER_OF_POINTS = 167;

    /**
     * Prepares the object for the game start
     */
    void start(ITable newTable);

    /**
     * Checks for winner of the game
     * @return ID of the winner, or PlayerId.NONE if there is no winner
     */
    PlayerId checkWinner();

    /**
     * Tries to play the move
     * @param move Move to be played
     * @return True if the move was played, false if it is not valid and not played
     */
    boolean tryPlayMove(Move move);

    /**
     * Gets the number of points for the first player
     * @return Number of points
     */
    int playerOnePoints();

    /**
     * Gets the number of points fot the second player
     * @return Number of points
     */
    int playerTwoPoints();
}