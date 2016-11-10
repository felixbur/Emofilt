package com.felix.util;

import java.io.*;
import java.util.*;

/**
 * Some methods around file management.
 * 
 * @author felix
 * 
 */
public class FileUtil {

	public final static String STD_ENCODING = "ISO8859-1";
	public final static String ENCODING_UTF_8 = "UTF-8";
	public final static String ENCODING_ISO8859_1 = "ISO8859-1";
	public final static String COMMENT_START_1 = "#";
	public final static String COMMENT_START_2 = ";";
	public final static long WAIT_TIME = 200;
	public final static long MAX_TIME = 5000;

	/**
	 * Return the current working directory.
	 * 
	 * @return The File.
	 */
	public static File getCurrentDirFile() {
		try {
			return new File(".").getCanonicalFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return the current working directory.
	 * 
	 * @return The Path.
	 */
	public static String getCurrentDirPath() {
		try {
			return new File(".").getCanonicalPath();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Wait till a file appeared.
	 * 
	 * @param filename
	 *            The file.
	 * @param writeProgressToSystemErr
	 *            If debug should be printed.
	 */
	public static void waitForFile(String filename,
			boolean writeProgressToSystemErr) {
		waitForFile(filename, WAIT_TIME, MAX_TIME, writeProgressToSystemErr);
	}

	/**
	 * Wait for a file to be created, size larger than zero and not changing in
	 * a specific time.
	 * 
	 * @param filename
	 *            The filename.
	 * @param waitTime
	 *            The time to check change in size in milliseconds.
	 */
	public static void waitForFile(String filename, long waitTime,
			long maxTime, boolean writeProgressToSystemErr) {
		long fileSize = 0;
		long oldFileSize = -1;
		long timePassed = 0;
		while (true) {
			try {
				Thread.sleep(waitTime);
				timePassed += waitTime;
			} catch (Exception e) {
				// TODO: handle exception
			}
			fileSize = new File(filename).length();
			if (writeProgressToSystemErr)
				System.err.println("fs: " + fileSize + ". ofs: " + oldFileSize);
			if (oldFileSize < fileSize) {
				oldFileSize = fileSize;
			} else {
				if (fileSize > 0)
					break;
			}
			if (timePassed > maxTime) {
				System.err.println("maxtime " + maxTime
						+ " reached, giving up.");
				return;
			}
		}
	}

	/**
	 * <pre>
	 * file 1 in 2 kopieren.
	 * </pre>
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public static void copyFile(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}

	/**
	 * Test if a string starts with a comment sign, irrespective of leading
	 * whitespace OR string contains only whitespace.
	 * 
	 * @param test
	 * @return The test result.
	 */
	public static boolean isCommentOrEmpty(String test) {
		test = test.trim();
		if (test.startsWith(COMMENT_START_1)
				|| test.startsWith(COMMENT_START_2)
				|| test.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * Insert part of the name to filename, e.g. bla/myname.ext -> n�
	 * bla/myname_foo.ext
	 * 
	 * @param filename
	 * @param part
	 * @return The new String.
	 */
	public static String addNamePart(String filename, String part) {
		File file = new File(filename);
		String path = file.getParent();
		String fn = file.getName();
		return path + "/" + getNameWithoutExtension(fn) + part + "."
				+ getExtension(fn);

	}

	/**
	 * checks if file exists
	 * 
	 * @param fileName
	 *            abssolute filename
	 * @return true if and only if the file denoted by this abstract pathname
	 *         exists; false otherwise
	 * @exception SecurityException
	 *                Description of the Exception
	 */
	public static boolean existFile(String fileName) throws SecurityException {
		boolean exist = false;
		File tmpFile = new File(fileName);
		exist = tmpFile.isFile();
		tmpFile = null;
		return exist;
	}

	/**
	 * checks if path exists
	 * 
	 * @param fileName
	 *            path to test
	 * @return true if path exists, otherwise false
	 * @exception SecurityException
	 *                Description of the Exception
	 */
	public static boolean existPath(String fileName) throws SecurityException {
		boolean exist = false;
		if (fileName != null && fileName.length() > 0) {
			File tmpFile = new File(fileName);
			exist = tmpFile.isDirectory();
			tmpFile = null;
		}
		return exist;
	}

	/**
	 * renames a file to the new filename
	 * 
	 * @param srcFilename
	 *            old filename
	 * @param destFilename
	 *            new filename
	 * @return true if success, otherwise false
	 */
	public static boolean rename(String srcFilename, String destFilename) {
		File srcFile;
		File destFile;
		boolean result = false;

		srcFile = new File(srcFilename);
		destFile = new File(destFilename);
		try {
			result = srcFile.renameTo(destFile);
		} catch (Exception e) {
			System.err.println("failed to rename file: " + e.toString());
			// e.printStackTrace();
		} finally {
			srcFile = null;
			destFile = null;
		}
		return result;
	}

	/**
	 * delete file
	 * 
	 * @param fileName
	 *            file to delete
	 * @return true if success, otherwise false
	 */
	public static boolean delete(String fileName) throws Exception {
		File f = new File(fileName);
		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
			throw new IllegalArgumentException(
					"Delete: no such file or directory: " + fileName);

		if (!f.canWrite())
			throw new IllegalArgumentException("Delete: write protected: "
					+ fileName);

		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException(
						"Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = f.delete();

		if (!success)
			throw new IllegalArgumentException("Delete: deletion of "
					+ fileName + " failed");
		f = null;
		return success;
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 * 
	 * @param dir
	 * @return True if success.
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * creates a new File
	 * 
	 * @param fileName
	 *            filename of the file to create
	 * @return true if successs, otherwise false
	 * @exception Exception
	 */
	public static boolean create(String fileName) throws Exception {
		boolean result = false;
		File tmpFile = new File(fileName);
		try {
			if (!existPath(fileName)) {
				createDir(fileName);
			}
			result = tmpFile.createNewFile();
		} catch (Exception e) {
			System.err.println("Failed to create file: " + e.toString());
			throw new Exception(e.toString());
		} finally {
			tmpFile = null;
		}
		return result;
	}

	/**
	 * creates a directory from filename
	 * 
	 * @param fileName
	 *            the filename of the file to create
	 * @return true if creation success, otherwise false
	 * @exception Exception
	 */
	public static boolean createDirFromFilename(String fileName)
			throws Exception {
		boolean result = false;
		File tmpFile = new File(fileName);
		try {
			fileName = tmpFile.getParent();
			tmpFile = new File(fileName);
			result = tmpFile.mkdir();
		} catch (Exception e) {
			System.err.println("Failed to create directory: " + e.toString());
			throw new Exception(e.toString());
		} finally {
			tmpFile = null;
		}
		return result;
	}

	/**
	 * Given a path string create all the directories in the path. For example,
	 * if the path string is "java/applet", the method will create directory
	 * "java" and then "java/applet" if they don't exist. The file separator
	 * string "/" is platform dependent system property.
	 * 
	 * @param path
	 *            Directory path string.
	 * @return true if creation success, otherwise false
	 */
	public static boolean createDir(String path) {
		boolean result = false;
		if (path == null || path.length() == 0) {
			result = false;
		}
		File dir = new File(path);
		try {
			if (dir.exists()) {
				result = true;
			} else {
				if (dir.mkdirs()) {
					result = true;
				} else {
					System.out.println("unable to create directory");
					result = false;
				}
			}
		} catch (SecurityException exc) {
			System.err.println("Failed to create file: " + exc.toString());
			// exc.printStackTrace();
			result = false;
		} catch (Exception exc) {
			System.err.println("Failed to create file: " + exc.toString());
			// exc.printStackTrace();
			result = false;
		} finally {
			dir = null;
		}
		return result;
	}

	/**
	 * returns true if fileName is a directory, otherwise false
	 * 
	 * @param fileName
	 *            filename to check if it's a directory
	 * @return true or false
	 */
	public static boolean isDirectory(String fileName) {
		boolean result = false;
		File tmpFile = new File(fileName);
		fileName = fileName.replace('/', File.separatorChar);
		String tmpPath = tmpFile.getAbsolutePath();
		if (tmpPath.equalsIgnoreCase(fileName)) {
			result = true;
		}
		tmpPath = tmpFile.getPath();
		if (tmpPath.equalsIgnoreCase(fileName)) {
			result = true;
		}
		tmpFile = null;
		tmpPath = null;
		return result;
	}

	/**
	 * Get the extension of a file, without the ".". Must contain non-numeric
	 * characters
	 * 
	 * @param f
	 *            the file you want to get the extension from
	 * @return the extension of the file or null, if none was there.
	 */
	public static String getExtension(File f) {
		String ext = null;
		boolean nunNumerical = false;
		if (f != null) {
			String s = f.getName();
			int i = s.lastIndexOf('.');
			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1);
			}
		}
		if (StringUtil.containsOnlyNumbers(ext))
			return null;
		return ext;
	}

	/**
	 * Return a filename with a specified extension.
	 * 
	 * @param fn
	 *            The file name.
	 * @param extension
	 *            The extension (without ".")
	 * @return The new name.
	 */
	public static String enforceExtension(String fn, String extension) {
		return getNameWithoutExtension(fn) + "." + extension;
	}

	/**
	 * Write some String to a file.
	 * 
	 * @param content
	 *            The String.
	 * @param fileName
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	/**
	 * Write some String to a file.
	 * 
	 * @param content
	 *            The String.
	 * @param fileName
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void writeFileContent(String fileName, String content)
			throws Exception {
		writeFileContent(fileName, content, STD_ENCODING);
	}

	/**
	 * Write a String (might contain newlines) to a file.
	 * 
	 * @param fileName
	 *            The file name.
	 * @param content
	 *            The String to write.
	 * @param charEncoding
	 *            The char encoding, e.g. Constants.CHAR_ENCODING.
	 * @throws Exception
	 *             E.g. if the file is not writable.
	 */
	public static void writeFileContent(String fileName, String content,
			String charEncoding) throws Exception {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charEncoding));
			bw.write(content);
		} catch (Exception e) {
			System.err.println("error trying to get file content"
					+ e.toString());
			// e.printStackTrace();
		} finally {
			bw.close();
			bw = null;
		}
	}

	/**
	 * Write a Vector of String (might contain newlines) to a file.
	 * 
	 * @param fileName
	 *            The file name.
	 * @param content
	 *            The Vector of Strings to write.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8".
	 * @throws Exception
	 *             E.g. if the file is not writable.
	 */
	public static void writeFileContent(String fileName,
			Vector<String> content, String charEncoding) throws Exception {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charEncoding));
			for (Iterator<?> iterator = content.iterator(); iterator.hasNext();) {
				String line = (String) iterator.next();
				bw.write(line + "\n");
			}
		} catch (Exception e) {
			System.err.println("error trying to get file content"
					+ e.toString());
			e.printStackTrace();
		} finally {
			bw.close();
			bw = null;
		}
	}

