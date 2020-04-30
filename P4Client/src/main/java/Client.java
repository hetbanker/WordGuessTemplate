import java.io.IOException; 
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    // Connection Info
    private String ipAddr;
    private int port;
    private Socket socketClient;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    // Callbacks to update the GUI
    private Consumer<Serializable> printMessage;
    private Consumer<Serializable> enableCategories;

    // Client Info
    private String category;

    public Client(String ipAddr, int port, Consumer<Serializable> printMessage, Consumer<Serializable> enableCategories) {
        this.port = port;
        this.ipAddr = ipAddr;

        this.printMessage = printMessage;
        this.enableCategories = enableCategories;
    }

    @Override
    public void run() {
        try {
            //TODO: Temporary changed ipAddr for easy testing
            socketClient= new Socket("127.0.0.1", port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {
            System.out.println("Bad Connection");
            System.exit(1);
        }

        while (true) {
            try {
                /**Letters guessed can go here*/
                GameplayController.plInfo = (PlayerInfo) in.readObject();
                printMessage.accept("");
                enableCategories.accept("");

            } catch (Exception e) { 
                System.out.println("\nSomething went wrong\n");
                e.printStackTrace();
            }
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void send(PlayerInfo info) {
        try {
            out.writeObject(info);
        } catch (IOException e) {
            System.out.println("Connection to server lost");
        }
    }
}
