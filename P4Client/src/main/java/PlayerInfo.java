import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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
    String word2Guess;
    String outString;


 
    
    PlayerInfo(int inNum)
    {
        clientNum = inNum;
        numOfGuesses = 0;
        category = "N/A";
        numAttemps = 0;
        numCorrectGuessses = 0;
        numWrongGuesses =0;
        word2Guess = "N/A";
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
                        " |Category: "+ category + " |Word: "+ word2Guess + " |Attemps: "+numAttemps;
    }
    
 

}