import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlayerInfo implements Serializable {
    int clientNum;
    int numOfGuesses;
    String category;
    int numAttemps;
    int numCorrectGuessses;
    int numWrongGuesses;
    String word2Guess;
    String outString;

    ArrayList<String> animal = new ArrayList<String>();
    ArrayList<String> food = new ArrayList<String>();
    ArrayList<String> city = new ArrayList<String>();
 
    
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
        animal.add("cat" + "dog" + "lion" + "rabbit");
        food.add("pizza" + "taco" + "burger" + "pasta");
        city.add("chicago" + "austin" + "new york" + "las vegas");
     }

    void setCategory(String inCategory)
    {
        category = inCategory;
        updateOutString();
        sendOutWord();
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
    
    void sendOutWord()
    {
    	Random random = new Random();
    	if(category == "animal")
    	{
    		System.out.println(animal.get(random.nextInt(animal.size())));
    	}
    	
    	
    }

}