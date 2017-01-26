package etf.backgammon.bn140314d.gui;

import etf.backgammon.bn140314d.logic.*;
import etf.backgammon.bn140314d.players.Dice;
import etf.backgammon.bn140314d.players.ExpectiMiniMaxBot;
import etf.backgammon.bn140314d.players.RandomBot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public final class GUI extends JFrame {

    private static final int WIDTH = 910;
    private static final int HEIGHT = 700;

    private PlayerId onMove = PlayerId.FIRST;

    private final Table tableImplementation = new Table(new FieldFactory());

    private final Game game = new Game();

    private final PlayerChoiceTable table = new PlayerChoiceTable(tableImplementation, game);

    private final ExpectiMiniMaxBot ai = new ExpectiMiniMaxBot();
    private final RandomBot random = new RandomBot();

    {
        ai.setDepth(4);
    }

    private final JButton aiButton = new JButton("ExpectiMiniMax");
    private final JButton randomButton = new JButton("Random");
    private final JButton diceButton = new JButton("Roll dice");

    private Dice dice = Dice.roll();

    private JLabel label = new JLabel("Dice: " + dice.getSmallerDie() + ", " + dice.getGreaterOrEqualDie(), SwingConstants.CENTER);

    private JLabel label2 = new JLabel(onMove.toString() + " plays", SwingConstants.CENTER);

    private final JPanel left = new JPanel(new GridLayout(5, 1));

    private final JPanel big = new JPanel(new BorderLayout());

    {
        left.add(aiButton);
        left.add(randomButton);
        left.add(diceButton);
        left.add(label);
        left.add(label2);

        big.add(table, "Center");
        big.add(left, "East");
    }

    {
        game.start(tableImplementation);
        table.setDice(dice);

        aiButton.addActionListener((ActionEvent e) -> {
            Move move = ai.playMove(game, onMove, dice);
            game.tryPlayMove(move);
            repaint();
            if (game.checkWinner() != PlayerId.NONE) {
                JOptionPane.showMessageDialog(this, "Winner: " + game.checkWinner().toString());
            }
        });

        randomButton.addActionListener((ActionEvent e) -> {
            Move move = random.playMove(game, onMove, dice);
            game.tryPlayMove(move);
            repaint();
            if (game.checkWinner() != PlayerId.NONE) {
                JOptionPane.showMessageDialog(this, "Winner: " + game.checkWinner().toString());
            }
        });

        diceButton.addActionListener((ActionEvent e) -> {
            boolean shouldChange = dice.getSmallerDie() != dice.getGreaterOrEqualDie();
            dice = Dice.roll();
            table.setDice(dice);
            if (shouldChange) {
                onMove = onMove.other();
            }

            label.setText("Dice: " + dice.getSmallerDie() + ", " + dice.getGreaterOrEqualDie());
            label2.setText(onMove.toString() + " plays");
        });
    }

    public GUI() {
        super("Backgammon");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        add(big);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
