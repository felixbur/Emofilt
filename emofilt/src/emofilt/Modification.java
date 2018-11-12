/*
 * Created on 17.11.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

/**
 *  
 * A modification-object models a change you can do with an
 * utterance. Four kinds of modifications get distinguished: pitch, duration,
 * articulation and phonation changes.
 * 
 * 
 * @author Felix Burkhardt
 */
public class Modification {
	/** A modification of pitch */
	public final static int PITCH = 0;

	/** A modification of duration */
	public final static int DURATION = 1;

	/** A modification of articulation */
	public final static int ARTICULATION = 2;

	/** A modification of phonation */
	public final static int PHONATION = 3;

	private String name;

	private boolean effortType;

	private boolean contourType;

	private boolean vowelTargetType;

	private boolean twoRates;

	private int defaultContour;

	private int defaultRate;

	private int defaultRate2;

	private String defaultEffort;

	private String defaultVowelTarget;

	private int type;


	/**
	 * Initializes the modification with all parameters.
	 * 
	 * @param name
	 *            The name (must be the same name in emotion-description and
	 *            Gui-labels).
	 * @param contourType
	 *            True if change has to set a contour-type.
	 * @param effortType
	 *            True if change has to set an effort-type.
	 * @param defaultRate
	 *            The default rate (that doesn't change the utterance).
	 * @param defaultContour
	 *            The default contour (that doesn't change the utterance).
	 * @param defaultEffort
	 *            The default effort (that doesn't change the utterance).
	 * @param type
	 *            The type of change (pitch, duration, articulation or
	 *            phonation).
	 */
	public Modification(String name, boolean contourType, boolean effortType,
			int defaultRate, int defaultContour, String defaultEffort, int type) {
		super();
		this.name = name;
		this.effortType = effortType;
		this.contourType = contourType;
		this.defaultRate = defaultRate;
		this.defaultContour = defaultContour;
		this.type = type;
		this.defaultEffort = defaultEffort;
	}

	/**
	 * Initializes the modification with all parameters.
	 * 
	 * @param name
	 *            The name (must be the same name in emotion-description and
	 *            Gui-labels).
	 * @param defaultRate
	 *            The default rate (that doesn't change the utterance).
	 * @param type
	 *            The type of change (pitch, duration, articulation or
	 *            phonation).
	 */
	public Modification(String name, int defaultRate, int type) {
		super();
		this.name = name;
		this.effortType = false;
		this.contourType = false;
		this.vowelTargetType = false;
		this.defaultRate = defaultRate;
		this.type = type;
	}

	/**
	 * Initializes the modification with all parameters. This constructor is for
	 * changes of vowel-precision targets (overshoot or undershoot).
	 * 
	 * @param name
	 *            The name (must be the same name in emotion-description and
	 *            Gui-labels).
	 * @param defaultTarget
	 *            The default target (that doesn't change the utterance).
	 * @param type
	 *            The type of change (pitch, duration, articulation or
	 *            phonation).
	 */
	public Modification(String name, String defaultTarget, int type) {
		super();
		this.vowelTargetType = true;
		this.name = name;
		this.effortType = false;
		this.contourType = false;
		this.defaultVowelTarget = defaultTarget;
		this.type = type;
	}

	/**
	 * Initializes the modification with all parameters for changes with two
	 * rate-parameters (like wave-model change).
	 * 
	 * @param name
	 *            The name (must be the same name in emotion-description and
	 *            Gui-labels).
	 * @param defaultRate
	 *            The default rate (that doesn't change the utterance).
	 * @param defaultRate2
	 *            The default rate (that doesn't change the utterance).
	 * @param type
	 *            The type of change (pitch, duration, articulation or
	 *            phonation).
	 */
	public Modification(String name, int defaultRate, int defaultRate2, int type) {
		super();
		this.name = name;
		this.effortType = false;
		this.contourType = false;
		this.twoRates = true;
		this.defaultRate = defaultRate;
		this.defaultRate2 = defaultRate2;
		this.type = type;
	}

