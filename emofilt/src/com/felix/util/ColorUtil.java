package com.felix.util;

import java.awt.Color;

/**
 * Util function around color.
 * 
 * @author felix
 * 
 */
public class ColorUtil {
	/**
	 * Get the hex string for HTML from a color.
	 * 
	 * @param c
	 *            The color.
	 * @return The Hexadecimal string.
	 */
	public static final String colorToHex(Color c) {
		// return Integer.toHexString( c.getRGB() & 0x00ffffff );
		String r = Integer.toHexString(c.getRed());
		String g = Integer.toHexString(c.getGreen());
		String b = Integer.toHexString(c.getBlue());
		if (r.length() <= 1)
			r = "0" + r;
		if (g.length() <= 1)
			g = "0" + g;
		if (b.length() <= 1)
			b = "0" + b;
		return r + g + b;
	}

	/**
	 * Return a java color object from a hex str, e.g. for html.
	 * 
	 * @param hexString
	 *            The hext String, e.g. ff0000 for red.
	 * @return The color.
	 */
	public static final Color hexStringToColor(String hexString) {
		int r = Integer.parseInt(hexString.substring(0, 2), 16);
		int g = Integer.parseInt(hexString.substring(2, 4), 16);
		int b = Integer.parseInt(hexString.substring(4, 6), 16);
		return new Color(r, g, b);
	}
}
