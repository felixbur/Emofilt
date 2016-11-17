package emofilt.gui;
import javax.swing.*;

/**
 * Class SimpleInput - input class for input of simple input types
 * via simple dialog box.
 * eg. int, char, String,float or boolean.
 *
 * @author Bruce Quig
 * @author Michael Kolling
 * @author Eugene Ageenko
 *
 * @version 1.2
 * Modified (Mar 19, 2003): methods converted to static, constructor is hidden.
 * Major corrections in behaviour. Support default value for the input.
 */

public class SimpleInput {
    // instance variables
    static final String STRING_TITLE = "Enter a String";
    static final String CHAR_TITLE = "Enter a char";
    static final String INT_TITLE = "Enter an int";
    static final String BOOLEAN_TITLE = "Select True or False";
    static final String FLOAT_TITLE = "Enter a float";
    static final String TRUE = "True";
    static final String FALSE = "False";
    static final String EMPTY_STRING = "";

    /**
     *  No constructor by default.
     */
    private SimpleInput() {
    }

    /**
     ** String input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @return String input from the user.
     **/
    public static String getString(String prompt) {
        return getString(prompt,"");
    }

    /**
     ** String input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input string that is initially displayed as selected by the user
     ** @return String input from the user.
     **/
    public static String getString(String prompt, String initialValue) {
        Object[] commentArray = {prompt, EMPTY_STRING, EMPTY_STRING};
        Object[] options = {"OK"};

        boolean validResponse = false;

        String result = null;

        while (!validResponse) {
            final JOptionPane optionPane = new JOptionPane(commentArray,
                                                           JOptionPane.QUESTION_MESSAGE,
                                                           JOptionPane.OK_CANCEL_OPTION,
                                                           null,
                                                           options,
                                                           options[0]);

            optionPane.setWantsInput(true);
            optionPane.setInitialSelectionValue(initialValue);  // EA: added
            JDialog dialog = optionPane.createDialog(null, STRING_TITLE);

            dialog.pack();
            dialog.show();

            Object response = optionPane.getInputValue();

            if (response != JOptionPane.UNINITIALIZED_VALUE) {
                result = (String) response;
                if (result != null) // EA: added for completnes
                    validResponse = true;
                else {
                    commentArray[1] = "Invalid entry : ";
                    commentArray[2] = "Enter a valid String";
                }
            } else {
                commentArray[1] = "Must enter a string";
                commentArray[2] = EMPTY_STRING;
            }
        }
        return result;
    }

    /**
     ** returns character input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @return the input character
     **/
    public static char getChar(String prompt) {
        return getChar(prompt,"");
    }

    /**
     ** returns character input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input value that is initially displayed as selected by the user
     ** @return the input character
     **/
    public static char getChar(String prompt, char initialValue) {
        return getChar(prompt,Character.toString(initialValue));
    }

    /**
     ** returns character input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input string that is initially displayed as selected by the user
     ** @return the input character
     **/
    public static char getChar(String prompt, String initialValue) {
        char response = (initialValue != null) ? initialValue.charAt(0) : '-'; // EA: modified

        String result = null;

        Object[] commentArray = {prompt, EMPTY_STRING, EMPTY_STRING};
        Object[] options = {"OK"};

        boolean validResponse = false;

        while (!validResponse) {
            final JOptionPane optionPane = new JOptionPane(commentArray,
                                                           JOptionPane.QUESTION_MESSAGE,
                                                           JOptionPane.OK_CANCEL_OPTION,
                                                           null,
                                                           options,
                                                           options[0]);

            optionPane.setWantsInput(true);
            optionPane.setInitialSelectionValue(initialValue);  // EA: added
            JDialog dialog = optionPane.createDialog(null, CHAR_TITLE);

            dialog.pack();
            dialog.show();

            result = null; // EA: added for convinience;
            // EA: why character processed in another way that integer?
            // EA: meaning that with check for uinitialized case then assignment?

            Object input = optionPane.getInputValue();
            if (input != JOptionPane.UNINITIALIZED_VALUE) {
                result = (String) input;
                if (result != null) {
                    if (result.length() == 1) {
                        response = result.charAt(0);
                        validResponse = true;
                    } else {
                        commentArray[1] = "Invalid entry : " + result;
                        commentArray[2] = "Enter a single character";
                    }
                } else {
                    commentArray[1] = "Invalid entry"; // EA: corrected, no point to print null-object. Question: when it is possible to have null objects?
                    commentArray[2] = "Enter a single character";
                }
            } else {
                commentArray[1] = "Must enter a single character";  //EA: error corrected, result removed
                commentArray[2] = EMPTY_STRING; //EA: cannot use result since it is not initialized
            }
        }
        return response;
    }