	/**
	 * Test if a is the default value.
	 * 
	 * @param dr
	 *            The String to test if default.
	 * @return True if dr corresponds to default and false otherwise.
	 */
	public boolean isDefault(String dr) {
		if (isVowelTargetType()) {
			if (defaultVowelTarget.compareTo(dr) == 0)
				return true;
		} else if (isEffortType()) {
			if (defaultEffort.compareTo(dr) == 0)
				return true;
		} else {
			String r = String.valueOf(defaultRate);
			if (r.compareTo(dr) == 0)
				return true;
		}
		return false;
	}

	/**
	 * Test if two Strings correspond to default rates 1 and 2.
	 * 
	 * @param dr1
	 *            String to test against first default-rate.
	 * @param dr2
	 *            String to test against second default-rate.
	 * @return True if both Strings are the same as the corresponding rates.
	 */
	public boolean isDefault(String dr1, String dr2) {
		String r1 = String.valueOf(defaultRate);
		String r2 = String.valueOf(defaultRate2);
		if (r1.compareTo(dr1) == 0 && r2.compareTo(dr2) == 0)
			return true;
		return false;
	}

	/**
	 * Test if an integer is the default value.
	 * 
	 * @param dr
	 *            The int to compare with default value..
	 * @return True if dr is default value, false otherwise.
	 */
	public boolean isDefault(int dr) {
		if (defaultRate == dr)
			return true;
		return false;
	}

	/**
	 * Test if two integers are the default values.
	 * 
	 * @param dr
	 *            The int to test against first rate.
	 * @param dr2
	 *            The int to test against second rate.
	 * @return True if dr and dr2 are default value, false otherwise.
	 */
	public boolean isDefault(int dr, int dr2) {
		if (defaultRate == dr && defaultRate2 == dr2)
			return true;
		return false;
	}

	/**
	 * Test if modification changes the vocal effort.
	 * 
	 * @return True if the modification has an effort-type, false otherwise.
	 */
	public boolean isEffortType() {
		return effortType;
	}

	/**
	 * Return the default effort-type (normal).
	 * 
	 * @return The default effort-type (normal).
	 */
	public String getDefaultEffort() {
		return defaultEffort;
	}

	/**
	 * Return the default vowel target (normal).
	 * 
	 * @return The default vowel target (normal).
	 */
	public String getDefaultVowelTarget() {
		return defaultVowelTarget;
	}

	/**
	 * Test if modification is not of effort or contour-type.
	 * 
	 * @return True if modification is not of effort or contour-type, false
	 *         otherwise.
	 */
	public boolean isRateOnly() {
		if (!isEffortType() && !isContourType()) {
			return true;
		}
		return false;
	}

	/**
	 * Get the default rate of a change for this modification.
	 * 
	 * @return The default rate.
	 */
	public int getDefaultRate() {
		return defaultRate;
	}

	/**
	 * test whether this modification has a contour type.
	 * 
	 * @return True of a contour type is present, false otherwise.
	 */
	public boolean isContourType() {
		return contourType;
	}

	/**
	 * Test whether a modification has a vowel target (overshoot or undershoot).
	 * 
	 * @return True if the modification has a vowel target, false otherwise.
	 */
	public boolean isVowelTargetType() {
		return vowelTargetType;
	}

	/**
	 * Return the default contour type as an integer value.
	 * 
	 * @return the default contour.
	 */
	public int getDefaultContour() {
		return defaultContour;
	}

	/**
	 * Get the name of this modification.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the type of modification as an intager index.
	 * 
	 * @return The type as an integer.
	 * 
	 * @see Modification
	 */
	public int getType() {
		return type;
	}

	/**
	 * Retrieve the name of the type of modification as a String (e.g. "pitch"
	 * or "duration").
	 * 
	 * @return The type of modification as a String.
	 */
	public String getTypeString() {
		return getTypeAsString(type);
	}

	private String getTypeAsString(int typei) {
		switch (typei) {
		case PITCH:
			return "pitch";
		case DURATION:
			return "duration";
		case ARTICULATION:
			return "articulation";
		case PHONATION:
			return "phonation";
		}
		return "ERROR, type not defined";
	}

	/**
	 * Retrieve the second default rate.
	 * 
	 * @return The second default rate.
	 */
	public int getDefaultRate2() {
		return defaultRate2;
	}

	/**
	 * Test if this modification has two rates.
	 * @return True if it has two rates, false otherwise.
	 */
	public boolean isTwoRates() {
		return twoRates;
	}

}