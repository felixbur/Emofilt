package emofilt.storytagger;

import java.awt.Color;

import com.felix.util.ColorUtil;

import emofilt.Constants;

public class TaggedPiece {
	private int start = 0, end = 0;
	private String tag="";
	private String emotion="";
	private String voice="";
	private String intensity="";
	private Color color=null;
	private String text="";

	public TaggedPiece() {
	}

	public TaggedPiece(int start, int end) {
		this.start = start;
		this.end = end;
	}
	public TaggedPiece(String emotionName, String text) {
		this.emotion = emotionName;
		this.text = text;
	}

	public boolean isNeutral() {
		if (emotion.compareTo(Constants.NEUTRAL_EMOTION) == 0)
			return true;
		return false;
	}

	public void addText(String text) {
		this.text += text;
		end += text.length();
	}

	public void addLength(int len) {
		start += len;
		end += len;
	}

	public int getLength() {
		return end - start;
	}

	public String toString() {
		return "start: " + start + ", end: " + end + ", text: " + text;
	}

	public void setNeutral() {
		emotion = Constants.NEUTRAL_EMOTION;
		color = Constants.NEUTRAL_COLOR;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getColorFontTag() {
		return "font color=\"" + ColorUtil.colorToHex(color) + "\"";
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
