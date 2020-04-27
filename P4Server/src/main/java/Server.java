import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
			
			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					 t.out.reset();
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				//updateClients("new client on server: client #"+count);
					
				 while(true) {
					    try {
							//String data = in.readObject().toString();
							//callback.accept(data);

							//Get PlayerInfo .in
							PlayerInfo data = (PlayerInfo)in.readObject();
							data.clientNum = clientInfo.get(this.count-1).clientNum;
							clientInfo.set(this.count-1, data);
							clientInfo.get(0).outString ="HELLO";

							clientInfo.clear();

							System.out.println("LOOK! "+clientInfo.get(count-1).outString);
							System.out.println("LOOK! "+data.outString);
							//Located the position in clientInfo using shadowed count
							callback.accept("");
							//Update variables accordingly

							//updateClients("client #"+count+" said: "+data);
							//TODO: Write the rest of the log for the server
					    	
					    	}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	//updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	