    /**
     ** boolean selection from the user via a simple dialog.
     ** @param  prompt message to appear in dialog
     ** @param  trueText message to appear on true "button"
     ** @param  falseText message to appear on "false" button
     ** @return boolean selection from the user
     **/
    public static boolean getBoolean(String prompt, String trueText, String falseText) {
        Object[] commentArray = {prompt, EMPTY_STRING};
        boolean validResponse = false;
        int result = -1;

        while (!validResponse) {
            Object[] options = {trueText, falseText};
            result = JOptionPane.showOptionDialog(null,
                                                  commentArray,
                                                  BOOLEAN_TITLE,
                                                  JOptionPane.YES_NO_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE,
                                                  null, //don't use a custom Icon
                                                  options, //the titles of buttons
                                                  trueText); //the title of the default button, EA: CORRECTED from TRUE

            // check true or false buttons pressed
            if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) // CORRECTED from 0:1
            {
                validResponse = true;
            } else {
                commentArray[1] = "Incorrect selection : Choose true or false buttons";
            }
        }
        return (result == 0);
    }


    /**
     ** boolean selection from the user via a simple dialog.
     ** @param  prompt message to appear in dialog
     ** @return boolean selection from the user
     **/
    public static boolean getBoolean(String prompt) {
        return getBoolean(prompt, TRUE, FALSE);
    }


    /**
     ** returns integer input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @return the input integer
     */
    public static int getInt(String prompt) {
        return getInt(prompt,"");
    }

    /**
     ** returns integer input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input value that is initially displayed as selected by the user
     ** @return the input integer
     **/
    public static int getInt(String prompt, int initialValue) {
        return getInt(prompt,Integer.toString(initialValue));
    }

    /**
     ** returns integer input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input string that is initially displayed as selected by the user
     ** @return the input integer
     **/
    public static int getInt(String prompt, String initialValue) {
        Object[] commentArray = {prompt, EMPTY_STRING, EMPTY_STRING};
        Object[] options = {"OK"};

        boolean validResponse = false;

        int response = 0;
        while (!validResponse) {
            final JOptionPane optionPane = new JOptionPane(commentArray,
                                                           JOptionPane.QUESTION_MESSAGE,
                                                           JOptionPane.OK_CANCEL_OPTION,
                                                           null,
                                                           options,
                                                           options[0]);

            optionPane.setWantsInput(true);
            optionPane.setInitialSelectionValue(initialValue);  // EA: added
            JDialog dialog = optionPane.createDialog(null, INT_TITLE);

            dialog.pack();
            dialog.show();

            // EA: rewritten as in getChar function
            // EA: added or corrected non-portable check for uninitialized value situation
            Object input = optionPane.getInputValue();
            if (input == JOptionPane.UNINITIALIZED_VALUE) {
                commentArray[1] = "Must enter an integer value"; // EA: explanatory text added
                commentArray[2] = EMPTY_STRING;
            } else {
                String result = (String) input;
                if (result == null) { // EA: added for completnes, but is this situation possible?
                    commentArray[1] = "Invalid integer:";
                    commentArray[2] = "Enter a valid integer";
                } else {
                    try {
                        //workaround for BlueJ bug - misses first exception after compilation
                        //response = Integer.parseInt(result); // EA: ?
                        response = Integer.parseInt(result);
                        validResponse = true;
                    } catch (NumberFormatException exception) {
                        commentArray[1] = "Invalid integer: " + result;
                        commentArray[2] = "Enter a valid integer";
                        initialValue = result; // EA: added
                    }
                }
            }
        }
        return response;
    }


    /**
     ** returns a float input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @return the input float
     **/
    public static float getFloat(String prompt) {
        return getFloat(prompt,"");
    }

    /**
     ** returns float input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input value that is initially displayed as selected by the user
     ** @return the input float
     **/
    public static float getFloat(String prompt, float initialValue) {
        return getFloat(prompt,Float.toString(initialValue));
    }

    /**
     ** returns float input from the user via a simple dialog.
     ** @param prompt the message string to be displayed inside dialog
     ** @param initialValue input string that is initially displayed as selected by the user
     ** @return the input float
     **/
    public static float getFloat(String prompt, String initialValue) {
        Object[] options = {"OK"};
        Object[] commentArray = {prompt, EMPTY_STRING, EMPTY_STRING};

        boolean validResponse = false;

        float response = 0.0f;

        while (!validResponse) {
            final JOptionPane optionPane = new JOptionPane(commentArray,
                                                           JOptionPane.QUESTION_MESSAGE,
                                                           JOptionPane.OK_CANCEL_OPTION,
                                                           null,
                                                           options,
                                                           options[0]);

            optionPane.setWantsInput(true);
            optionPane.setInitialSelectionValue(initialValue);  // EA: added
            JDialog dialog = optionPane.createDialog(null, FLOAT_TITLE);

            dialog.pack();
            dialog.show();

            Object input = optionPane.getInputValue();
            if (input == JOptionPane.UNINITIALIZED_VALUE) {
                commentArray[1] = "Must enter a float value"; // EA: explanatory text added
                commentArray[2] = EMPTY_STRING;
            } else {
                String result = (String) input;
                if (result == null) { // EA: added for completnes, but is this situation possible?
                    commentArray[1] = "Invalid float:";
                    commentArray[2] = "Enter a valid float";
                } else {
                    // convert String to float
                    try {
                        // workaround for BlueJ bug - misses first exception after recompilation?
                        response = Float.valueOf(result).floatValue();
                        response = Float.valueOf(result).floatValue();
                        validResponse = true;
                    } catch (NumberFormatException exception) {
                        // EA: case with uninitialized value is moved up
                        commentArray[1] = "Invalid float: " + result;
                        commentArray[2] = "Enter a valid float";
                        initialValue = result;    // EA: corrected
                    }
                }
            }
        }
        return response;
    }
}
