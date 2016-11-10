package com.felix.util;

/*
 * Created on 17.03.2005
 *
 * @author Felix Burkhardt
 */

/**
 * Mix an audio source file with some other audio while keeping the length of
 * the source file, i.e. repeating or cutting the mix file. Mixing byte-wise.
 */
public class SoundMixer {
	private static final short MAXVAL = Byte.MAX_VALUE;

	/**
	 * Mix infile1 and infile2 into outfile with factor. If infile1 is shorter
	 * infile 2 is cut, if infile2 is shorter, infile 2 will be repeated. Mixing
	 * byte-wise.
	 * 
	 * @param infile1
	 * @param infile2
	 * @param outfile
	 * @param factor
	 */
	public void mix(String inf1, String inf2, String outf, double factor) {
		try {
			byte[] indata1 = FileUtil.getFileContentAsByteArray(inf1);
			byte[] indata2 = FileUtil.getFileContentAsByteArray(inf2);
			byte[] outData = new byte[indata1.length];
			int if1length = indata1.length;
			int if2length = indata2.length;
			int zeroCounter = 0;
			if (if1length > if2length) {
				int if2c = 0;
				for (int i = 0; i < indata1.length; i++) {
					byte s1 = indata1[i];
					if (i >= if2length) {
						if2c = 0;
					}
					byte s2 = indata2[if2c++];
					int add = (int) (factor * s2);
					if (add == 0) {
						zeroCounter++;
					}
					int s = s1 + add;
					if (s > MAXVAL) {
						s = MAXVAL;
					}
					outData[i] = (byte) s;
				}
			} else {
				for (int i = 0; i < indata1.length; i++) {
					int s1 = indata1[i];
					int s2 = indata2[i];
					int add = (int) (factor * s2);
					if (add == 0) {
						zeroCounter++;
					}
					int s = s1 + add;
					if (s > MAXVAL) {
						s = MAXVAL;
					}
					outData[i] = (byte) s;
				}
			}

			FileUtil.writeFileContent(outf, outData);
			System.out.println("name: " + inf1 + ", wrote " + indata1.length
					+ " bytes, added " + zeroCounter + " times 0");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Interface for command line call.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String usage = "SoundMixer: mix two files (byte by byte) weighted by a factor into an outfile. version: "
				+ "\nusage: SoundMixer infile1 infile2 factor outfile";
		if (args.length != 4) {
			System.err.println(usage);
		} else {
			try {
				double fac = Double.valueOf(args[2]).doubleValue();
				new SoundMixer().mix(args[0], args[1], args[3], fac);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.err.println(usage);
			}
		}
	}
}