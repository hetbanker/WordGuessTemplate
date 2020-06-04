import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.util.Duration;


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
 	       gameStatus.accept("Clients Connected: "+(count-1));
			
		    while(true) {
					ClientThread c = new ClientThread(mysocket.accept(), count);
					gameStatus.accept("!!!! client has connected to server: " + "client #" + count);
					clientInfo.add(new PlayerInfo(count));
					clients.add(c);
					count++;
					c.start();

					callback.accept("");

					PauseTransition nPause = new PauseTransition(new Duration(2000));

					nPause.setOnFinished(e->{
						gameStatus.accept("Clients Connected: "+(count -1));
					});
					nPause.play();
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread
		{

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
								
				 while(true) {
					    try {

							
							PlayerInfo data = (PlayerInfo)in.readObject();
							data.setClientNum(clientInfo.get(this.count-1).clientNum);
							clientInfo.set(this.count-1, data);

							PlayerInfo currentInfo = clientInfo.get(this.count-1);


							if(currentInfo.numOfGuesses == 6)
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


						}//End of the Try Statement
						
					    catch(Exception e) {
					    	break;
					    }
					}

				}//end of run
			
		}//end of client thread
}


	
	

	