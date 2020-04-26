
public class PlayerInfo {
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
        outString = "Client#: " + clientNum + " Correct Guesses: "+numCorrectGuessses+ 
                        " Wrong Guesses: "+ numWrongGuesses + System.lineSeparator() + "   "+
                        "Category: "+ category + " Word: "+ word2Guess + " Attemps: "+numAttemps;
    }



}