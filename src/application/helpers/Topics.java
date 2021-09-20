package application.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Topics {

	/** the number of words per quiz */
	private static final int NUM_WORDS = 5;

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
			ArrayList<Word> allWords = new ArrayList<>();
			Word[] randomWords = new Word[NUM_WORDS];

			File file = new File(WORDS_DIR + fileName);
			Scanner scanner = new Scanner(file);
			scanner.nextLine(); // throw away the first line, it's the title

			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(",");
				allWords.add(new Word(line[0], line[1]));
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

	/**
	 * Reads all the files in the src/words directory, and returns a list of Topic classes
	 */
	public static ArrayList<Topic> getTopics() {
		ArrayList<Topic> list = new ArrayList<>();
		File dir = new File(WORDS_DIR);
		File[] files = dir.listFiles();
		for (File file : files) {
			// someone has made a subfolder in WORDS_DIR for some reason. skip it
			if (file.isDirectory()) continue;

			String fileName = file.getName();
			try {
				Scanner scanner = new Scanner(file);
				String[] firstLine = scanner.nextLine().split(",");
				scanner.close();

				// see the README, the first line is the topic title
				// if the title in Te Reo is blank, only show the English title
				boolean hasTeReoTitle = firstLine[0].length() > 1;
				String topicTitle = hasTeReoTitle
					? (firstLine[1] + " / " + firstLine[0])
					: firstLine[1];

				list.add(new Topic(fileName, topicTitle));
			} catch (Exception ex) {
				// if something goes wrong, we just skip this file
				System.out.println("Skipped invalid topic file: " + fileName);
			}
		}
		return list;
	}
}
