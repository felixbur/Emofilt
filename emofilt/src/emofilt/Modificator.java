/*
 * Created on 27.05.2005
 *
 * @author Felix Burkhardt
 */
package emofilt;

/**
 * An interface for a class that modifies an utterance.
 * 
 * @author Felix Burkhardt
 */
public interface Modificator {
    public Utterance changeUtterance(Utterance u);
}
