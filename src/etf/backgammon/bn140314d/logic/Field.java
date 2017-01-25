package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class Field implements IField {

    // region Private fields
    private PlayerId playerId = PlayerId.NONE;
    private int numberOfChips = 0;
    // endregion

    // region IField implementation

    @Override
    public PlayerId getPlayerId() {
        return playerId;
    }

    @Override
    public int getNumberOfChips() {
        return numberOfChips;
    }

    @Override
    public void increaseNumberOfChips(PlayerId playerId, int number) {
        this.numberOfChips += number;
        this.playerId = playerId;

        // TODO: Think about throwing an exception when wrong ID is passed
    }

    @Override
    public void decreaseNumberOfChips(PlayerId playerId, int number) {
        this.numberOfChips -= number;
        if (this.numberOfChips == 0) {
            this.playerId = PlayerId.NONE;
        }

        // TODO: Think about changing the method signature (ID not needed)
        // TODO: Thing about throwing an exception when number of chips gets below zero
    }

    @Override
    public Field makeCopy() {
        Field ret = new Field();
        ret.playerId = this.playerId;
        ret.numberOfChips = this.numberOfChips;

        return ret;
    }


    // endregion
}
