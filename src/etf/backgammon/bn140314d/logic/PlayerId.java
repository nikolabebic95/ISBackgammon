package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public enum PlayerId {
    NONE,
    FIRST,
    SECOND;

    public PlayerId other() {
        if (this == PlayerId.FIRST) {
            return PlayerId.SECOND;
        } else {
            return PlayerId.FIRST;
        }
    }

    public String toString() {
        if (this == PlayerId.FIRST) {
            return "Yoda";
        } else {
            return "Stormtrooper";
        }
    }
}
