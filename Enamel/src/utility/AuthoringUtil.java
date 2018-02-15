package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * This method will be responsible with Parsing the scenarioFile.
 * 
 * @author Jinho Hwang
 *
 */
public class AuthoringUtil {
	
	private final static String PCE = "PHRASE_CHECK_ERROR";

	/**
	 * Letting constructor private so this class can be a utility class.
	 * 
	 * @author Jinho Hwang
	 */
	private AuthoringUtil() {
		// Meh
	}

	/**
	 * Extension overload version of isCorrectlyParsed.
	 * 
	 * @author Jinho Hwang
	 * @param file
	 * @return List\<Phrase\> if given file is correctly parsed
	 * @return null if given file has incorrect syntax
	 */
	public static List<Phrase> isCorrectlyParsed(File file) {
		String scenarioFile = AuthoringUtil.fileToString(file);
		return isCorrectlyParsed(scenarioFile);
	}

	/**
	 * This method is responsible to determine if lines of Strings are correctly
	 * phrased. ( Correct phrase is available at
	 * https://wiki.eecs.yorku.ca/course_archive/2017-18/W/2311/_media/scenarioformat.pdf
	 * 
	 * @author Jinho Hwang
	 * @param scenarioFile
	 * @return List\<Phrase\> if the file was correctly phrased, return a Pair
	 *         of true and list of correct List of Phrases.
	 * @return null if given file failed to phrase correctly.
	 */
	public static List<Phrase> isCorrectlyParsed(String scenarioFile) {
		
		// -----------------------------------------------------------------/

		// List of phrases
		List<Phrase> phraseList = new LinkedList<Phrase>();
		
		// Get scanner to read each line of String
		Scanner scan = new Scanner(scenarioFile);

		// Get validation check of two first line------------//
		try {
			// Get 1st and 2nd line
			String[] firstLine = scan.nextLine().split("\\s");
			String[] secondLine = scan.nextLine().split("\\s");
			
			// Separtion of words
			String cell = firstLine[0];
			String button = secondLine[0];
			int cellNumber = Integer.parseInt(firstLine[1]);
			int buttonNumber = Integer.parseInt(secondLine[1]);
			
			// Throw exception if cell is not "Cell" or button is not "Button"
			if(!cell.equals("Cell")){
				scan.close();
				throw new IllegalArgumentException("First word of first line is not \"Cell\".");
			}
			if(!button.equals("Button")){
				scan.close();
				throw new IllegalArgumentException("First word of second line is not \"Button\".");
			}
			
			// Validation of first two line completed here;
			// Submit the first two line in the phraseList as phrases
			phraseList.add(new Phrase(cell,cellNumber + ""));
			phraseList.add(new Phrase(cell,buttonNumber + ""));
			
			
		} catch (Exception e) {
			// In case where word "Cell" or "Button" or number after cell and button fails, deal with exception
			errorLog("Exception error: " + e.toString(),
					"Expected format: Cell num1 \n Button num2 \n "
							+ "as the first two lines of the scenarion file, and where num1 and num2 are positive integers. \n"
							+ "Did not receive such a format in the scenario file and program had to end due to the incorrect"
							+ "file format.",PCE);
			// Exception was thrown, which means phrase was wrong, so return false
			return null;
		}
		
		scan.close();
		
		// Got validation check of two first line---------------//
		
		
		// TODO : MAKE THIS MULTILINE READER WORK
		// Translate given string to Phrases
		
		// While scenarioFile has line to read,
		while (scan.hasNextLine()) {

			// Check if each phrase is correct and spit out their output
			ArrayList<String> answerPair = parseThisLine(scan.nextLine());
			
		}
		
		
		// Get the correctness of the phrase of the line and type of correct
		// phrase
		String type = answerPair.getFirst(); // first is boolean
		String argument = answerPair.getSecond(); // second is string
		
		// If the phrase is incorrect, then whole thing is incorrect so
		// return null (false)
		if (type == null) {
			return null;
		}
		else if(type != null){
			
		}

		
		
		

		return null; // dummy return
	}
	
	

