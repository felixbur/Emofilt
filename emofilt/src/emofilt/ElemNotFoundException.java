/*
 * Created on 24.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt;

/**
 * If an element is queried but not contained in an emotion descriptor.
 * 
 * @author Felix Burkhardt
 */
public class ElemNotFoundException extends Exception {

    /**
     * Constructor.
     */
    public ElemNotFoundException() {
        super("Element not found in Emotion Description");
    }
}
