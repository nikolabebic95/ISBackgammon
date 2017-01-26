package etf.backgammon.bn140314d.gui;

import etf.backgammon.bn140314d.logic.*;
import etf.backgammon.bn140314d.players.Dice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public class PlayerChoiceTable extends GraphicTable implements MouseListener, MouseMotionListener {

    // region Private fields

    private Point point;

    private Dice dice = Dice.roll();

    private Game game;

    // endregion

    // Public interface

    public PlayerChoiceTable(ITable tableImplementation, Game game) {
        super(tableImplementation);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.game = game;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    // endregion

    // region Helpers

    private Point pointFromCoordinates(int x, int y) {
        return new Point(x / FIELD_DIMENSION, y / (FIELDS_IN_COLUMN / 2 * FIELD_DIMENSION));
    }

    private int indexFromPoint(Point point) {
        if (point.y == 1) {
            return FIELDS_IN_ROW - point.x - 1;
        } else {
            return FIELDS_IN_ROW + point.x;
        }
    }

    private void drawDesired(Graphics g) {
        if (point != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform oldTransform = g2d.getTransform();
            g2d.translate(point.x * FIELD_DIMENSION, point.y * FIELDS_IN_COLUMN / 2 * FIELD_DIMENSION);
            g2d.setColor(new Color(255, 120, 0, 128));
            g2d.fillRect(0, 0, FIELD_DIMENSION, FIELD_DIMENSION * FIELDS_IN_COLUMN / 2);
            g2d.setTransform(oldTransform);
        }
    }

    // endregion

    // region Overrides

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDesired(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (point != null) {
            int index = indexFromPoint(point);
            PlayerId playerId = game.table().getField(index).getPlayerId();
            if (playerId == PlayerId.NONE) {
                return;
            }

            Move move = new Move(playerId, 1);
            move.chipIndices[0] = index;
            if (SwingUtilities.isLeftMouseButton(e)) {
                move.chipMoves[0] = dice.getSmallerDie();
            } else {
                move.chipMoves[0] = dice.getGreaterOrEqualDie();
            }

            IGame copy = game.makeCopy();
            if (copy.tryPlayMove(move)) {
                game.tryPlayMove(move);
            }

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        point = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        point = pointFromCoordinates(e.getX(), e.getY());
        repaint();
    }

    // endregion
}