	/**
	 * This method checks if a phrase of a line of String is well-defined (
	 * correctly written ) returns true if the phrase follows the file format,
	 * returns false if the phrase does not.
	 * 
	 * @author ScenarioParser Writer ( Used his / her list of if statements /
	 *         comments )
	 * @author Jinho Hwang
	 * 
	 * @param fileLine
	 *            a phrase to test if it satisfy file format
	 * @return Pair<String, String>
	 * 			First String is type of legal phrase ( Ex : sound, skip, repeat...)
	 * 			Second String is argument of legal phrase (Ex : voice.wav, GOHERE, 1, 2...)
	 * 			Second String can be a null if the legal phrase do not have any argument to take.
	 * 			(Ex. First String = user-input, second String = null because user-input don't take an arg.
	 * 
	 * @return Pair<String, String>
	 * 			If the given line input is not syntactically correct, ( not well-formed ) then, 
	 * 			both first and second String is null. 
	 * 			First String is null if and only if the given argument is incorrect.
	 * 			
	 */
	private static ArrayList<String> parseThisLine(String fileLine) {

		ArrayList<String> answerPair = null;

		
		
		

		// The key phrase to indicate to play a sound file.
		if (fileLine.length() >= 8 && fileLine.substring(0, 8).equals("/~sound:")) {
			// command = sound 
			// arg = audio file name
			answerPair.add("sound");
			answerPair.add(fileLine.substring(8, fileLine.length()));
		}
		// The key phrase to indicate to skip to another part of the
		// scenario.
		else if (fileLine.length() >= 7 && fileLine.substring(0, 7).equals("/~skip:")) 
			// command = skip
			// arg = 
			answerPair.add("skip");
			answerPair.add(fileLine.substring(7, fileLine.length()));
		}
		// The key phrase to indicate to pause for a specified number of
		// seconds.
		else if (fileLine.length() >= 8 && fileLine.substring(0, 8).equals("/~pause:")) {
			answerPair.set("pause");
		}
		// The key phrase to assign a button to repeat text.
		else if (fileLine.length() >= 16 && fileLine.substring(0, 16).equals("/~repeat-button:")) {
			answerPair.set("repeat-button");
		}
		// The key phrase to signal that everything after that key phrase
		// will be repeated.
		else if (fileLine.length() >= 8 && fileLine.substring(0, 8).equals("/~repeat")) {
			answerPair.set("repeat");
		} else if (fileLine.length() >= 11 && fileLine.substring(0, 11).equals("/~endrepeat")) {
			answerPair.set("endrepeat");
		}
		// The key phrase to reset the action listeners of all of the
		// JButtons.
		else if (fileLine.length() >= 15 && fileLine.substring(0, 15).equals("/~reset-buttons")) {
			answerPair.set("reset-buttons");
		}
		// The key phrase to assign a button to skip to another part of the
		// scenario.
		else if (fileLine.length() >= 14 && fileLine.substring(0, 14).equals("/~skip-button:")) {
			answerPair.set("skip-button");
		}
		// The key phrase to clear the display of all of the braille cells.
		else if (fileLine.length() >= 15 && fileLine.substring(0, 15).equals("/~disp-clearAll")) {
			answerPair.set("disp-clearAll");
		}
		// The key phrase to set a Braille cell to a string.
		else if (fileLine.length() >= 17 && fileLine.substring(0, 17).equals("/~disp-cell-pins:")) {
			answerPair.set("disp-cell-pins:");
		}
		// The key phrase to represent a string in Braille.
		else if (fileLine.length() >= 14 && fileLine.substring(0, 14).equals("/~disp-string:")) {
			answerPair.set("disp-string");
		}
		// The key phrase to change the cell to represent a character in
		// Braille.
		else if (fileLine.length() >= 17 && fileLine.substring(0, 17).equals("/~disp-cell-char:")) {
			answerPair.set("disp-cell-char");
		}
		// The key phrase to raise a pin of the specified Braille cell.
		else if (fileLine.length() >= 18 && fileLine.substring(0, 18).equals("/~disp-cell-raise:")) {
			answerPair.set("disp-cell-raise");
		}
		// The key phrase to lower a pin of the specified Braille cell.
		else if (fileLine.length() >= 18 && fileLine.substring(0, 18).equals("/~disp-cell-lower:")) {
			answerPair.set("disp-cell-lower");
		}
		// The key phrase to clear a Braille cell.
		else if (fileLine.length() >= 18 && fileLine.substring(0, 18).equals("/~disp-cell-clear:")) {
			answerPair.set("disp-cell-clear");
		}
		// The key phrase to lower pins of the Braille cell.
		else if (fileLine.length() >= 21 && fileLine.substring(0, 21).equals("/~disp-cell-lowerPins")) {
			answerPair.set("disp-cell-lowerPins");
		}
		// The key phrase to wait for the program to receive a user's input.
		else if (fileLine.length() >= 12 && fileLine.substring(0, 12).equals("/~user-input")) {
			answerPair.set("user-input");
		}
		// Anything other than the specified commands above, is to be
		// interpreted as text that
		// will be spoken for the user to hear.
		else {
			answerPair.set("speak");
		}

		return answerPair;

	}

	/**
	 * @author ScenarioParser writer
	 * @param exception
	 *            the exception message
	 * @param message
	 *            the specific error message
	 * @param errorLoggerName
	 *            the name of error file and logger's name
	 */

	public static void errorLog(String exception, String message, String errorLoggerName) {
		Logger logger = Logger.getLogger(errorLoggerName);
		FileHandler fh;

		System.out.println(message);

		// speak("Error! Something went wrong in the program! Please consult a
		// teacher "
		// + "or administrator for assistance! Also please view the ERROR_LOG
		// file for more details");
		// The try-catch block is to format the Logger class so that the error
		// log file is easier to understand.
		try {
			File f = new File(errorLoggerName + ".txt");
			fh = new FileHandler(f.toString());

			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			logger.warning(exception);
			logger.info(message);
			fh.close();

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// exit();
	}

	/**
	 * A method that scans through input file and return String.
	 * 
	 * @author Jinho Hwang
	 * @param file
	 *            Input File.
	 * @return fileContent Output String.
	 */
	public static String fileToString(File file) {
		String fileContent = "";
		if (file.isFile()) {
			try {
				Scanner scan = new Scanner(file);
				while (scan.hasNextLine()) {
					fileContent += scan.nextLine() + "\n";
				}
				scan.close();
			} catch (FileNotFoundException e) {
				System.out.println("File parsing error. File name: " + file.getName());
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException(file.getName() + " is not a file!");
		}

		return fileContent;

	}
}
