import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {
    // Connection info
    private String ipAddr;
    private int port;
    private Socket socketClient;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(String ipAddr, int port) {
        this.port = port;
        this.ipAddr = ipAddr;
    }

    @Override
    public void run() {
        try {
            socketClient= new Socket(ipAddr, port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {
            System.out.println("Bad Connection");
            System.exit(1);
        }

        while (true) {

        }
    }
}
