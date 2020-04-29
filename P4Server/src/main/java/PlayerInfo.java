import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class PlayerInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int clientNum;
    int numOfGuesses;
    String category;
    int numAttemps;
    int numCorrectGuessses;
    int numWrongGuesses;
    String userletter;
    String word2Guess;
    String outString;

    ArrayList<String> animal = new ArrayList<> (Arrays.asList("robot","dog", "ox", "cow", "sheep", "lion", "rabbit"));
    ArrayList<String> food = new ArrayList<> (Arrays.asList("pizza", "taco", "burger", "pasta"));
    ArrayList<String> city = new ArrayList<> (Arrays.asList("chicago", "austin", "denver", "seattle"));
 
    ArrayList<Character> userInput;
    
    PlayerInfo(int inNum)
    {
        clientNum = inNum;
        numOfGuesses = 0;
        category = "N/A";
        numAttemps = 0;
        numCorrectGuessses = 0;
        numWrongGuesses =0;
        word2Guess = "N/A";
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

    void updateOutString()
    {
        outString = "Client#: " + clientNum + " |Correct Guesses: "+numCorrectGuessses+ 
                        " |Wrong Guesses: "+ numWrongGuesses + System.lineSeparator() + "   "+
                        " |Category: "+ category + " |Character: "+ userletter + " |Attemps: "+numAttemps;
    }
    
 
    void setUserLetter(String userLetter)
    {
    	userletter = userLetter;
    	updateOutString();
    }

}