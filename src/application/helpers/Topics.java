package application.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Topics {

	/** the number of words per quiz */
	public static final int NUM_WORDS = 5;

	/** the folder where the csv files are stored, relative to the repo root */
	private static final String WORDS_DIR = "./src/words/";

	/**
	 * Represents a topic file (from the WORDS_DIR folder)
	 * The fileName and the file title is available
	 */
	public static class Topic {

		public String fileName;
		public String title;

		public Topic(String fileName, String title) {
			this.fileName = fileName;
			this.title = title;
		}

		public Word[] getRandomWords() throws FileNotFoundException {
			// allWords is a temporary list which we use to pick the random words
			ArrayList<Word> allWords = new ArrayList<>();
			Word[] randomWords = new Word[NUM_WORDS];

			// read the file contents
			File file = new File(WORDS_DIR + fileName);
			Scanner scanner = new Scanner(file);
			scanner.nextLine(); // throw away the first line, it's the title

			// read through every line after the title row
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(",");
				allWords.add(
					// commas would break the csv format, so we use semicolons instead
					new Word(line[0].replaceAll(";", ","), line[1].replaceAll(";", ","))
				);
			}
			scanner.close();

			Random random = new Random();

			// loop NUM_WORDS times, and grab a new random word from the list of allWords
			// then delete the word we picked, so we don't pick it twice
			for (int i = 0; i < NUM_WORDS; i++) {
				int randomIndex = random.nextInt(allWords.size());
				randomWords[i] = allWords.get(randomIndex);
				allWords.remove(randomIndex);
			}

			return randomWords;
		}
	}

	/** Represents a word, in two languages */
	public static class Word {

		public String teReo;
		public String english;

		public Word(String teReo, String english) {
			this.teReo = teReo;
			this.english = english;
		}
	}

	/** gets the topic title in the user's preferred language */
	private static String getTopicTitle(
		String teReoTitle,
		String englishTitle,
		String language
	) {
		// see the README, the first line is the topic title
		// if the title in Te Reo is blank, only show the English title
		boolean hasTeReoTitle = teReoTitle.length() > 1;

		// we have no choice, so use the English title
		if (!hasTeReoTitle) return englishTitle;

		if (language.equals("both")) return englishTitle + " / " + teReoTitle;
		return language.equals("mi") ? teReoTitle : englishTitle;
	}

	/**
	 * Reads all the files in the WORDS_DIR directory, and returns a list of Topic classes
	 * @param language either "en", "mi", or "both"
	 */
	public static ArrayList<Topic> getTopics(String language) {
		ArrayList<Topic> topics = new ArrayList<>();

		// get a list of every file in the WORDS_DIR folder
		File dir = new File(WORDS_DIR);
		File[] files = dir.listFiles();

		// loop through every file (or subfolder) in the folder
		for (File file : files) {
			// someone has made a subfolder in WORDS_DIR for some reason. skip it
			if (file.isDirectory()) continue;

			String fileName = file.getName();
			try {
				// create a scanner to read just the first line of the file
				Scanner scanner = new Scanner(file);
				String[] firstLine = scanner.nextLine().split(",");
				scanner.close();

				String topicTitle = getTopicTitle(firstLine[0], firstLine[1], language);

				topics.add(new Topic(fileName, topicTitle));
			} catch (Exception error) {
				// if something goes wrong, we just skip this file
				System.err.println("Skipped invalid topic file: " + fileName);
			}
		}
		return topics;
	}
}
