import java.net.*;
import java.io.*;

public class ClientSocket {
    private int PORT;
    private String HOST_NAME;
    private Socket socket;
    private ObjectOutputStream objectOut = null;
    private ObjectInputStream objectIn = null;

    public ClientSocket(String hostName, int port) {
        this.HOST_NAME = hostName;
        this.PORT = port;
    }

    public void connectToServer() throws UnknownHostException, IOException, IllegalArgumentException {
        this.socket = new Socket(this.HOST_NAME, this.PORT);
    }

    public void sendObject(Object object) throws IOException, NullPointerException {
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeObject(object);
    }

    public Object reciveObject() throws IOException, ClassNotFoundException, NullPointerException {
        if (objectIn == null) {
            objectIn = new ObjectInputStream(this.socket.getInputStream());
        }
        return objectIn.readObject();
    }

    public int getPortNo() {
        return this.PORT;
    }

    public String getHostName() {
        return this.HOST_NAME;
    }
}
