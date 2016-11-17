/*
 * Created on 31.08.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.util;

import java.awt.Color;

/**
 * Class to handle color name resolution.
 * 
 * @author Felix Burkhardt
 */
public class Colors {

	/**
	 * Retrieve a color given a name, e.g. "black"
	 * 
	 * @param name
	 *            The name.
	 * @return A color or null if the color is not found.
	 */
	public static Color getColorByName(String name) {
		if (name == null) {
			return null;
		}
		String cs = getHexFromColorName(name.trim());
		try {
			int r = Integer.parseInt(cs.substring(1, 3), 16);
			int g = Integer.parseInt(cs.substring(3, 5), 16);
			int b = Integer.parseInt(cs.substring(5, 7), 16);
			return new Color(r, g, b);
		} catch (Exception e) {
			System.err.println("no color found from: "+cs+", rep.: "+name);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the hex rgb values given a color name.
	 * 
	 * @param name
	 *            The name of the color, e.g. "black".
	 * @return The hex string or an error string of not found.
	 */
	public static String getHexFromColorName(String name) {
		if (name.compareTo("AliceBlue") == 0) {
			return "#F0F8FF";
		}
		if (name.compareTo("AntiqueWhite") == 0) {
			return "#FAEBD7";
		}
		if (name.compareTo("Aqua") == 0) {
			return "#00FFFF";
		}
		if (name.compareTo("Aquamarine") == 0) {
			return "#7FFFD4";
		}
		if (name.compareTo("Azure") == 0) {
			return "#F0FFFF";
		}
		if (name.compareTo("Beige") == 0) {
			return "#F5F5DC";
		}
		if (name.compareTo("Bisque") == 0) {
			return "#FFE4C4";
		}
		if (name.compareTo("Black") == 0) {
			return "#000000";
		}
		if (name.compareTo("BlanchedAlmond") == 0) {
			return "#FFEBCD";
		}
		if (name.compareTo("Blue") == 0) {
			return "#0000FF";
		}
		if (name.compareTo("BlueViolet") == 0) {
			return "#8A2BE2";
		}
		if (name.compareTo("Brown") == 0) {
			return "#A52A2A";
		}
		if (name.compareTo("BurlyWood") == 0) {
			return "#DEB887";
		}
		if (name.compareTo("CadetBlue") == 0) {
			return "#5F9EA0";
		}
		if (name.compareTo("Chartreuse") == 0) {
			return "#7FFF00";
		}
		if (name.compareTo("Chocolate") == 0) {
			return "#D2691E";
		}
		if (name.compareTo("Coral") == 0) {
			return "#FF7F50";
		}
		if (name.compareTo("CornflowerBlue") == 0) {
			return "#6495ED";
		}
		if (name.compareTo("Cornsilk") == 0) {
			return "#FFF8DC";
		}
		if (name.compareTo("Crimson") == 0) {
			return "#DC143C";
		}
		if (name.compareTo("Cyan") == 0) {
			return "#00FFFF";
		}
		if (name.compareTo("DarkBlue") == 0) {
			return "#00008B";
		}
		if (name.compareTo("DarkCyan") == 0) {
			return "#008B8B";
		}
		if (name.compareTo("DarkGoldenRod") == 0) {
			return "#B8860B";
		}
		if (name.compareTo("DarkGray") == 0) {
			return "#A9A9A9";
		}
		if (name.compareTo("DarkGreen") == 0) {
			return "#006400";
		}
		if (name.compareTo("DarkKhaki") == 0) {
			return "#BDB76B";
		}
		if (name.compareTo("DarkMagenta") == 0) {
			return "#8B008B";
		}
		if (name.compareTo("DarkOliveGreen") == 0) {
			return "#556B2F";
		}
		if (name.compareTo("Darkorange") == 0) {
			return "#FF8C00";
		}
		if (name.compareTo("DarkOrchid") == 0) {
			return "#9932CC";
		}
		if (name.compareTo("DarkRed") == 0) {
			return "#8B0000";
		}
		if (name.compareTo("DarkSalmon") == 0) {
			return "#E9967A";
		}
		if (name.compareTo("DarkSeaGreen") == 0) {
			return "#8FBC8F";
		}
		if (name.compareTo("DarkSlateBlue") == 0) {
			return "#483D8B";
		}
		if (name.compareTo("DarkSlateGray") == 0) {
			return "#2F4F4F";
		}
		if (name.compareTo("DarkTurquoise") == 0) {
			return "#00CED1";
		}
		if (name.compareTo("DarkViolet") == 0) {
			return "#9400D3";
		}
		if (name.compareTo("DeepPink") == 0) {
			return "#FF1493";
		}
		if (name.compareTo("DeepSkyBlue") == 0) {
			return "#00BFFF";
		}
		if (name.compareTo("DimGray") == 0) {
			return "#696969";
		}
		if (name.compareTo("DodgerBlue") == 0) {
			return "#1E90FF";
		}
		if (name.compareTo("Feldspar") == 0) {
			return "#D19275";
		}
		if (name.compareTo("FireBrick") == 0) {
			return "#B22222";
		}
		if (name.compareTo("FloralWhite") == 0) {
			return "#FFFAF0";
		}
		if (name.compareTo("ForestGreen") == 0) {
			return "#228B22";
		}
		if (name.compareTo("Fuchsia") == 0) {
			return "#FF00FF";
		}
		if (name.compareTo("Gainsboro") == 0) {
			return "#DCDCDC";
		}
		if (name.compareTo("GhostWhite") == 0) {
			return "#F8F8FF";
		}
		if (name.compareTo("Gold") == 0) {
			return "#FFD700";
		}
		if (name.compareTo("GoldenRod") == 0) {
			return "#DAA520";
		}
		if (name.compareTo("Gray") == 0) {
			return "#808080";
		}
		if (name.compareTo("Green") == 0) {
			return "#008000";
		}
		if (name.compareTo("GreenYellow") == 0) {
			return "#ADFF2F";
		}
		if (name.compareTo("HoneyDew") == 0) {
			return "#F0FFF0";
		}
		if (name.compareTo("HotPink") == 0) {
			return "#FF69B4";
		}
		if (name.compareTo("IndianRed") == 0) {
			return "#CD5C5C";
		}
		if (name.compareTo("Indigo") == 0) {
			return "#4B0082";
		}
		if (name.compareTo("Ivory") == 0) {
			return "#FFFFF0";
		}
		if (name.compareTo("Khaki") == 0) {
			return "#F0E68C";
		}
		if (name.compareTo("Lavender") == 0) {
			return "#E6E6FA";
		}
		if (name.compareTo("LavenderBlush") == 0) {
			return "#FFF0F5";
		}
		if (name.compareTo("LawnGreen") == 0) {
			return "#7CFC00";
		}
		if (name.compareTo("LemonChiffon") == 0) {
			return "#FFFACD";
		}
		if (name.compareTo("LightBlue") == 0) {
			return "#ADD8E6";
		}
		if (name.compareTo("LightCoral") == 0) {
			return "#F08080";
		}
		if (name.compareTo("LightCyan") == 0) {
			return "#E0FFFF";
		}
		if (name.compareTo("LightGoldenRodYellow") == 0) {
			return "#FAFAD2";
		}
		if (name.compareTo("LightGrey") == 0) {
			return "#D3D3D3";
		}
		if (name.compareTo("LightGreen") == 0) {
			return "#90EE90";
		}
		if (name.compareTo("LightPink") == 0) {
			return "#FFB6C1";
		}
		if (name.compareTo("LightSalmon") == 0) {
			return "#FFA07A";
		}
		if (name.compareTo("LightSeaGreen") == 0) {
			return "#20B2AA";
		}
		if (name.compareTo("LightSkyBlue") == 0) {
			return "#87CEFA";
		}
		if (name.compareTo("LightSlateBlue") == 0) {
			return "#8470FF";
		}
		if (name.compareTo("LightSlateGray") == 0) {
			return "#778899";
		}
		if (name.compareTo("LightSteelBlue") == 0) {
			return "#B0C4DE";
		}
		if (name.compareTo("LightYellow") == 0) {
			return "#FFFFE0";
		}
		if (name.compareTo("Lime") == 0) {
			return "#00FF00";
		}
		if (name.compareTo("LimeGreen") == 0) {
			return "#32CD32";
		}
		if (name.compareTo("Linen") == 0) {
			return "#FAF0E6";
		}
		if (name.compareTo("Magenta") == 0) {
			return "#FF00FF";
		}
		if (name.compareTo("Maroon") == 0) {
			return "#800000";
		}
		if (name.compareTo("MediumAquaMarine") == 0) {
			return "#66CDAA";
		}
		if (name.compareTo("MediumBlue") == 0) {
			return "#0000CD";
		}
		if (name.compareTo("MediumOrchid") == 0) {
			return "#BA55D3";
		}
		if (name.compareTo("MediumPurple") == 0) {
			return "#9370D8";
		}
		if (name.compareTo("MediumSeaGreen") == 0) {
			return "#3CB371";
		}
		if (name.compareTo("MediumSlateBlue") == 0) {
			return "#7B68EE";
		}
		if (name.compareTo("MediumSpringGreen") == 0) {
			return "#00FA9A";
		}
		if (name.compareTo("MediumTurquoise") == 0) {
			return "#48D1CC";
		}
		if (name.compareTo("MediumVioletRed") == 0) {
			return "#C71585";
		}
		if (name.compareTo("MidnightBlue") == 0) {
			return "#191970";
		}
		if (name.compareTo("MintCream") == 0) {
			return "#F5FFFA";
		}
		if (name.compareTo("MistyRose") == 0) {
			return "#FFE4E1";
		}
		if (name.compareTo("Moccasin") == 0) {
			return "#FFE4B5";
		}
		if (name.compareTo("NavajoWhite") == 0) {
			return "#FFDEAD";
		}
		if (name.compareTo("Navy") == 0) {
			return "#000080";
		}
		if (name.compareTo("OldLace") == 0) {
			return "#FDF5E6";
		}
		if (name.compareTo("Olive") == 0) {
			return "#808000";
		}
		if (name.compareTo("OliveDrab") == 0) {
			return "#6B8E23";
		}
		if (name.compareTo("Orange") == 0) {
			return "#FFA500";
		}
		if (name.compareTo("OrangeRed") == 0) {
			return "#FF4500";
		}
		if (name.compareTo("Orchid") == 0) {
			return "#DA70D6";
		}
		if (name.compareTo("PaleGoldenRod") == 0) {
			return "#EEE8AA";
		}
		if (name.compareTo("PaleGreen") == 0) {
			return "#98FB98";
		}
		if (name.compareTo("PaleTurquoise") == 0) {
			return "#AFEEEE";
		}
		if (name.compareTo("PaleVioletRed") == 0) {
			return "#D87093";
		}
		if (name.compareTo("PapayaWhip") == 0) {
			return "#FFEFD5";
		}
		if (name.compareTo("PeachPuff") == 0) {
			return "#FFDAB9";
		}
		if (name.compareTo("Peru") == 0) {
			return "#CD853F";
		}
		if (name.compareTo("Pink") == 0) {
			return "#FFC0CB";
		}
		if (name.compareTo("Plum") == 0) {
			return "#DDA0DD";
		}
		if (name.compareTo("PowderBlue") == 0) {
			return "#B0E0E6";
		}
		if (name.compareTo("Purple") == 0) {
			return "#800080";
		}
		if (name.compareTo("Red") == 0) {
			return "#FF0000";
		}
		if (name.compareTo("RosyBrown") == 0) {
			return "#BC8F8F";
		}
		if (name.compareTo("RoyalBlue") == 0) {
			return "#4169E1";
		}
		if (name.compareTo("SaddleBrown") == 0) {
			return "#8B4513";
		}
		if (name.compareTo("Salmon") == 0) {
			return "#FA8072";
		}
		if (name.compareTo("SandyBrown") == 0) {
			return "#F4A460";
		}
		if (name.compareTo("SeaGreen") == 0) {
			return "#2E8B57";
		}
		if (name.compareTo("SeaShell") == 0) {
			return "#FFF5EE";
		}
		if (name.compareTo("Sienna") == 0) {
			return "#A0522D";
		}
		if (name.compareTo("Silver") == 0) {
			return "#C0C0C0";
		}
		if (name.compareTo("SkyBlue") == 0) {
			return "#87CEEB";
		}
		if (name.compareTo("SlateBlue") == 0) {
			return "#6A5ACD";
		}
		if (name.compareTo("SlateGray") == 0) {
			return "#708090";
		}
		if (name.compareTo("Snow") == 0) {
			return "#FFFAFA";
		}
		if (name.compareTo("SpringGreen") == 0) {
			return "#00FF7F";
		}
		if (name.compareTo("SteelBlue") == 0) {
			return "#4682B4";
		}
		if (name.compareTo("Tan") == 0) {
			return "#D2B48C";
		}
		if (name.compareTo("Teal") == 0) {
			return "#008080";
		}
		if (name.compareTo("Thistle") == 0) {
			return "#D8BFD8";
		}
		if (name.compareTo("Tomato") == 0) {
			return "#FF6347";
		}
		if (name.compareTo("Turquoise") == 0) {
			return "#40E0D0";
		}
		if (name.compareTo("Violet") == 0) {
			return "#EE82EE";
		}
		if (name.compareTo("VioletRed") == 0) {
			return "#D02090";
		}
		if (name.compareTo("Wheat") == 0) {
			return "#F5DEB3";
		}
		if (name.compareTo("White") == 0) {
			return "#FFFFFF";
		}
		if (name.compareTo("WhiteSmoke") == 0) {
			return "#F5F5F5";
		}
		if (name.compareTo("Yellow") == 0) {
			return "#FFFF00";
		}
		if (name.compareTo("YellowGreen") == 0) {
			return "#9ACD32";
		}
		return "ERROR: no such color: " + name;
	}

}