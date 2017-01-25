package etf.backgammon.bn140314d.players;

import etf.backgammon.bn140314d.logic.*;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public interface IPlayer {

    Move playMove(ITable table, PlayerId myId, Dice dice);
}
