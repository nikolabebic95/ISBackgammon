package etf.backgammon.bn140314d.players;

import etf.backgammon.bn140314d.logic.*;

/**
 * @author Nikola Bebic
 * @version 26-Jan-2017
 */
public interface IPlayer {

    /**
     * Function which all players should implement, to play the game
     * @param game Game object
     * @param myId ID of the current player
     * @param dice Dice roll the player got
     * @return Move object the player has chosen
     */
    Move playMove(IGame game, PlayerId myId, Dice dice);
}
