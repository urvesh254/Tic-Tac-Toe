import java.net.*;
import java.io.*;

public class Networking {
    protected int PORT;
    protected String HOST_NAME;
    protected Socket socket;
    protected ObjectOutputStream objectOut;
    protected ObjectInputStream objectIn;

    protected Networking(String hostName, int port) {
        this.HOST_NAME = hostName;
        this.PORT = port;
    }

    public void sendMessage(Object object) throws IOException, NullPointerException {
        objectOut.writeObject(object);
    }

    public Object reciveMessage() throws IOException, ClassNotFoundException, NullPointerException {
        Object object;
        object = objectIn.readObject();
        return object;
    }

    public int getPortNo() {
        return this.PORT;
    }

    public String getHostName() {
        return this.HOST_NAME;
    }
}
