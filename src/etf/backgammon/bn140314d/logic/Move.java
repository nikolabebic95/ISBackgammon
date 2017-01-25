package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class Move {

    // Bar is represented with -1 in the chipIndices array

    public PlayerId playerId;
    public int[] chipIndices;
    public int[] chipMoves;
    public int numberOfMovedChips;

    public Move(PlayerId playerId, int numberOfMovedChips) {
        this.playerId = playerId;
        this.numberOfMovedChips = numberOfMovedChips;
        this.chipIndices = new int[numberOfMovedChips];
        this.chipMoves = new int[numberOfMovedChips];
    }
}
