import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    public static String getRandomWord(ArrayList<String> inArr)
    {   
        Random random = new Random();
        int randomNumber = random.nextInt(inArr.size());
        return inArr.get(randomNumber);
    }

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