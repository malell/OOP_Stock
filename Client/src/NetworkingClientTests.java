import model.ClientConfiguration;
import network.ServerComunication;

import java.io.IOException;

public class NetworkingClientTests {

    public static void main(String[] args) {
        new ClientConfiguration();
        try {
            ServerComunication serverCom = new ServerComunication();
            serverCom.startServerComunication();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
