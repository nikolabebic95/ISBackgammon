package etf.backgammon.bn140314d.players;

import etf.backgammon.bn140314d.gui.GraphicTable;
import etf.backgammon.bn140314d.logic.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public class RandomBot implements IPlayer {

    @Override
    public Move playMove(IGame game, PlayerId myId, Dice dice) {
        Random random = new Random();

        ArrayList<Move> moves = game.calculateAllPossibleMoves(myId, dice);
        return moves.get(random.nextInt(moves.size()));
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

        RandomBot bot = new RandomBot();


        for (int i = 0; i < 50; i++) {
            Move move = bot.playMove(game, PlayerId.FIRST, Dice.roll());
            game.tryPlayMove(move);
            move = bot.playMove(game, PlayerId.SECOND, Dice.roll());
            game.tryPlayMove(move);
        }

        graphicTable.repaint();
    }
}
