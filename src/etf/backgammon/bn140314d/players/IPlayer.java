package etf.backgammon.bn140314d.players;

import etf.backgammon.bn140314d.logic.*;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public interface IPlayer {

    Move playMove(IGame game, PlayerId myId, Dice dice);
}
