import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class PlayerInfo implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**Current number of guesses*/
	int clientNum;
    int numOfGuesses;
    String category;

    /**Message used to communicate between server and client*/
    String backForthMessage;
    
    /**How many where correct out of 10*/
    int numCorrectGuessses;
    int numWrongGuesses;

    /**User Letter is wha't going to be send in as the single letter */
    String userletter;
    String word2Guess;

    String outString;

    /**Guessed so far, used for when reaveling words */
    String guessedSoFar;

    ArrayList<String> animal = new ArrayList<> (Arrays.asList("robot","dog", "ox", "cow", "sheep", "lion", "rabbit"));
    ArrayList<String> food = new ArrayList<> (Arrays.asList("pizza", "taco", "burger", "pasta"));
    ArrayList<String> city = new ArrayList<> (Arrays.asList("chicago", "austin", "denver", "seattle"));
 
    ArrayList<Character> userInput;
    
    PlayerInfo(int inNum)
    {
        clientNum = inNum;
        numOfGuesses = 0;
        category = "N/A";

        backForthMessage = "";

        numCorrectGuessses = 0;
        numWrongGuesses =0;
        /**End of how many where correct out of 10 */

        word2Guess = "N/A";
        guessedSoFar = "";

        /**?? */
        userletter = "_";
        userInput = new ArrayList<Character>();


        updateOutString();
        
 
     }

    void setCategory(String inCategory)
    {
        category = inCategory;
        updateOutString();
        
    }

    void setClientNum(int inNum)
    {
        clientNum = inNum;
        updateOutString();
    }

    void setWord2Guess(String inString)
    {
        word2Guess = inString;
        updateOutString();
    }

    void updateOutString()
    {
        outString = "Client#: " + clientNum + " |Correct Guesses: "+numCorrectGuessses+ 
                        " |Wrong Guesses: "+ numWrongGuesses + System.lineSeparator() + "   "+
                        " |Category: "+ category + " |Word: "+ word2Guess + " |Attemps: "+ Math.abs(6-numOfGuesses);
    }
    
 

}