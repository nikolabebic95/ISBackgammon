package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class Table implements ITable {

    // region Private fields

    private final IField[] fields = new IField[ITable.NUMBER_OF_FIELDS];

    private int playerOneBar = 0;
    private int playerTwoBar = 0;

    private int playerOneOff = 0;
    private int playerTwoOff = 0;

    // endregion

    // region Constructors

    private Table() {
    }

    public Table(IFieldFactory fieldFactory) {
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fieldFactory.create();
        }
    }

    // endregion

    // region Helpers

    private boolean canBePlaced(PlayerId playerId, int index) {
        // The chip can be placed on the field if and only if:
        //   1) The field is empty
        //   2) The field contains some chips from the current player
        //   3) The field contains only one chip from the opposing player
        return fields[index].getPlayerId() == PlayerId.NONE || fields[index].getPlayerId() == playerId || fields[index].getNumberOfChips() == 1;
    }

    private boolean canHit(PlayerId playerId, int index) {
        return fields[index].getPlayerId() != PlayerId.NONE && fields[index].getPlayerId() != playerId && fields[index].getNumberOfChips() == 1;
    }

    private void hit(PlayerId playerId) {
        if (playerId == PlayerId.FIRST) {
            playerTwoBar++;
        } else {
            playerOneBar++;
        }
    }

    private boolean canBearOff(PlayerId playerId) {
        if (playerId == PlayerId.FIRST && playerOneBar != 0) {
            return false;
        } else if (playerId == PlayerId.SECOND && playerTwoBar != 0) {
            return false;
        }

        int offset = playerId == PlayerId.SECOND ? ITable.NUMBER_OF_FIELDS / 4 : 0;
        for (int i = 0; i < ITable.NUMBER_OF_FIELDS * 3 / 4; i++) {
            if (fields[i + offset].getPlayerId() == playerId) {
                return false;
            }
        }

        return true;
    }

    private void bearOff(PlayerId playerId) {
        if (playerId == PlayerId.FIRST) {
            playerOneOff++;
        } else {
            playerTwoOff++;
        }
    }

    private boolean tryPutChip(PlayerId playerId, int index) {
        if (!canBePlaced(playerId, index)) {
            return false;
        }

        if (canHit(playerId, index)) {
            this.fields[index].decreaseNumberOfChips(playerId);
            hit(playerId);
        }

        this.fields[index].increaseNumberOfChips(playerId);

        return true;
    }

    // endregion

    // region ITable implementation

    @Override
    public IField getField(int index) {
        return this.fields[index];
    }

    @Override
    public boolean moveChip(int index, int numberOfFields) {
        PlayerId playerId = this.fields[index].getPlayerId();

        int newIndex;
        if (playerId == PlayerId.FIRST) {
            newIndex = index + numberOfFields;
        }
        else {
            newIndex = index - numberOfFields;
        }

        if (canBearOff(playerId) && (newIndex < 0 || newIndex >= ITable.NUMBER_OF_FIELDS)) {
            bearOff(playerId);
            fields[index].decreaseNumberOfChips(playerId);
            return true;
        }

        boolean ret = tryPutChip(playerId, newIndex);
        if (ret) {
            fields[index].decreaseNumberOfChips(playerId);
        }

        return ret;
    }

    @Override
    public boolean spawnFromBar(PlayerId playerId, int numberOfFields) {
        int index;
        if (playerId == PlayerId.FIRST) {
            index = numberOfFields - 1;
        }
        else {
            index = ITable.NUMBER_OF_FIELDS - numberOfFields;
        }

        return tryPutChip(playerId, index);
    }

    @Override
    public Table makeCopy() {
        Table ret = new Table();
        for (int i = 0; i < ret.fields.length; i++) {
            ret.fields[i] = this.fields[i].makeCopy();
        }

        ret.playerOneBar = this.playerOneBar;
        ret.playerOneOff = this.playerOneOff;
        ret.playerTwoBar = this.playerTwoBar;
        ret.playerTwoOff = this.playerTwoOff;

        return ret;
    }

    // endregion
}
