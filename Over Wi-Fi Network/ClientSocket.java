import java.net.*;
import java.io.*;

public class ClientSocket extends Networking {
    public ClientSocket(String hostName, int port) {
        super(hostName, port);
    }

    public void connectToServer() throws UnknownHostException, IOException, IllegalArgumentException {
        socket = new Socket(this.HOST_NAME, this.PORT);
        objectIn = new ObjectInputStream(socket.getInputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());
    }

}
