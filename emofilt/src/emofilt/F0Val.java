/*
 * Created on 07.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

/**
 * 
 * A F0-value models a mbrola dupel consisting of a timevalue (denoting the
 * percentage of duration) and the f0-value in Hertz.
 * 
 * @author Felix Burkhardt
 */
public class F0Val {
	private int pos;

	private int val;

	public F0Val() {
		pos = 0;
		val = 0;
	}

	/**
	 * Constructor, given the two values.
	 * 
	 * @param pos
	 *            The position (percent of duration)
	 * @param val
	 *            The F0-value.
	 */
	public F0Val(int pos, int val) {
		this.pos = pos;
		this.val = val;
	}

	/**
	 * Get the position.
	 * 
	 * @return The position as a relative percentage of duration.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Set the position.
	 * 
	 * @param pos The position as a relative percentage of duration.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * Get the value.
	 * 
	 * @return The value in Hertz.
	 */
	public int getVal() {
		return val;
	}

	/**
	 * set the F0 value in Hertz.
	 * 
	 * @param val F0 value in Hertz.
	 */
	public void setVal(int val) {
		this.val = val;
	}
}