package etf.backgammon.bn140314d.gui;

import etf.backgammon.bn140314d.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class GraphicTable extends JPanel {

    // region Images

    private final static Image boardImage = (new ImageIcon("images/space.jpg")).getImage();
    private final static Image playerOneImage = (new ImageIcon("images/yoda.gif")).getImage();
    private final static Image playerTwoImage = (new ImageIcon("images/stormtrooper.png")).getImage();
    private final static Image upField = (new ImageIcon("images/fieldup.png")).getImage();
    private final static Image downField = (new ImageIcon("images/fielddown.png")).getImage();

    // endregion

    // region Helpers

    private Point convertIndex(int index) {
        if (index < FIELDS_IN_ROW) {
            return new Point((FIELDS_IN_ROW - index - 1) * FIELD_DIMENSION, (FIELDS_IN_COLUMN - 1) * FIELD_DIMENSION);
        } else {
            return new Point((index - FIELDS_IN_ROW) * FIELD_DIMENSION, 0);
        }
    }

    private void paintFields(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();
        for (int i = 0; i < FIELDS_IN_ROW; i++) {
            g.drawImage(upField, 0, 0, FIELD_DIMENSION, FIELD_DIMENSION * FIELDS_IN_COLUMN / 2, this);
            g.translate(0, FIELDS_IN_COLUMN / 2 * FIELD_DIMENSION);
            g.drawImage(downField, 0, 0, FIELD_DIMENSION, FIELD_DIMENSION * FIELDS_IN_COLUMN / 2, this);
            g.translate(FIELD_DIMENSION, -FIELDS_IN_COLUMN / 2 * FIELD_DIMENSION);
        }

        g.setTransform(oldTransform);
    }

    private void paintBoard(Graphics2D g) {
        g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void paintChecker(Graphics2D g, IField field, int index) {
        Point point = convertIndex(index);

        AffineTransform oldTransform = g.getTransform();
        g.translate(point.x, point.y);

        Image image = field.getPlayerId() == PlayerId.FIRST ? playerOneImage : playerTwoImage;

        for (int i = 0; i < field.getNumberOfChips(); i++) {
            g.drawImage(image, 0, 0, FIELD_DIMENSION, FIELD_DIMENSION, this);
            if (index < ITable.NUMBER_OF_FIELDS / 2) {
                g.translate(0, -FIELD_DIMENSION);
            } else {
                g.translate(0, FIELD_DIMENSION);
            }
        }

        g.setTransform(oldTransform);
    }

    private void paintCheckers(Graphics2D g) {
        for (int i = 0; i < ITable.NUMBER_OF_FIELDS; i++) {
            IField field = tableImplementation.getField(i);
            if (field.getPlayerId() != PlayerId.NONE) {
                paintChecker(g, field, i);
            }
        }
    }

    // endregion

    // region Private fields

    private final ITable tableImplementation;

    // endregion

    // region Public interface

    public final static int FIELD_DIMENSION = 64;
    public final static int FIELDS_IN_COLUMN = 10;
    public final static int FIELDS_IN_ROW = ITable.NUMBER_OF_FIELDS / 2;

    public GraphicTable(ITable tableImplementation) {
        this.tableImplementation = tableImplementation;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintBoard(g2d);
        paintFields(g2d);
        paintCheckers(g2d);
    }

    // endregion

    // region Test

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        Table table = new Table(new FieldFactory());

        Game game = new Game();

        game.start(table);

        GraphicTable graphicTable = new GraphicTable(table);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setVisible(true);
        frame.getContentPane().add(graphicTable);
        graphicTable.repaint();
    }

    // endregion
}
