package com.felix.util;

/*
 * Created on 17.03.2005
 *
 * @author Felix Burkhardt
 */

import java.io.File;

/**
 * 
 * Mix an audio source file with some other audio while keeping the length of
 * the source file, i.e. repeating or cutting the mix file.
 * 
 * @author Burkhardt.Felix
 * 
 * 
 */
public class SoundMixer_16bit {
	static boolean littleEndian = false;
	static final short MAXVAL = Short.MAX_VALUE;

	/**
	 * Mix an audio source file with some other audio while keeping the length
	 * of the source file, i.e. repeating or cutting the mix file.
	 * Mixing short-wise (2 bytes).
	 * 
	 * @param sourceFile
	 * @param mixFile
	 * @param outFile
	 * @param factor
	 */
	public void mix(String sourceFile, String mixFile, String outFile,
			double factor) {
		try {
			short outData[] = mix(sourceFile, mixFile, factor);
			File outfile = new File(outFile);
			FileUtil.writeFileContent(outfile, outData, littleEndian);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

	}

	/**
	 * 
	 * Mix an audio source file with some other audio while keeping the length
	 * of the source file, i.e. repeating or cutting the mix file.
	 * 
	 * @param sourceFile
	 * @param mixFile
	 * @param factor
	 * @return An array with the result values.
	 */
	public short[] mix(String sourceFile, String mixFile, double factor) {
		File infile1 = new File(sourceFile);
		File infile2 = new File(mixFile);
		try {
			short[] indata1 = AudioUtil.byteToShort(FileUtil.getFileContentAsByteArray(infile1.getPath()),littleEndian);
			short[] indata2 = AudioUtil.byteToShort(FileUtil.getFileContentAsByteArray(infile2.getPath()),littleEndian);
			return mix(indata1, indata2, factor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Mix an audio source file with some other audio while keeping the length
	 * of the source file, i.e. repeating or cutting the mix file.
	 * 
	 * @param source
	 *            A short array containing the source audio.
	 * @param mix
	 *            A short array containing the mixing audio.
	 * @param factor
	 *            The weighting factor (0-1) of the mixing audio.
	 * @return An array with the result values.
	 */
	public short[] mix(short[] source, short[] mix, double factor) {
		int if1length = source.length;
		int if2length = mix.length;
		short[] outData = new short[source.length];
		if (if1length > if2length) {
			int if2c = 0;
			for (int i = 0; i < source.length; i++) {
				short s1 = source[i];
				if (i >= if2length) {
					if2c = 0;
				}
				short s2 = mix[if2c++];
				int s = s1 + (short) (factor * s2);
				if (s > MAXVAL) {
					s = MAXVAL;
				}
				outData[i] = (short) s;
			}
		} else {
			for (int i = 0; i < source.length; i++) {
				short s1 = source[i];
				short s2 = mix[i];
				int s = s1 + (short) (factor * s2);
				if (s > MAXVAL) {
					s = MAXVAL;
				}
				outData[i] = (short) s;
			}
		}
		return outData;
	}

	public static void main(String[] args) {
		String usage = "SoundMixer: mix two raw audio files weighted by a factor into an outfile."
				+ "\nusage: SoundMixer infile1 infile2 factor outfile";
		if (args.length != 4) {
			System.err.println(usage);
		} else {
			try {
				double fac = Double.valueOf(args[2]).doubleValue();
				new SoundMixer_16bit().mix(args[0], args[1], args[3], fac);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.err.println(usage);
			}
		}
	}
}