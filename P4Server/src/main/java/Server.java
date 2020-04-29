import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;
	int wins = 0;
	int losses = 0;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	static ArrayList<PlayerInfo> clientInfo;

	TheServer server;
	private Consumer<Serializable> callback;
	private Consumer<Serializable> gameStatus;
	
	
	Server(Consumer<Serializable> call, Consumer<Serializable> inStatus){
		//TODO: Add the Port variable and replace it in run
		callback = call;
		gameStatus = inStatus;
		server = new TheServer();
		server.start();
		clientInfo = new ArrayList<PlayerInfo>();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			//This is just for the initialization of the server, after this is up,
			//There's no need to update it anymore, and all logic would go in runMethod of ClientThread
			try(ServerSocket mysocket = new ServerSocket(5555);){
			System.out.println("Server is waiting for a client!");
			
           //The initialization of the TextField (server)
 	       gameStatus.accept("Clients Connected: "+(count)+
 	       " Wins: "+wins+" Loss: "+losses);
		  
			
		    while(true) {
					ClientThread c = new ClientThread(mysocket.accept(), count);
					gameStatus.accept("!!!! client has connected to server: " + "client #" + count);
					clientInfo.add(new PlayerInfo(count));
					clients.add(c);
					count++;
					c.start();

					//TODO: DEBUGGING PRINT STATEMENT< REMOVE
					/*
					if(clientInfo.size() > 1){
						clientInfo.get(0).outString = "HWLLO";
					}*/

					callback.accept("");

					PauseTransition nPause = new PauseTransition(new Duration(2000));

					nPause.setOnFinished(e->{
						gameStatus.accept("Clients Connected: "+(count -1)+
						" Wins: "+wins+" Loss: "+losses);
					});
					nPause.play();
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			//String showing Player Information to the server
			//String =
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updateClients(PlayerInfo playerInfo) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(playerInfo);
					 t.out.reset();
					}
					catch(Exception e) {}
				}
			}
			
			public void run()
			{
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				//updateClients("new client on server: client #"+count);
				PlayerInfo playerInfo	= new PlayerInfo(count);
				
				 while(true) {
					    try {

							//Get PlayerInfo .in
							PlayerInfo data = (PlayerInfo)in.readObject();
							data.setClientNum(clientInfo.get(this.count-1).clientNum);
							clientInfo.set(this.count-1, data);

							//Located the position in clientInfo using shadowed count
							callback.accept("");
							//Update variables accordingly

							/**TODO: Debugging Purposes */
							data.setCategory("ITS Working Bruh");
							clients.get(this.count-1).out.writeObject(data);

							//TODO: Write the rest of the log for the server
							
							System.out.println("Inside the while loop");
							 
							System.out.println(data.category);
							//System.out.println(clientInfo.indexOf(data) + " At index 1");
							
							if(data.category.equals("Animals"))
							{
								System.out.println("Inside animal");
								System.out.println("Clientinfo" + clientInfo.get(this.count-1).userInput);
								choices(playerInfo.animal);
								updateClients(playerInfo);
							}
							
							if(data.category.equals("Food"))
							{
								choices(playerInfo.food);
							}
							
							if(data.category.equals("city"))
							{
								choices(playerInfo.city);
							}
							
					    }
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	//updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			public  void choices(ArrayList<String> nameHolder)
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
		        
		        //clientInfo.get(this.count-1).userInput.size(charsArray.size()); 
		        
		        //prints the dashes
		        for(Integer i =0; i < charsArray.size(); i++)
		        {
		        	clientInfo.get(this.count-1).userInput.add('_');
		        	System.out.print(" _ ");
		        }
		        
		       
		        int guesses = 6;
		        
		        Boolean flag = false;
		        
				System.out.println("\n\nEnter a character: ");
				String s = clientInfo.get(this.count-1).userletter;
				char c = s.charAt(0);
		        
				System.out.println("c: " +c);
				
		        while(clientInfo.get(this.count-1).userInput.contains('_') && guesses > 0) 
		        {
		        	//@SuppressWarnings("resource") 
					//Scanner charInputScanner = new Scanner(System.in);

					
			        for(char letter : charsArray) 
			        {
			        	if(letter == c) 
			        	{
			        		int x = charsArray.indexOf(letter);
			        		int y = charsArray.lastIndexOf(c);
			        		clientInfo.get(this.count-1).userInput.set(x,letter);
			        		clientInfo.get(this.count-1).userInput.set(y, letter);
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
		    		for(char printer : clientInfo.get(this.count-1).userInput)
		    		{
		    			System.out.print(" " + printer + " ");

		    		}
			        
			        System.out.println("  guesses left: " + guesses);
		        }
		        
		        System.out.println("\n\nBye!");
			}
			
		}//end of client thread
}


	
	

	