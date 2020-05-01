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

							PlayerInfo currentInfo = clientInfo.get(this.count-1);

							/**If the Number of Guesses if 6 (i.e. new category was chosen)
							 * The Server will now chose a random word that the server needs to 
							 * guess.
							*/
							if(currentInfo.numOfGuesses == 7)
							{
								switch(currentInfo.category)
								{
									case "Animals":	currentInfo.word2Guess = GameLogic.getRandomWord(currentInfo.animal);
													break;
									case "Food":	currentInfo.word2Guess = GameLogic.getRandomWord(currentInfo.food);
													break;
									case "Cities":	currentInfo.word2Guess = GameLogic.getRandomWord(currentInfo.city);
													break;
								}
								/**Send out the information so the player knows what's needed next*/
								currentInfo.backForthMessage = "New Word From "+ currentInfo.category+" was chosen, please proceed";
								currentInfo.setWord2Guess(currentInfo.word2Guess);
								clients.get(this.count-1).out.writeObject(currentInfo);
							}
							else
							{
								clients.get(this.count-1).out.writeObject(currentInfo);
							}

							callback.accept("");


							/**TODO: Debugging Purposes 
							data.setCategory("ITS Working Bruh");
							clients.get(this.count-1).out.writeObject(data);
							*/
						}//End of the Try Statement
						
					    catch(Exception e) {
					    	break;
					    }
					}

				}//end of run
			
		}//end of client thread
}


	
	

	