	/**
	 * Write some Strings to a file.
	 * 
	 * @param content
	 *            The String.
	 * @param fileName
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void writeFileContent(String fileName, Vector<String> content)
			throws Exception {
		writeFileContent(fileName, content, STD_ENCODING);
	}

	/**
	 * writes a byte array to file
	 * 
	 * @param fileName
	 *            the filename
	 * @param content
	 *            the byte array
	 * @exception Exception
	 */
	public static void writeFileContent(String fileName, byte[] content)
			throws Exception {
		BufferedOutputStream writer = null;
		try {
			writer = new BufferedOutputStream(new FileOutputStream(fileName));
			writer.write(content);
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Returns the content of a given file as byte array.
	 * 
	 * @param fileName
	 *            Description of the Parameter
	 * @return The content in a String or empty String, if an error occured.
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static byte[] getFileContentAsByteArray(String fileName)
			throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");
		Long lengthFile;
		byte[] b;
		try {
			raf.seek(0);
			lengthFile = new Long(raf.length());
			b = new byte[lengthFile.intValue()];
			raf.readFully(b);
		} catch (Exception e) {
			System.err.println("failed to get content from file: "
					+ e.toString());
			// e.printStackTrace();
			throw new Exception();
		} finally {
			raf.close();
			raf = null;
			lengthFile = null;
		}
		return b;
	}

	/**
	 * Returns the content of a given file as String.
	 * 
	 * @param f
	 *            Description of the Parameter
	 * @param content
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void writeFileContent(File f, String content)
			throws Exception {
		writeFileContent(f.getAbsolutePath(), content);
	}

	public static void writeFileContent(File f, String content,
			String charEncoding) throws Exception {
		writeFileContent(f.getAbsolutePath(), content, charEncoding);
	}

	/**
	 * Get lines of a file given a filename and encoding.
	 * 
	 * @param f
	 *            The filename.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8"
	 * @return A Vector containing the file lines.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(File f, String charEncoding)
			throws Exception {
		return getFileLines(f.getAbsolutePath(), charEncoding);
	}

	/**
	 * Get lines of a file enocded with UTF-8, ignoring comments.
	 * 
	 * @param fn
	 *            The path to the file.
	 * @return Vector fo Strings.
	 * @throws Exception
	 */
	public static Vector<String> getFileLinesWithoutComments(String fn)
			throws Exception {
		return getFileLinesWithoutComments(fn, STD_ENCODING);
	}

	/**
	 * Get lines of a file enocded with UTF-8.
	 * 
	 * @param fn
	 *            The path to the file.
	 * @return Vector fo Strings.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(String fn) throws Exception {
		return getFileLines(fn, STD_ENCODING);
	}

	/**
	 * Get lines of a file enocded with UTF-8.
	 * 
	 * @param f
	 *            The path to the file.
	 * @return Vector fo Strings.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(File f) throws Exception {
		return getFileLines(f.getAbsolutePath(), STD_ENCODING);
	}

	/**
	 * Get lines of a file enocded with UTF-8.
	 * 
	 * @param inputStream
	 *            the input stream.
	 * @return Vector fo Strings.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(InputStream inputStream)
			throws Exception {
		return getFileLines(inputStream, STD_ENCODING);
	}

	/**
	 * Get lines of a file given a filename and encoding.
	 * 
	 * @param fn
	 *            The filename.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8"
	 * @return A Vector containing the file lines.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(String fn, String charEncoding)
			throws Exception {
		BufferedReader input = null;
		Vector<String> ret = new Vector<String>();
		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		input = new BufferedReader(new InputStreamReader(
				new FileInputStream(fn), charEncoding), 1024);
		String line = null;
		while ((line = input.readLine()) != null) {
			ret.add(line);
		}
		if (input != null)
			input.close();
		return ret;
	}

	/**
	 * Get lines of a file given a filename and encoding, ignoring comments and
	 * empty lines.
	 * 
	 * @param fn
	 *            The filename.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8"
	 * @return A Vector containing the file lines.
	 * @throws Exception
	 */
	public static Vector<String> getFileLinesWithoutComments(String fn,
			String charEncoding) throws Exception {
		BufferedReader input = null;
		Vector<String> ret = new Vector<String>();
		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		input = new BufferedReader(new InputStreamReader(
				new FileInputStream(fn), charEncoding), 1024);
		String line = null;
		while ((line = input.readLine()) != null) {
			if (!isCommentOrEmpty(line))
				ret.add(line);
		}
		if (input != null)
			input.close();
		return ret;
	}

	/**
	 * Get lines of a file given an inputStream and encoding.
	 * 
	 * @param inputStream
	 *            The input stream.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8"
	 * @return A Vector containing the file lines.
	 * @throws Exception
	 */
	public static Vector<String> getFileLines(InputStream inputStream,
			String charEncoding) throws Exception {
		BufferedReader input = null;
		Vector<String> ret = new Vector<String>();
		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		input = new BufferedReader(new InputStreamReader(inputStream,
				charEncoding), 1024);
		String line = null;
		while ((line = input.readLine()) != null) {
			ret.add(line);
		}
		if (input != null)
			input.close();
		return ret;
	}

	/**
	 * Get lines of a file given a filename and encoding.
	 * 
	 * @param fn
	 *            The filename.
	 * @param charEncoding
	 *            The char encoding, e.g. "UTF-8"
	 * @return A String containing the file lines.
	 * @throws Exception
	 */
	public static String getFileText(String fn, String charEncoding)
			throws Exception {
		BufferedReader input = null;
		String ret = "";
		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		// default buffer size is 4k - using smaller
		input = new BufferedReader(new InputStreamReader(
				new FileInputStream(fn), charEncoding), 1024);
		String line = null;
		while ((line = input.readLine()) != null) {
			ret += line + "\n";
		}
		if (input != null)
			input.close();
		return ret;
	}

	/**
	 * Returns the content of a given file as String.
	 * 
	 * @param fileName
	 *            Description of the Parameter
	 * @return The content in a String or empty String, if an error occured.
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static String getFileText(String fileName) throws Exception {
		return getFileText(fileName, STD_ENCODING);
	}

	/**
	 * Returns the content of a given file as String.
	 * 
	 * @param f
	 *            Description of the Parameter
	 * @return The content in a String or empty String, if an error occured.
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static String getFileText(File f) throws Exception {
		return getFileText(f.getAbsolutePath());
	}

	/**
	 * returns the reversed vector of the input vector
	 * 
	 * @param src
	 *            the Vector to reverse
	 * @return the reversed Vector
	 */
	public static Vector<String> reverseVector(final Vector<String> src) {
		Vector<String> ret = new Vector<String>();
		for (int i = src.size(); i > 0; i--) {
			ret.addElement((String) src.elementAt(i - 1));
		}
		return ret;
	}

	/**
	 * Return an absolute path, uses Global.getAppRootPath if neccessary.
	 * 
	 * @param fp
	 *            The (perhaps relative) filePath
	 * @param appRootPath
	 *            the 'appRootPath' (as it is given in the 'global' object)
	 * @return An absoluteFilePath or null, if getAppRootPath == null.
	 */
	public static String getAbsoluteFilePath(String fp, String appRootPath) {
		String retPath = null;
		if (fp.indexOf(":") > -1) {
			retPath = fp.replace('/', File.separatorChar);
		} else {
			if (!fp.startsWith("/")) {
				fp += "/";
			}
			if (appRootPath != null) {
				retPath = appRootPath + fp.replace('/', File.separatorChar);
			} else {
				System.err
						.println("- getAboluteFilePath WARNING: no absolute filepath given and appRootPath == NULL!");
			}
		}
		return retPath;
	}

	/**
	 * Return a hashmap from a filepath. Format is "<keyString> | <valueString>"
	 * (each line one value pair).
	 * 
	 * @param filename
	 *            The path to the file that containes the values.
	 * @return The hashmap.
	 */
	public static HashMap<String, String> getValuesFromFile(String filename) {
		HashMap<String, String> hm = new HashMap<String, String>();
		try {
			String line = null;
			String key, value;
			StringTokenizer st = null;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
					st = new StringTokenizer(line, "|");
					key = st.nextToken();
					value = "";
					try {
						value = st.nextToken();
					} catch (Exception ex) {
						System.err.println("found no value for: " + key);

					}
					hm.put(key, value);
					// System.err.println("key: "+key+", val: "+value);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error reading config file: " + e);
		}
		return hm;
	}

	/**
	 * write short array as byte file.
	 * 
	 * @param audioFile
	 */
	public static void writeFileContent(File audioFile, short[] data,
			boolean littleEndian) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					audioFile));
			byte hb, lb;
			if (littleEndian) {
				for (int i = 0; i < data.length; i++) {
					lb = (byte) data[i];
					hb = (byte) (data[i] >> 8);
					out.write(lb);
					out.write(hb);
				}
			} else {
				// big Endian
				for (int i = 0; i < data.length; i++)
					out.writeShort(data[i]);
			}
			out.close();
		} catch (Exception audioEx) {
			System.err.println("problem writing audio-track: " + audioEx);
		}
	}

	/**
	 * Get the extension with out the ".".
	 * 
	 * @param fileName
	 *            The name.
	 * @return The extension or "". if none was there.
	 */
	public final static String getExtension(String fileName) {
		String ext = getExtension(new File(fileName));
		if (ext != null) {
			return ext;
		}
		return "";

	}

	/**
	 * Get the name without the exteion.
	 * 
	 * @param fileName
	 *            The name.
	 * @return The name without (the last) extension.
	 */
	public final static String getNameWithoutExtension(String fileName) {
		if (getExtension(fileName).length() > 0) {
			return fileName.substring(0, fileName.lastIndexOf('.'));
		} else {
			return fileName;
		}
	}
}
