package etf.backgammon.bn140314d.logic;

import etf.backgammon.bn140314d.players.Dice;

import java.util.ArrayList;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public class Game implements IGame {

    // region Private fields

    private PlayerId winner;
    private ITable table;

    // endregion

    // region Helpers

    private void arrangeStartingPositions() {
        table.getField(0).increaseNumberOfChips(PlayerId.FIRST, 2);
        table.getField(11).increaseNumberOfChips(PlayerId.FIRST, 5);
        table.getField(16).increaseNumberOfChips(PlayerId.FIRST, 3);
        table.getField(18).increaseNumberOfChips(PlayerId.FIRST, 5);

        table.getField(5).increaseNumberOfChips(PlayerId.SECOND, 5);
        table.getField(7).increaseNumberOfChips(PlayerId.SECOND, 3);
        table.getField(12).increaseNumberOfChips(PlayerId.SECOND, 5);
        table.getField(23).increaseNumberOfChips(PlayerId.SECOND, 2);
    }

    private void checkForWin() {
        if (table.getPlayerOneOff() == ITable.NUMBER_OF_CHECKERS_PER_PLAYER) {
            winner = PlayerId.FIRST;
        } else if (table.getPlayerTwoOff() == ITable.NUMBER_OF_CHECKERS_PER_PLAYER) {
            winner = PlayerId.SECOND;
        }
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

        if (move == null) {
            return true;
        }

        ITable oldTable = table.makeCopy();

        for (int i = 0; i < move.numberOfMovedChips; i++) {
            if (move.chipIndices[i] == -1) {
                boolean canMove = table.spawnFromBar(move.playerId, move.chipMoves[i]);
                if (!canMove) {
                    table = oldTable;
                    return false;
                } else {
                    continue;
                }
            }

            boolean legal = table.moveChip(move.chipIndices[i], move.chipMoves[i]);
            if (!legal) {
                table = oldTable;
                return false;
            }
        }

        checkForWin();
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

    @Override
    public ArrayList<Move> calculateAllPossibleMoves(PlayerId playerId, Dice dice) {
        ArrayList<Move> ret = new ArrayList<>();

        if (playerId == PlayerId.FIRST && table.getPlayerOneBar() > 1 || playerId == PlayerId.SECOND && table.getPlayerTwoBar() > 1) {
            Move move = new Move(playerId, 2);
            move.chipIndices[0] = -1;
            move.chipIndices[1] = -1;
            move.chipMoves[0] = dice.getSmallerDie();
            move.chipMoves[1] = dice.getGreaterOrEqualDie();
            ret.add(move);
            return ret;
        }

        if (playerId == PlayerId.FIRST && table.getPlayerOneBar() == 1 || playerId == PlayerId.SECOND && table.getPlayerTwoBar() == 1) {
            ArrayList<Integer> indices = table.getAllIndicesWithPlayer(playerId);
            for (Integer i : indices) {
                ITable copy = table.makeCopy();
                boolean ok = copy.moveChip(i, dice.getSmallerDie());
                if (ok) {
                    Move move = new Move(playerId, 2);
                    move.chipIndices[0] = -1;
                    move.chipIndices[1] = i;
                    move.chipMoves[0] = dice.getGreaterOrEqualDie();
                    move.chipMoves[1] = dice.getSmallerDie();
                    ret.add(move);
                }

                copy = table.makeCopy();
                ok = copy.moveChip(i, dice.getGreaterOrEqualDie());
                if (ok) {
                    Move move = new Move(playerId, 2);
                    move.chipIndices[0] = -1;
                    move.chipIndices[1] = i;
                    move.chipMoves[0] = dice.getSmallerDie();
                    move.chipMoves[1] = dice.getGreaterOrEqualDie();
                    ret.add(move);
                }
            }
        }

        ArrayList<Integer> indices = table.getAllIndicesWithPlayer(playerId);
        for (Integer i : indices) {
            for (Integer j : indices) {
                if (i.equals(j) && table.getField(i).getNumberOfChips() == 1) {
                    continue;
                }

                ITable copy = table.makeCopy();
                boolean ok = copy.moveChip(i, dice.getSmallerDie());
                if (ok) {
                    ok = copy.moveChip(j, dice.getGreaterOrEqualDie());
                }

                if (ok) {
                    Move move = new Move(playerId, 2);
                    move.chipIndices[0] = i;
                    move.chipIndices[1] = j;
                    move.chipMoves[0] = dice.getSmallerDie();
                    move.chipMoves[1] = dice.getGreaterOrEqualDie();
                    ret.add(move);
                }
            }
        }

        for (Integer i : indices) {
            ITable copy = table.makeCopy();
            boolean ok = copy.moveChip(i, dice.getSmallerDie());
            if (ok) {
                int newIndex;
                if (playerId == PlayerId.FIRST) {
                    newIndex = i + dice.getSmallerDie();
                } else {
                    newIndex = i - dice.getSmallerDie();
                }
                if (newIndex >= 0 && newIndex < ITable.NUMBER_OF_FIELDS) {
                    ok = copy.moveChip(newIndex, dice.getGreaterOrEqualDie());
                    if (ok) {
                        Move move = new Move(playerId, 2);
                        move.chipIndices[0] = i;
                        move.chipIndices[1] = newIndex;
                        move.chipMoves[0] = dice.getSmallerDie();
                        move.chipIndices[1] = dice.getGreaterOrEqualDie();
                        ret.add(move);
                    }
                }
            }

            if (dice.getGreaterOrEqualDie() == dice.getGreaterOrEqualDie()) {
                continue;
            }

            copy = table.makeCopy();
            ok = copy.moveChip(i, dice.getGreaterOrEqualDie());
            if (ok) {
                int newIndex;
                if (playerId == PlayerId.FIRST) {
                    newIndex = i + dice.getGreaterOrEqualDie();
                } else {
                    newIndex = i - dice.getGreaterOrEqualDie();
                }
                if (newIndex >= 0 && newIndex < ITable.NUMBER_OF_FIELDS) {
                    ok = copy.moveChip(newIndex, dice.getSmallerDie());
                    if (ok) {
                        Move move = new Move(playerId, 2);
                        move.chipIndices[0] = i;
                        move.chipIndices[1] = newIndex;
                        move.chipMoves[0] = dice.getGreaterOrEqualDie();
                        move.chipIndices[1] = dice.getSmallerDie();
                        ret.add(move);
                    }
                }
            }
        }

        if (ret.size() == 0) {
            for (Integer i : indices) {
                ITable copy = table.makeCopy();
                boolean ok = copy.moveChip(i, dice.getSmallerDie());
                if (ok) {
                    Move move = new Move(playerId, 1);
                    move.chipIndices[0] = i;
                    move.chipMoves[0] = dice.getSmallerDie();
                    ret.add(move);
                }

                copy = table.makeCopy();
                ok = copy.moveChip(i, dice.getGreaterOrEqualDie());
                if (ok) {
                    Move move = new Move(playerId, 1);
                    move.chipIndices[0] = i;
                    move.chipMoves[0] = dice.getGreaterOrEqualDie();
                    ret.add(move);
                }
            }
        }

        return ret;
    }

    @Override
    public IGame makeCopy() {
        Game ret = new Game();
        ret.table = this.table.makeCopy();
        ret.winner = this.winner;
        return ret;
    }

    @Override
    public ITable table() {
        return table;
    }

    // endregion
}
