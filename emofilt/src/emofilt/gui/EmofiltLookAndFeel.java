package emofilt.gui;
/*
* This file is copyright to javareference.com
* for more information visit,
* http://www.javareference.com
*/

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.*;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltLookAndFeel extends MetalLookAndFeel
{
    /**
     * This method is overriden from MetalLookAndFeel to set a different color for
     * the ToolTip background
     */
    protected void initSystemColorDefaults(UIDefaults table)
    {
        //call the super method and populate the UIDefaults table
        super.initSystemColorDefaults(table);

        //After populating the UIDefaults table reset the info key value to the
        //desired color. In this case lightYellow
        table.put("info", new ColorUIResource(255, 255, 255));
    }

} 