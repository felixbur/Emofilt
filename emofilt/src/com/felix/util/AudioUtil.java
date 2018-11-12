package com.felix.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Some methods concerning audio file handling.
 * 
 * @author felix
 * 
 */
public class AudioUtil {
	/**
	 * Calculate a new f0-value in Hertz given a start f0 value, a time span and
	 * a gradient.
	 * 
	 * @param freq
	 *            The start value in Hertz.
	 * @param time
	 *            The time span.
	 * @param gradient
	 *            The gradient, interpreted as semi-tones per second (ST/S)
	 * @return The new F0-value.
	 */
	public static double gradient2Hz(int freq, int time, double gradient) {
		final double _12_LN2 = 17.31234;
		double _exp;
		double semiTone = (time * 0.001) * gradient;
		_exp = semiTone / _12_LN2;
		return Math.exp(_exp) * freq;
	}

	/**
	 * Audio format (8000 Hz, 16 bit, mono, little endian, pcm coding).
	 */
	public final static AudioFormat FORMAT_PCM_8KHZ = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, 8000,
			// Samplerate
			16, // quantization
			1, // mono
			2, 8000, false // byteorder: little endian
	);

	/**
	 * Audio format (8000 Hz, 8 bit alaw, mono, little endian).
	 */
	public final static AudioFormat FORMAT_ALAW = new AudioFormat(
			AudioFormat.Encoding.ALAW, 8000,
			// Samplerate
			8, // quantization
			1, // mono
			1, 8000, false // byteorder: little endian
	);
	/**
	 * Audio format (16000 Hz, 16 bit, mono, little endian, pcm coding).
	 */
	public final static AudioFormat FORMAT_PCM_16KHZ = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, 16000,
			// Samplerate
			16, // quantization
			1, // mono
			2, 16000, false // byteorder: little endian
	);
	/**
	 * Audio format (22000 Hz, 16 bit, mono, little endian, pcm coding).
	 */
	public final static AudioFormat FORMAT_PCM_22KHZ = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, 22050,
			// Samplerate
			16, // quantization
			1, // mono
			2, 22050, false // byteorder: little endian
	);
	   /**
     * Function converts a short array to a byte array
     * 
     * @param shortData
     *            The short array.
     * @return The byte array.
     */
    public static byte[] shortToByte(short[] shortData, boolean littleEndian) {
        byte[] data = new byte[shortData.length * 2];
        int size = shortData.length;
        if (littleEndian) {
            for (int i = 0; i < size; i++) {
                data[i * 2] = (byte) shortData[i];
                data[i * 2 + 1] = (byte) (shortData[i] >> 8);
            }
        } else {
            // big Endian
            for (int i = 0; i < size; i++) {
                data[i * 2] = (byte) (shortData[i] >> 8);
                data[i * 2 + 1] = (byte) shortData[i];
            }
        }
        return data;
    }

	
	/**
	 * Function converts a byte array to a short array.
	 * 
	 * @param byteData
	 *            The byte array.
	 * @param writeLittleEndian
	 *            If true data is handled as little endian, else as big endian
	 * @return The short array.
	 */
	public static short[] byteToShort(byte[] byteData, boolean writeLittleEndian) {
		short[] data = new short[byteData.length / 2];
		int size = data.length;
		byte lb, hb;
		if (writeLittleEndian) {
			for (int i = 0; i < size; i++) {
				lb = byteData[i * 2];
				hb = byteData[i * 2 + 1];
				data[i] = (short) (((short) hb << 8) | lb & 0xff);
			}
		} else {
			for (int i = 0; i < size; i++) {
				lb = byteData[i * 2];
				hb = byteData[i * 2 + 1];
				data[i] = (short) (((short) lb << 8) | hb & 0xff);
			}

		}
		return data;
	}

	/**
	 * Dump the first 1024 byte of an byte-array. Approximation to remove a
	 * Riff-wave header.
	 * 
	 * @param data
	 * @return data - first 1024 byte or empty array if was shorter than 1024
	 *         byte.
	 */
	public static byte[] dumpFirst1024Byte(byte[] data) {
		byte[] ret = new byte[0];
		if (data.length >= 1024) {
			ret = new byte[data.length - 1024];
			System.arraycopy(data, 1024, ret, 0, data.length - 1024);
		}
		return ret;
	}

	/**
	 * Convert a bytearray of sound data from (mono, 8bit a-law, 8kHz, little
	 * Endian) to (mono, 16 bit PCM, 8kHz, little Endian)
	 * 
	 * @param data
	 *            source-array
	 * @return destination array
	 * @throws Exception
	 */
	public static byte[] convertFrom8bitALawTo16bitPCM(byte[] data)
			throws Exception {
		byte[] ret = null;
		AudioFormat sourceformat = new AudioFormat(AudioFormat.Encoding.ALAW,
				8000,
				// Samplerate
				8, // quantization
				1, // mono
				2, 8000, false // byteorder: little endian
		);
		AudioFormat targetformat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, 8000,
				// Samplerate
				16, // quantization
				1, // mono
				2, 8000, false // byteorder: little endian
		);

		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(
				data), sourceformat, data.length);
		ais = AudioSystem.getAudioInputStream(targetformat, ais);
		ret = getBytesFromInputStream(ais);
		// AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new
		// File("D:\\Work\\SymEmoTester\\RecServer\\recordings\\testsample.raw"));
		return ret;
	}

	/**
	 * Convert a bytearray of sound data from (mono, 8bit mu-law, 8kHz, little
	 * Endian) to (mono, 16 bit PCM, 8kHz, little Endian)
	 * 
	 * @param data
	 *            source-array
	 * @return destination array
	 * @throws Exception
	 */
	public static byte[] convertFrom8bitMuLawTo16bitPCM(byte[] data)
			throws Exception {
		byte[] ret = null;
		AudioFormat sourceformat = new AudioFormat(AudioFormat.Encoding.ULAW,
				8000,
				// Samplerate
				8, // quantization
				1, // mono
				2, 8000, false // byteorder: little endian
		);
		AudioFormat targetformat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, 8000,
				// Samplerate
				16, // quantization
				1, // mono
				2, 8000, false // byteorder: little endian
		);

		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(
				data), sourceformat, data.length);
		ais = AudioSystem.getAudioInputStream(targetformat, ais);
		ret = getBytesFromInputStream(ais);
		// AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new
		// File("D:\\Work\\SymEmoTester\\RecServer\\recordings\\testsample.raw"));
		return ret;
	}

	/**
	 * Write a byte array with a wav header to a file.
	 * 
	 * @param data
	 *            The byte array.
	 * @param format
	 *            The audioformat.
	 * @param fn
	 *            The filename.
	 * @throws Exception
	 */
	public static void writeAudioToWavFile(byte[] data, AudioFormat format,
			String fn) throws Exception {
		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(
				data), format, data.length);
		AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(fn));
	}

	/***************************************************************************
	 * Get bytes array from InputStream.
	 * 
	 */
	public static byte[] getBytesFromInputStream(InputStream is)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (true) {
			byte[] buffer = new byte[100];
			int noOfBytes = is.read(buffer);
			if (noOfBytes == -1) {
				break;
			} else {
				bos.write(buffer, 0, noOfBytes);
			}
		}
		bos.flush();
		bos.close();
		return bos.toByteArray();
	}

}
