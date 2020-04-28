import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Logic {

	
	public static void main(String[] args) 
	{
	    ArrayList<String> animal = new ArrayList<> (Arrays.asList("robot","dog", "ox", "cow", "sheep", "lion", "rabbit"));
	    ArrayList<String> food = new ArrayList<> (Arrays.asList("pizza", "taco", "burger", "pasta"));
	    ArrayList<String> city = new ArrayList<> (Arrays.asList("chicago", "austin", "denver", "seattle"));

	   
	    System.out.println("Enter 1 for animal, 2 for food, 3 for city:");
	    
		@SuppressWarnings("resource") 
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt( );
		
		
		if(choice == 1)
		{
			choices(animal);
		}
		
		if(choice == 2)
		{
			choices(food);
		}
		
		if(choice == 3)
		{
			choices(city);
		}
		
		else 
		{
			System.out.println("Please pick 1,2,or 3 next time");
		}
	}

	
	public static void choices(ArrayList<String> nameHolder)
	{
        System.out.println();
        
        //get random element from the animal array list
        Random random = new Random();
        String theWordString = nameHolder.get(random.nextInt(nameHolder.size()));
        System.out.println("random word is: "+ theWordString);
        
        //converting theWordString into CharArray
        ArrayList<Character> charsArray = new ArrayList<Character>();
        for (char c : theWordString.toCharArray()) 
        {
        	charsArray.add(c);	//splitting the string into array
        }
        
        ArrayList<Character> userInput = new ArrayList<Character>(charsArray.size());
        
        //prints the dashes
        for(Integer i =0; i < charsArray.size(); i++)
        {
        	userInput.add('_');
        	System.out.print(" _ ");
        }
        
        
        
        int guesses = 6;
        
        Boolean flag = false;
        
        while(userInput.contains('_') && guesses > 0) 
        {
        	@SuppressWarnings("resource") 
			Scanner charInputScanner = new Scanner(System.in);
			System.out.println("\n\nEnter a character: ");
			char c = charInputScanner.next().charAt(0);
			
	        for(char letter : charsArray) 
	        {
	        	if(letter == c) 
	        	{
	        		int x = charsArray.indexOf(letter);
	        		int y = charsArray.lastIndexOf(c);
	        		userInput.set(x,letter);
	        		userInput.set(y, letter);
	        		flag=true;
	        		
	        	}
	        	

	        }
	       
	        if(flag == false)
	        {
	        	guesses--;
	        	
	        } 
	        
	        if(flag == true)
	        {
	        	flag =false;
	        }
    		for(char printer : userInput)
    		{
    			System.out.print(" " + printer + " ");

    		}
	        
	        System.out.println("  guesses left: " + guesses);
        }
        
        System.out.println("\n\nBye!");
	}
}
