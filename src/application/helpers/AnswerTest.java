package application.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for the Answer class */
class AnswerTest {

	@Test
	void testCorrectAnswers() {
		assertEquals(Answer.checkAnswer("abc", "abc"), Answer.Correctness.CORRECT);
		assertEquals(Answer.checkAnswer("Abc", "aBc"), Answer.Correctness.CORRECT);
		assertEquals(Answer.checkAnswer("Ōā", "ōĀ"), Answer.Correctness.CORRECT);
		assertEquals(Answer.checkAnswer("eE", "ē"), Answer.Correctness.CORRECT);
		assertEquals(
			Answer.checkAnswer("pākehā", "paakehaa"),
			Answer.Correctness.CORRECT
		);
	}

	@Test
	void testOnlyMacronsWrong() {
		assertEquals(
			Answer.checkAnswer("Opotiki", "Ōpōtiki"),
			Answer.Correctness.ONLY_MACRONS_WRONG
		);
	}

	@Test
	void testOnlySyntaxWrong() {
		assertEquals(
			Answer.checkAnswer("Tāmaki Makaurau", "Tāmaki Makau-rau"),
			Answer.Correctness.ONLY_SYNTAX_WRONG
		);
	}

	@Test
	void testWrongAnswers() {
		assertEquals(
			Answer.checkAnswer("papa tuatahi", "papatū a tahi"),
			Answer.Correctness.WRONG
		);
	}
}
