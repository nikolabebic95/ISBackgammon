package etf.backgammon.bn140314d.players;

import etf.backgammon.bn140314d.gui.GraphicTable;
import etf.backgammon.bn140314d.logic.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public class ExpectiMiniMaxBot implements IPlayer {

    private int depth;

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public Move playMove(IGame game, PlayerId myId, Dice dice) {
        Node node = new Node(game, depth, myId, dice, false);
        expectiMinimax(node);
        return node.bestMove;
    }

    /**
     *
     * @param node Node from which the search is run
     * @return Evaluation of the best move
     *
     * Algorithm:
     *
     * 1) If the maximum depth was reached, return the evaluation
     * 2) If node type is Dice throw:
     *    1) Generate all possible dice rolls
     *    2) Generate nodes from dice rolls
     *    3) Call expectiMinimax on each node
     *    4) Return the average of the children nodes
     * 3) If node type is player (MIN or MAX)
     *    1) Check for winner
     *       1) If MIN is the winner, return 0
     *       2) If MAX is the winner, return positive infinity
     *    2) Generate all possible moves
     *    3) Try to play all possible moves
     *    4) Call expectiMinimax on each move
     *    5) Find the best of all moves and return it
     *
     */
    private double expectiMinimax(Node node) {
        if (node.depth == 0) {
            node.calculateValue();
            return node.value;
        }

        if (node.isDice) {
            double sum = 0;
            int num = 0;

            for (int i = 1; i <= Dice.DICE_MAX; i++) {
                for (int j = 1; j <= Dice.DICE_MAX; j++) {
                    Dice dice = new Dice(i, j);
                    Node child = new Node(node.game, node.depth - 1, node.playerId, dice, false);
                    sum += expectiMinimax(child);
                    num++;
                }
            }

            node.value = sum / num;
        } else if (node.playerId == PlayerId.FIRST) {
            if (node.game.checkWinner() == PlayerId.FIRST) {
                return 0;
            } else if (node.game.checkWinner() == PlayerId.SECOND) {
                return Double.POSITIVE_INFINITY;
            }

            node.value = Double.POSITIVE_INFINITY;

            ArrayList<Move> moves = node.game.calculateAllPossibleMoves(node.playerId, node.dice);
            if (moves == null) {
                node.calculateValue();
                return node.value;
            }

            for (Move move : moves) {
                IGame copy = node.game.makeCopy();
                boolean ok = copy.tryPlayMove(move);
                if (!ok) {
                    continue;
                }

                Node child = new Node(copy, node.depth - 1, node.playerId.other(), node.dice, true);
                expectiMinimax(child);
                if (child.value < node.value) {
                    node.bestMove = move;
                    node.value = child.value;
                }
            }

            if (node.value == Double.POSITIVE_INFINITY) {
                node.calculateValue();
                return node.value;
            }
        } else {
            if (node.game.checkWinner() == PlayerId.FIRST) {
                return 0;
            } else if (node.game.checkWinner() == PlayerId.SECOND) {
                return  Double.POSITIVE_INFINITY;
            }

            node.value = -1; // Will never be below 0

            ArrayList<Move> moves = node.game.calculateAllPossibleMoves(node.playerId, node.dice);
            if (moves == null) {
                node.calculateValue();
                return node.value;
            }

            for (Move move : moves) {
                IGame copy = node.game.makeCopy();
                boolean ok = copy.tryPlayMove(move);
                if (!ok) {
                    continue;
                }

                Node child = new Node(copy, node.depth - 1, node.playerId.other(), node.dice, true);
                expectiMinimax(child);
                if (child.value > node.value) {
                    node.bestMove = move;
                    node.value = child.value;
                }
            }
        }

        return node.value;
    }

    // region Test

    public static void main(String[] args) {
        Game game = new Game();
        Table table = new Table(new FieldFactory());

        GraphicTable graphicTable = new GraphicTable(table);
        game.start(table);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setVisible(true);
        frame.getContentPane().add(graphicTable);
        graphicTable.repaint();

        frame.repaint();

        RandomBot bot = new RandomBot();

        ExpectiMiniMaxBot minimax = new ExpectiMiniMaxBot();
        minimax.setDepth(4);

        while (true) {
            Move move = minimax.playMove(game, PlayerId.FIRST, Dice.roll());
            game.tryPlayMove(move);
            if (game.checkWinner() != PlayerId.NONE) {
                System.out.println(game.checkWinner());
                break;
            }

            graphicTable.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
            }
            move = minimax.playMove(game, PlayerId.SECOND, Dice.roll());
            game.tryPlayMove(move);
            if (game.checkWinner() != PlayerId.NONE) {
                System.out.println(game.checkWinner());
                break;
            }


            graphicTable.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
            }
        }

        graphicTable.repaint();
    }

    // endregion
}

/**
 * Represents the node of the ExpectiMinimax Algorithm
 */
class Node {
    /**
     * Game object for the node
     * Should be copied for children nodes, if the change is needed
     */
    IGame game;

    /**
     * Value the expectiMinimax algorithm calculates
     */
    double value;

    /**
     * How much more in depth should we go
     */
    int depth;

    /**
     * Current player ID
     * Dice throw player ID changes before the dice throw
     */
    PlayerId playerId;

    /**
     * Dice throw
     */
    Dice dice;

    /**
     * Whether the node type is Dice
     */
    boolean isDice;

    /**
     * Best move of this node
     * If the node has no moves, null
     */
    Move bestMove = null;

    Node(IGame game, int depth, PlayerId playerId, Dice dice, boolean isDice) {
        this.game = game;
        this.depth = depth;
        this.playerId = playerId;
        this.dice = dice;
        this.isDice = isDice;
    }

    /**
     * Evaluation function
     *
     * Calculates the ratio between player 1 points and player 2 points
     * Player 1 is MIN, player 2 is MAX
     */
    void calculateValue() {
        double playerOne = game.playerOnePoints();
        double playerTwo = game.playerTwoPoints();

        if (playerTwo == 0) {
            value = Double.POSITIVE_INFINITY;
        } else {
            value = playerOne / playerTwo;
        }
    }
}
