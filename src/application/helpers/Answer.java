package application.helpers;

public class Answer {

	public static enum Correctness {
		CORRECT,
		ONLY_MACRONS_WRONG,
		ONLY_SYNTAX_WRONG, // just spaces/hyphens are wrong
		WRONG, // totally wrong
	}

	/**
	 * this is called after lowercasing everything so we
	 * don't need to replace capital macrons
	 */
	private static String doubleVowelsToMacrons(String input) {
		return input
			.replace("aa", "ā")
			.replace("ee", "ē")
			.replace("ii", "ī")
			.replace("oo", "ō")
			.replace("uu", "ū");
	}

	/** we only use this to check if the answer is nearly correct */
	private static String removeMacrons(String input) {
		return input
			.replace("ā", "a")
			.replace("ē", "e")
			.replace("ī", "i")
			.replace("ō", "o")
			.replace("ū", "u");
	}

	/**
	 * compares two strings (a and b) to see if they are equal or
	 * almost equal. This means we can give the user helpful feedback
	 * if they only made a minor error.
	 *
	 * This also means we can treat double vowels as equal to macrons,
	 * which is what some Iwi prefer (Ā = AA, ē = ee, etc.)
	 */
	public static Correctness checkAnswer(
		String usersAnswer,
		String correctAnswer
	) {
		// doesn't matter whether we mix up usersAnswer and correctAnswer
		String a = doubleVowelsToMacrons(usersAnswer.toLowerCase().strip());
		String b = doubleVowelsToMacrons(correctAnswer.toLowerCase().strip());

		// totally correct
		if (a.equals(b)) return Correctness.CORRECT;

		// if we omit hyphens and spaces, the answer would be correct
		if (a.replaceAll("( |-)", "").equals(b.replaceAll("( |-)", ""))) {
			return Correctness.ONLY_SYNTAX_WRONG;
		}

		// if we ignore macrons, the answer would be correct
		if (removeMacrons(a).equals(removeMacrons(b))) {
			return Correctness.ONLY_MACRONS_WRONG;
		}

		return Correctness.WRONG;
	}
}
