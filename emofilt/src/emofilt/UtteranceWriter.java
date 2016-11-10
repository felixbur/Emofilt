/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import emofilt.util.Util;

/**
 * An utteranceWriter prints an utterance in Mbrola pho-notation either to a
 * file or to stdout.
 * 
 * 
 * @author Felix Burkhardt
 */
public class UtteranceWriter {
	private String filename;

	private boolean printFile = false;

	/**
	 * Print utterance u to filename.
	 * 
	 * @param u
	 * @param filename
	 * @return
	 */
	public boolean printToFile(Utterance u, String filename) {
		setPrintFile(true);
		setFilename(filename);
		return printUtterance(u);
	}

	/**
	 * This attempts to print an utterance.
	 * 
	 * @param u
	 *            The utterance.
	 * @return True if success, false otherwise.
	 */
	public boolean printUtterance(Utterance u) {
		try {
			PrintStream ps;
			if (printFile)
				ps = new PrintStream(new FileOutputStream(filename));
			else
				ps = System.out;
			return printUtterance(u, ps);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the utterance
	 * 
	 * @param u
	 * @return
	 */
	public String getPhoString(Utterance u) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		printUtterance(u, ps);
		return os.toString();
	}

	/**
	 * This attempts to print an utterance.
	 * 
	 * @param u
	 *            The utterance.
	 * 
	 * @param ps
	 *            The Printstream as target.
	 * @return True if success, false otherwise.
	 */
	public boolean printUtterance(Utterance u, PrintStream ps) {
		try {
			ps.println(Constants.PHO_PRELIMINARY+Constants.VERSION);
			for (Iterator siter = u.getSyllables().iterator(); siter.hasNext();) {
				Syllable syl = (Syllable) siter.next();
				ps.print("; - ");
				if (syl.isWordStressed())
					ps.print(Constants.wordStressSymbol);
				if (syl.isFocusStressed())
					ps.print(Constants.focusStressSymbol);
				ps.println();
				for (Iterator piter = syl.getPhonemes().iterator(); piter
						.hasNext();) {
					Phoneme pho = (Phoneme) piter.next();
					ps.print(pho.getName() + " ");
					ps.print(pho.getDur() + " ");
					if (pho.hasF0Description()) {
						Util.printF0ValVec(pho.getF0vals(), ps);
					}
					ps.println();
				}
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setPrintFile(boolean printFile) {
		this.printFile = printFile;
	}
}