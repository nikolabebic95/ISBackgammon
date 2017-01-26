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
}

class Node {
    IGame game;
    double value;
    int depth;
    PlayerId playerId;
    Dice dice;
    boolean isDice;
    Move bestMove;

    Node(IGame game, int depth, PlayerId playerId, Dice dice, boolean isDice) {
        this.game = game;
        this.depth = depth;
        this.playerId = playerId;
        this.dice = dice;
        this.isDice = isDice;
    }

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
