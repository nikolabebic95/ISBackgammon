package etf.backgammon.bn140314d.players;

import java.util.Random;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class Dice {

    // region Private fields and constructor

    private int smallerDie;
    private int greaterOrEqualDie;

    private Dice(int smallerDie, int greaterOrEqualDie) {
        this.smallerDie = smallerDie;
        this.greaterOrEqualDie = greaterOrEqualDie;
    }

    // endregion

    // region Public interface

    public int getSmallerDie() {
        return smallerDie;
    }

    public int getGreaterOrEqualDie() {
        return greaterOrEqualDie;
    }

    // endregion

    // region Dice roll implementation

    private static final int DICE_MAX = 6;
    private static final Random randomGenerator = new Random();

    public static Dice roll() {
        int small = randomGenerator.nextInt(DICE_MAX) + 1;
        int big = randomGenerator.nextInt(DICE_MAX) + 1;
        if (small > big) {
            int temp = small;
            small = big;
            big = temp;
        }

        return new Dice(small, big);
    }

    // endregion
}
