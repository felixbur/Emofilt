package emofilt.storyreading;

public class Phrase {
	private String emotion;
	private int intensity;
	private String text;
	private String voice;
	
	public Phrase(String emotion, int intensity, String text, String voice) {
		super();
		this.emotion = emotion;
		this.intensity = intensity;
		this.text = text;
		this.voice = voice;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public int getIntensity() {
		return intensity;
	}
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	

}
