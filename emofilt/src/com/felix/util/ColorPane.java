package com.felix.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.plaf.TextUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * A jtextpane that allows for setting the text color in parts, retrieving the
 * color and overtyping mode,
 * 
 * @author felix
 * 
 */
public class ColorPane extends JTextPane {
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static boolean isOvertypeMode;

	private Caret defaultCaret;
	private Caret overtypeCaret;

	public ColorPane() {
		super();
		defaultCaret = getCaret();
		overtypeCaret = new OvertypeCaret();
		overtypeCaret.setBlinkRate(defaultCaret.getBlinkRate());
		setOvertypeMode(false);
		setForeground(Color.black, 0, 0);
	}

	/**
	 * Append text in some color at end.
	 * 
	 * @param c
	 *            The color.
	 * @param s
	 *            The text.
	 */
	public void append(Color c, String s) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);

		int len = getDocument().getLength();
		setCaretPosition(len);
		setCharacterAttributes(aset, false);
		replaceSelection(s);
	}

	public void selectLineAroundCursor() {
		int pos = getCaretPosition();
		String text = getText();
		int lineStart = text.substring(0, pos).lastIndexOf('\n')+1;
		if (lineStart<0) lineStart=0;
		int lineEnd = text.substring(pos).indexOf('\n')+pos;
		if (lineEnd < lineStart)
			lineEnd = text.length();
		select(lineStart, lineEnd);
	}

	/**
	 * Append a newline and set the foreground color to black.
	 */
	public void appendNewLine() {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, Color.black);
		int len = getDocument().getLength();
		setCaretPosition(len);
		setCharacterAttributes(aset, false);
		replaceSelection("\n");
	}

	/**
	 * Set the foreground color oof the selected text.
	 * 
	 * @param col
	 *            The color.
	 */
	public void setForegroundForSelection(Color col) {
		int start = getSelectionStart();
		int end = getSelectionEnd();
		setForeground(col, start, end);
	}

	/**
	 * Set the foreground text color.
	 * 
	 * @param col
	 *            The color.
	 * @param start
	 *            The start index.
	 * @param end
	 *            The end index.
	 */
	public void setForeground(Color col, int start, int end) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, col);
		select(start, end);
		setCharacterAttributes(aset, true);
	}

	/**
	 * Get the color for a part of text.
	 * 
	 * @param start
	 *            The start index.
	 * @param end
	 *            The end index.
	 * @return The color.
	 */
	public Color getColor(int start, int end) {
		select(start, end);
		AttributeSet aset = getCharacterAttributes();
		Color c = (Color) aset.getAttribute(StyleConstants.Foreground);
		return c;
	}

	/**
	 * Get a string where all color changes are tagged.
	 * 
	 * @return The string.
	 */
	public String getColorTaggedText1() {
		String text = getText();
		Color oldColor = getColor(0, 0);
		String ret = "<color val=\"" + ColorUtil.colorToHex(oldColor) + "\">";
		int newlineCounter = 0, count = 0;
		boolean startCountdown = false;
		for (int i = 0; i < text.length(); i++) {
			Color act = getColor(i, i);
			if (act != oldColor) {
				oldColor = act;
				startCountdown = true;
				count = newlineCounter;
			}
			if (startCountdown) {
				if (count == 0) {
					ret += "</color><color val=\""
							+ ColorUtil.colorToHex(oldColor) + "\">";
					startCountdown = false;
				} else {
					count--;
				}
			}
			char actC = text.charAt(i);
			if (actC == '\n') {
				newlineCounter++;
			}
			ret += actC;
			// ret += text.substring(i, i + 1);
		}
		return ret += "</color>";
	}

	public String getColorTaggedText() {
		String text = getText();
		Color oldColor = getColor(0, 0);
		String ret = "<color val=\"" + ColorUtil.colorToHex(oldColor) + "\">";
		int charCounter = 0;
		for (int i = 0; i < text.length(); i++) {
			Color act = getColor(i, i);
			if (act != oldColor) {
				oldColor = act;
				if (charCounter > 0) {
					ret += "</color>";
				} else {
					ret = StringUtil.stripLastTag(ret);
				}
				ret += "<color val=\"" + ColorUtil.colorToHex(oldColor) + "\">";
				charCounter = 0;
			}
			char actC = text.charAt(i);
			ret += actC;
			if (actC != '\n') {
				charCounter++;
			}
		}
		if (charCounter > 0) {
			ret += "</color>";
		} else {
			ret = StringUtil.stripLastTag(ret);
		}
		return ret;
	}

	/*
	 * Return the overtype/insert mode
	 */
	public boolean isOvertypeMode() {
		return isOvertypeMode;
	}

	/*
	 * Set the caret to use depending on overtype/insert mode
	 */
	public void setOvertypeMode(boolean isOvertypeMode) {
		this.isOvertypeMode = isOvertypeMode;
		int pos = getCaretPosition();

		if (isOvertypeMode()) {
			setCaret(overtypeCaret);
		} else {
			setCaret(defaultCaret);
		}

		setCaretPosition(pos);
	}

	/*
	 * Override method from JComponent
	 */
	public void replaceSelection(String text) {
		// Implement overtype mode by selecting the character at the current
		// caret position

		if (isOvertypeMode()) {
			int pos = getCaretPosition();

			if (getSelectedText() == null && pos < getDocument().getLength()) {
				moveCaretPosition(pos + 1);
			}
		}

		super.replaceSelection(text);
	}

	/*
	 * Override method from JComponent
	 */
	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);

		// Handle release of Insert key to toggle overtype/insert mode

		if (e.getID() == KeyEvent.KEY_RELEASED
				&& e.getKeyCode() == KeyEvent.VK_INSERT) {
			setOvertypeMode(!isOvertypeMode());
		}
	}

	/*
	 * Paint a horizontal line the width of a column and 1 pixel high
	 */
	class OvertypeCaret extends DefaultCaret {
		/*
		 * The overtype caret will simply be a horizontal line one pixel high
		 * (once we determine where to paint it)
		 */
		public void paint(Graphics g) {
			if (isVisible()) {
				try {
					JTextComponent component = getComponent();
					TextUI mapper = component.getUI();
					Rectangle r = mapper.modelToView(component, getDot());
					g.setColor(component.getCaretColor());
					int width = g.getFontMetrics().charWidth('w');
					int y = r.y + r.height - 2;
					g.drawLine(r.x, y, r.x + width - 2, y);
				} catch (BadLocationException e) {
				}
			}
		}

		/*
		 * Damage must be overridden whenever the paint method is overridden
		 * (The damaged area is the area the caret is painted in. We must
		 * consider the area for the default caret and this caret)
		 */
		protected synchronized void damage(Rectangle r) {
			if (r != null) {
				JTextComponent component = getComponent();
				x = r.x;
				y = r.y;
				width = component.getFontMetrics(component.getFont())
						.charWidth('w');
				height = r.height;
				repaint();
			}
		}
	}
}
