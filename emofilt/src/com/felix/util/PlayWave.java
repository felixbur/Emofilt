package com.felix.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Helper class to play an audio file.
 * 
 * @author felix
 * 
 */
public class PlayWave extends Thread {

	public static final String AUDIOFORMAT_WAV = "wav";
	public static final String AUDIOFORMAT_PCM_22050 = "pcm22050";
	private boolean _playing = false;

	public static enum Position {
		LEFT, RIGHT, NORMAL
	};

	private String audioFormat = "wav";
	private String filename;

	private Position curPosition;

	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	/**
	 * Stop the current playback.
	 * 
	 */
	public void stopPlayback() {
		_playing = false;
	}

	/**
	 * Empty connstructor to play from bytestream.
	 */
	public PlayWave() {
	}

	/**
	 * Constructor for a wav-headed file.
	 * 
	 * @param wavfile
	 */
	public PlayWave(String wavfile) {
		filename = wavfile;
		curPosition = Position.NORMAL;
	}

	/**
	 * Constructor for wav headed file and panorama position.
	 * 
	 * @param wavfile
	 * @param p
	 */
	public PlayWave(String wavfile, Position p) {
		filename = wavfile;
		curPosition = p;
	}

	/**
	 * Set format for headerless files.
	 * 
	 * @param afm
	 */
	public void setAudioformat(String afm) {
		this.audioFormat = afm;
	}

	public void run() {

		File soundFile = new File(filename);
		if (!soundFile.exists()) {
			System.err.println("Wave file not found: " + filename);
			return;
		}

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		AudioFormat format = null;
		if (audioFormat.compareTo(AUDIOFORMAT_WAV) == 0) {
			format = audioInputStream.getFormat();
		} else if (audioFormat.compareTo(AUDIOFORMAT_PCM_22050) == 0) {
			format = AudioUtil.FORMAT_PCM_22KHZ;
		} else {
			System.err.println("undefined autio format: " + audioFormat);
			return;
		}
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (auline.isControlSupported(FloatControl.Type.PAN)) {
			FloatControl pan = (FloatControl) auline
					.getControl(FloatControl.Type.PAN);
			if (curPosition == Position.RIGHT)
				pan.setValue(1.0f);
			else if (curPosition == Position.LEFT)
				pan.setValue(-1.0f);
		}
		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
		_playing = true;
		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
				if (! _playing)
					break;
			}
			_playing = false;
			audioInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
			auline = null;
			audioInputStream = null;
			soundFile = null;

		}

	}

	/**
	 * Play from a byte array output stream.
	 * 
	 * @param out
	 * @param format
	 * @throws Exception
	 */
	public void playAudioFromByteStrean(ByteArrayOutputStream out,
			AudioFormat format) throws Exception {
		byte audio[] = out.toByteArray();
		playAudioFromByteArray(audio, format);
	}
	/**
	 * Play from a byte array.
	 * 
	 * @param audio The byte array.
	 * @param format
	 * @throws Exception
	 */
	public void playAudioFromByteArray(byte[] audio,
			AudioFormat format) throws Exception {
		final AudioFormat _format = format;
		InputStream input = new ByteArrayInputStream(audio);
		final AudioInputStream ais = new AudioInputStream(input, format,
				audio.length / format.getFrameSize());
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		Runnable runner = new Runnable() {
			int bufferSize = (int) _format.getSampleRate()
					* _format.getFrameSize();
			byte buffer[] = new byte[bufferSize];

			public void run() {
				try {
					int count;
					_playing = true;
					while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
						if (count > 0) {
							line.write(buffer, 0, count);
						}
						if (! _playing)
							break;
					}
					_playing = false;
					line.drain();
					line.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Thread playThread = new Thread(runner);
		playThread.start();
	}
}