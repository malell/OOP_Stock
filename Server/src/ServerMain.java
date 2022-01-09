import controller.ServerController;
import model.Bot;
import model.Company;
import model.db.dao.CompanyDAO;
import model.network.Server;
import model.ServerConfiguration;
import service.CompanyService;
import view.ServerView;

import java.util.LinkedList;

public class ServerMain {

    public static void main(String[] args) {

        System.out.println("This is Server program");
        //Reads config.json for connections with Client and DB
        new ServerConfiguration();
        //Starts Server

        Server server = new Server();
        ServerController controller = new ServerController(server);
        ServerView serverView = new ServerView(controller);
        serverView.registerController(controller);
        serverView.setVisible(true);
        controller.setView(serverView);
        controller.initBots();

        server.setController(controller);
        server.startServer();

        CompanyService companyService = new CompanyService(controller, server);
        controller.setService(companyService);
    }
}