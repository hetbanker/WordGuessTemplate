import java.util.ArrayList;
import java.util.Random;

public class GameLogicClient {

    public static String getHiddenWord(PlayerInfo inInfo)
    {
        String retString;

        /**New Word */
        if(inInfo.numOfGuesses == 6)
        {
            retString = "";

            for(int i = 0; i < inInfo.word2Guess.length(); i++)
            {
                retString = retString + "_ ";
            }

            return retString;
        }

        /**TODO: Current Word */

        return ""; //Place Holder
    }

}