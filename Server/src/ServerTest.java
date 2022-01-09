import controller.ServerController;
import model.Bot;
import model.Company;
import model.ServerConfiguration;
import model.db.dao.CompanyDAO;
import model.network.Server;
import service.CompanyService;
import view.BuyAction;
import view.ServerView;

import java.util.LinkedList;

public class ServerTest {

    public static void main(String[] args) {
        System.out.println("This is Server program");
        //Reads config.json for connections with Client and DB
        new ServerConfiguration();
        //Starts Server
/*
        Server server = new Server();
        ServerView serverView = new ServerView();
        ServerController controller = new ServerController(server, serverView);

        BuyAction buyAction = new BuyAction(controller);
        buyAction.registerController(controller);
        controller.setBuyActionView(buyAction);


        server.setController(controller);
        server.startServer();
        serverView.initialize(controller, controller.getBotsDB());
        serverView.registerController(controller);
        serverView.setVisible(true);

 */



        /*CompanyService companyService = new CompanyService(controller, server);
        controller.setService(companyService);

        Bot bot0 = new Bot((float)50.0, (float)0.5, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId("Google")), controller);
        bot0.start();
        Bot bot1 = new Bot((float)52.0, (float)0.75, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId("Facebook Inc")), controller);
        bot1.start();
        Bot bot2 = new Bot((float)50.0, (float)1, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId("Inditex")), controller);
        bot2.start();
        Bot bot3 = new Bot((float)50.0, (float)1.25, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId("Monstruos S.A")), controller);
        bot3.start();
        Bot bot4 = new Bot((float)50.0, (float)1.5, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId("Dentomedic")), controller);
        bot4.start();

        LinkedList<Company> companies = controller.getCompaniesDB();
        LinkedList<Bot> bots = new LinkedList<>();
        float i = (float) 0.01;
        for (int j = 0; j < 4; j++) {
            for (Company c : companies) {
                Bot bot = new Bot((float)50.0, (float)i, true, CompanyDAO.getCompany(CompanyDAO.getCompanyId(c.getName())), controller);
                bot.start();
                bots.add(bot);
                i = (float) (i + 0.1);
            }
        }*/
    }
}
