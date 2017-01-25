package etf.backgammon.bn140314d.logic;

/**
 * @author Nikola Bebic
 * @version 25-Jan-2017
 */
public class FieldFactory implements IFieldFactory {

    @Override
    public IField create() {
        return new Field();
    }
}
