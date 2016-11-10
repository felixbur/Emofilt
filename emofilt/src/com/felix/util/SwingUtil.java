package com.felix.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Helper class around swing code.
 * 
 * @author felix
 * 
 */
public class SwingUtil {
	/**
	 * Retrieve the value for a specific key for a Point (constructed from 2
	 * integers). E.g. point 13 2341 constructs new Point(13, 2341)
	 * 
	 * @param key
	 *            The key.
	 * @return The value a a Point.
	 */
	public static Point getPoint(KeyValues keyValues, String key) {
		String val = keyValues.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		int x = Integer.parseInt(val.trim().split(" ")[0]);
		int y = Integer.parseInt(val.trim().split(" ")[1]);
		return new Point(x, y);
	}
		/**
	 * Retrieve the value for a specific key for a Dimension (constructed from 2
	 * integers). E.g. point 13 2341 constructs new Dimension(13, 2341)
	 * 
	 * @param key
	 *            The key.
	 * @return The value a a Dimension.
	 **/
	public static Dimension getDimension(KeyValues keyValues, String key) {
		String val = keyValues.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		int x = Integer.parseInt(val.trim().split(" ")[0]);
		int y = Integer.parseInt(val.trim().split(" ")[1]);
		return new Dimension(x, y);
	}
	
	/**
	 * Return a button with action command set to name.
	 * 
	 * @param name
	 * @param l
	 * @return
	 */
	public final static JButton makeButton(String name, ActionListener l) {
		JButton button = new JButton(name);
		button.addActionListener(l);
		button.setActionCommand(name);
		return button;
	}
public final static JPanel makeButtonPanel(Vector<String> names, ActionListener actionListener) {
	JPanel pane = new JPanel();
	for(String s : names) {
		JButton b = makeButton(s, actionListener);
		pane.add(b);
	}
	return pane;
}
	/**
	 * Return a button with action command set to name.
	 * 
	 * @param name
	 * @param l
	 * @return
	 */
	public final static JButton makeWhiteOnBlackButton(String name, ActionListener l) {
		JButton button = new JButton(name);
		button.addActionListener(l);
		button.setActionCommand(name);
		button.setForeground(Color.white);
		button.setBackground(Color.black);
		return button;
	}

	public final static ImagePanel getImagePanel(String img) {
		ImagePanel ret = new ImagePanel(img);
		return ret;
	}

	public final static ImagePanel getImagePanel(Image[] images) {
		ImagePanel ret = new ImagePanel(images);
		return ret;
	}

	/**
	 * Helper class for panels with background image.
	 * 
	 * @author felix
	 * 
	 */
	public static class ImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private Image _img;
		private Image[] _images;
		private Image _additionalImg;
		private Point _additionalImageLocation;

		/**
		 * Constructor with background image path.
		 * 
		 * @param _img
		 */
		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		/**
		 * Constructor with a array of images to be kept in memory and switched.
		 * 
		 * @param images
		 */
		public ImagePanel(Image[] images) {
			_images = images;
			this._img = _images[0];
			Dimension size = new Dimension(_img.getWidth(null), _img
					.getHeight(null));
			setMySize(size);
			repaint();
		}

		/**
		 * Switch bg image.
		 * 
		 * @param _img
		 */
		public void switchImage(String img) {
			switchImage(new ImageIcon(img).getImage());
		}

		/**
		 * Switch bg image.
		 * 
		 * @param _img
		 */
		public void switchImage(int index) {
			this._img = _images[index];
			Dimension size = new Dimension(_img.getWidth(null), _img
					.getHeight(null));
			setMySize(size);
			update(getGraphics());
		}

		/**
		 * Switch bg image.
		 * 
		 * @param _img
		 */
		public void switchImage(Image img) {
			this._img = img;
			Dimension size = new Dimension(_img.getWidth(null), _img
					.getHeight(null));
			setMySize(size);
			update(getGraphics());
		}

		/**
		 * Constructor with background image.
		 * 
		 * @param _img
		 */
		public ImagePanel(Image img) {
			this._img = img;
			Dimension size = new Dimension(img.getWidth(null), img
					.getHeight(null));
			setMySize(size);
			repaint();
		}

		public void setAdditionalImage(Image img, Point loc) {
			_additionalImg = img;
			_additionalImageLocation = loc;

		}

		public void paintComponent(Graphics g) {
			g.drawImage(_img, 0, 0, null);
			if (_additionalImg != null) {
				g.drawImage(_additionalImg, _additionalImageLocation.x,
						_additionalImageLocation.y, null);
			}
		}

		private void setMySize(Dimension size) {
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
		}

	}

	public static InvisibleButton getInvisibleButton(int width, int height) {
		return new InvisibleButton(width, height);
	}

	/**
	 * A button without borders, icons or text.
	 * 
	 * @author felix
	 * 
	 */
	public static class InvisibleButton extends JButton {
		/**
		 */
		private static final long serialVersionUID = 1L;
		int _width, _height;

		public InvisibleButton(int width, int height) {
			_width = width;
			_height = height;
		}

		public boolean isBorderPainted() {
			return false;
		}

		public String getText() {
			return "";
		}

		public boolean isContentAreaFilled() {
			return false;
		}

		public Dimension getPreferredSize() {
			return new Dimension(_width, _height);
		}
	}
}