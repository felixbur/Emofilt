// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

//import com.ms.wfc.util.Debug.*;
/*
 * Java purists will want to coment some lines of this file out
 * 
 *
 * @version 	1.0,11/01/1998
 * @author Panos Kougiouris
 */

public class Util
{
    // If you do not tatrget the Microsoft Win32 VM
	// comment out the following few lines. Always return
    // "" from this function. It will just remove the
    // env. variable functionality
	// If you know how to get an env. variable in
	// Pure Java pls e-mail me panos@acm.org
    static public String GetEnvVariable(String name) {
        StringBuffer strBuf = new StringBuffer(200);
//        int ret = com.ms.win32.Kernel32.GetEnvironmentVariable(
//               name, strBuf, 200);
//        if (ret != 0) {
//            return strBuf.toString();
//        } else {
            return "";
//        }
    }
    
    // Just comment the line out for pure java environments
    static public void utilAssert(boolean cond, String msg) {
//        com.ms.wfc.util.Debug.assert(cond, msg);    
    }
}
