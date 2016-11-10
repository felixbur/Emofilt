/*
 * Created on 24.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt;

/**
 * If a Phoneme was not found in the language description.
 * 
 * @author Felix Burkhardt
 */
public class PhonemeNotFoundException extends Exception {

    /**
     * Constructor.
     */
    public PhonemeNotFoundException() {
        super("Phoneme not found in Language Description");
    }
    /**
     * Constructor, given a message.
     * @param message The message.
     */
    public PhonemeNotFoundException(String message) {
        super(message);
    }
    

}
