package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class Game implements IGame {

    // region Private fields

    private PlayerId winner;
    private ITable table;

    // endregion

    // region Helpers

    private void arrangeStartingPositions() {
        table.getField(5).increaseNumberOfChips(PlayerId.FIRST, 5);
        table.getField(7).increaseNumberOfChips(PlayerId.FIRST, 3);
        table.getField(12).increaseNumberOfChips(PlayerId.FIRST, 5);
        table.getField(23).increaseNumberOfChips(PlayerId.FIRST, 2);

        table.getField(18).increaseNumberOfChips(PlayerId.SECOND, 5);
        table.getField(16).increaseNumberOfChips(PlayerId.SECOND, 3);
        table.getField(11).increaseNumberOfChips(PlayerId.SECOND, 5);
        table.getField(0).increaseNumberOfChips(PlayerId.SECOND, 2);
    }

    // endregion

    // region IGame implementation

    @Override
    public void start(ITable newTable) {
        this.winner = PlayerId.NONE;
        table = newTable;
        arrangeStartingPositions();
    }

    @Override
    public PlayerId checkWinner() {
        return winner;
    }

    @Override
    public boolean tryPlayMove(Move move) {
        if (winner != PlayerId.NONE) {
            return false;
        }

        ITable oldTable = table.makeCopy();

        for (int i = 0; i < move.numberOfMovedChips; i++) {
            if (move.chipIndices[i] == -1) {
                boolean canMove = table.spawnFromBar(move.playerId, move.chipMoves[i]);
                if (!canMove) {
                    table = oldTable;
                    return false;
                }
            }

            boolean legal = table.moveChip(move.chipIndices[i], move.chipMoves[i]);
            if (!legal) {
                table = oldTable;
                return false;
            }
        }

        return true;
    }

    @Override
    public int playerOnePoints() {
        return table.calculateNumberOfPoints(PlayerId.FIRST);
    }

    @Override
    public int playerTwoPoints() {
        return table.calculateNumberOfPoints(PlayerId.SECOND);
    }

    // endregion
}
