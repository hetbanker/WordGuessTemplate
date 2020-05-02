import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuessTest {
	PlayerInfo info;

	@BeforeEach
	void init() {
		info = new PlayerInfo(0);
	}

	@Test
	void getNewWord() {
		info.numOfGuesses = 6;
		String word = GameLogicClient.getHiddenWord(info);
		for (Character c : word.toCharArray()) {
			assertEquals('_', c);
		}
	}

	@Test
	void testBadGuess() {
		info.word2Guess = "ebvhvhfeva";
		info.guessedSoFar = "__v_v___va";
		info.userletter = "x";
		GameLogicClient.getHiddenWord(info);
		assertEquals("__v_v___va", info.guessedSoFar.split(" ")[0]);
	}

	@Test
	void testBadGuess2() {
		info.word2Guess = "dvmbiuaaas";
		info.guessedSoFar = "____iuaaa_";
		info.userletter = "z";
		GameLogicClient.getHiddenWord(info);
		assertEquals("____iuaaa_", info.guessedSoFar.split(" ")[0]);
	}

	@Test
	void testCorrectGuess() {
		info.word2Guess = "nuyqtimcij";
		info.guessedSoFar = "n_____mc__";
		info.userletter = "i";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("n____imci_", word);
	}

	@Test
	void testCorrectGuess2() {
		info.word2Guess = "urowylngey";
		info.guessedSoFar = "_rowy____y";
		info.userletter = "g";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("_rowy__g_y", word);
	}

	@Test
	void testMultipleCorrectGuess() {
		info.word2Guess = "ewcdjincht";
		info.guessedSoFar = "e__d______";
		info.userletter = "j";
		GameLogicClient.getHiddenWord(info);
		info.userletter = "t";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("e__dj____t", word);
	}

	@Test
	void testMultipleCorrectGuess2() {
		info.word2Guess = "ryxwdvqakv";
		info.guessedSoFar = "__________";
		info.userletter = "k";
		GameLogicClient.getHiddenWord(info);
		info.userletter = "v";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("_____v__kv", word);
	}

	@Test
	void testGoodBadGuess() {
		info.word2Guess = "umdfwpskqw";
		info.guessedSoFar = "__d__p____";
		info.userletter = "w";
		GameLogicClient.getHiddenWord(info);
		info.userletter = "x";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("__d_wp___w", info.guessedSoFar.split(" ")[0]);
	}

	@Test
	void testGoodBadGuess2() {
		info.word2Guess = "ovwqqwmwfm";
		info.guessedSoFar = "_vw__w_w__";
		info.userletter = "l";
		GameLogicClient.getHiddenWord(info);
		info.userletter = "m";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("_vw__wmw_m", word);
	}

	@Test
	void testGuessedWord() {
		info.word2Guess = "kijmzvemoz";
		info.guessedSoFar = "kijm_vemo_";
		info.userletter = "z";
		String word = GameLogicClient.getHiddenWord(info);
		assertEquals("kijmzvemoz", word);
		assertTrue(info.userGuessedWord);
	}

}
