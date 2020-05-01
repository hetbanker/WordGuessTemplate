import java.util.ArrayList;
import java.util.Random;

public class GameLogicClient {


    public static String getHiddenWord(PlayerInfo inInfo)
    {
        String retString;

        /**New Word */
        if(inInfo.numOfGuesses == 7)
        {
            retString = "";
            retString = Repeats("_", inInfo.word2Guess.length());
            inInfo.guessedSoFar = retString;
            return retString;
        }

        /**Word Guessing */
        else
        {
        		retString = Repeats(inInfo.userletter.charAt(0), inInfo.guessedSoFar, inInfo.word2Guess, inInfo);
        		if(retString.indexOf('_') == -1)
             	{
             		retString = "'" + retString + "'" + " You guessed the correct word!";
             		inInfo.userGuessedWord = true; 
             		inInfo.numCorrectGuessses++;
             		return retString;
             	}
                inInfo.guessedSoFar = retString;
                return retString;
        }
    }

    /**Returns of String of a certain length */
    private static String Repeats(String c, int inInt)
    {
        String retString = "";
        for(int i = 0; i < inInt; i++)
        {
            retString = retString + c;
        }
        return retString;
    }
    /**For a String that's already declared*/
    private static String Repeats(char userLetter, String hiddenWord, String word2Guess, PlayerInfo inInfo)
    {
        String retString = "";
        Boolean enteredBoolean = false;
        for(int i = 0; i < word2Guess.length(); i++)
        {
            if(word2Guess.charAt(i) == userLetter)
            {
                enteredBoolean = true;
                retString = retString + word2Guess.charAt(i);
            }
           
            else
            {
                retString = retString + hiddenWord.charAt(i);
            }
        }
        
        if(enteredBoolean)
        {
            inInfo.numOfGuesses += 1;
        }
        return retString;
    }
            
}