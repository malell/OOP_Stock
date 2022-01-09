package controller;

import model.*;
import model.Error;
import model.db.dao.BotDAO;
import model.db.dao.CompanyDAO;
import model.db.dao.ShareDAO;
import model.db.dao.UserDAO;
import model.network.Server;
import network.ShareValueUpdateDTO;
import service.CompanyInfoCollector;
import view.*;
import service.CompanyService;
import view.bot.BotRowChangeStatus;
import view.bot.BotRowEliminateButton;
import view.bot.BotCreation;
import view.bot.BotTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

//      El problema esta en que la vista es reconstrueix fent una nova crida a la DB, per tant els objectes BOT
//      anteriors queden "descontrolats" i es superposen amb els antics
//TODO Tema bots: Ajustar la vista

public class ServerController implements ActionListener, MouseListener {

    private Server server;
    private ServerView view;
    private CompanyService service;
    private LinkedList<Bot> botsList;

    public static final String CREATE_BOT = "CREATE";
    public static final String ELIMINATE_BOT = "ELIMINATE";
    public static final String STATUS_ON = "STATUS_ON";
    public static final String STATUS_OFF = "STATUS_OFF";
    public static final String SEARCH_COMPANY = "SEARCH_COMPANY";

    public static final String TRANSACTION="TRANSACTION";

    public static final String USERSTABLE_COMMAND = "USERS TABLE";
    public static final String TOPCHART_COMMAND = "TOP CHART";
    public static final String BOTENGINE_COMMAND = "BOT ENGINE";


    public ServerController(Server server) {
        this.server = server;
        this.view = null;
    }

    /**
     * initialize the bots list getting the info from the BotDao
     */

    public void initBots() {
        botsList = BotDAO.getBots();
        for (Bot bot : botsList) {
            bot.setSc(this);
            bot.start();
        }
    }

    public void setView(ServerView view){
        this.view=view;
    }


    public void setService(CompanyService service) {
        this.service = service;
    }

    /**
     * updates the cash of the UserDao
     * @param username name of the user
     * @param transactionValue gets true or false depending on if it's sells or buys
     */

    public synchronized void modifyCash(String username, float transactionValue) {
        UserDAO.addCash(username, transactionValue);
    }


    //e.g. taula.update() --> agafara dades DB
    public synchronized void transaction(String companyName, boolean transactionType) {

        //Updating DB
        float shareValue = updateShareValueDB(companyName, transactionType);
        System.out.println(companyName + "  -  " + transactionType);
        view.getJpBuyAction().updateCompanySharePrice(companyName, shareValue);

        //updates topChart
        //view.getJpTop().getJpChart().revalidate();
        //view.getJpTop().getJpChart().repaint();
        view.getJpTop().setTopInfo();
        //view.repaint();

        //Updating all clients
        ShareValueUpdateDTO shareValueUpdate = new ShareValueUpdateDTO(companyName, shareValue);
        server.sendBroadcast(shareValueUpdate);
    }

    public synchronized float updateShareValueDB(String companyName, boolean transactionType) {
        float shareValue = CompanyDAO.getShareValue(companyName);
        float onePercent = (float) (shareValue * 0.01);
        if (transactionType == Commons.BUY) shareValue += onePercent;
        else shareValue -= onePercent;
        CompanyDAO.setShareValue(companyName, shareValue);
        //TODO Potser passar share value a la taula server

        //searchCompany();
        return shareValue;
    }

    /**
     * checks if both of the strings are on the DDBB
     * @param username name user
     * @param email email user
     * @return
     */

    public Error checkSignUpDB(String username, String email) {
        if (!UserDAO.findUserByName(username)) {
            if (!UserDAO.findUserByEmail(email)) {
                return Error.SIGNUP_OK;
            } else {
                return Error.EMAIL_EXISTS;
            }
        } else {
            return Error.USERNAME_EXISTS;
        }
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case CREATE_BOT:

                BotCreation viewBotFrame = view.getBotCreation();
                BotTable viewBotList = view.getBotTable();

                if (checkBotsParameters(viewBotFrame.getPercentatge(), viewBotFrame.getActivationTime())){
                    String item = viewBotFrame.getComboBoxItem().toString();
                    int i = CompanyDAO.getCompanyId(item);
                    Bot b = new Bot(Float.parseFloat(viewBotFrame.getPercentatge()), Float.parseFloat(viewBotFrame.getActivationTime()), true, CompanyDAO.getCompany(i));
                    BotDAO.addBot(b);
                    b.setSc(this);
                    b.start();
                    //b.resumeThread();
                    System.out.println(b);

                    botsList.add(b);
                    viewBotList.refreshBotsList(botsList);
                    viewBotList.registerControllers(this);
                    viewBotFrame.clearTextFields();
                    break;

                }
                JOptionPane.showMessageDialog(null, "Has introduit malament les dades");
                viewBotFrame.clearTextFields();
                break;

            case ELIMINATE_BOT:
                BotRowEliminateButton o=(BotRowEliminateButton) actionEvent.getSource();
                int posicioBot = o.getPosicio();
                System.out.println(posicioBot);
                System.out.println(botsList.get(posicioBot));
                BotDAO.removeBot(botsList.get(posicioBot));
                botsList.get(posicioBot).killThread();
                botsList.remove(posicioBot);
                //view.getBotTable().deleteBotRow(botsList, posicioBot);
                view.getBotTable().refreshBotsList(botsList);
                view.getBotTable().registerControllers(this);
                System.out.println("Elimíneme serdo");
                break;


            case STATUS_ON:
                BotRowChangeStatus s=(BotRowChangeStatus) actionEvent.getSource();
                int posicioBot2 = s.getPosicio();

                BotDAO.updateStatusBot(botsList.get(posicioBot2), true);
                botsList.get(posicioBot2).resumeThread();
                //view.getCbl().getBotsList().get(posicioBot2).start();
                System.out.println("sa posat on"+botsList.get(posicioBot2));
                break;


            case STATUS_OFF:
                BotRowChangeStatus s2=(BotRowChangeStatus) actionEvent.getSource();
                int posicioBot3 = s2.getPosicio();
                BotDAO.updateStatusBot(botsList.get(posicioBot3), false);

                Bot bot = botsList.get(posicioBot3);
                System.out.println("JUST ABANS off"+"   -   "+bot.getName()+"  -  "+bot.getCompany().getName()+"  -  "+bot.isStatus()+"   -   "+ bot.hashCode());
                bot.pauseThread();
                System.out.println("sa posat off"+botsList.get(posicioBot3));
                break;


            case SEARCH_COMPANY :

                LinkedList<Share> shares = ShareDAO.getUserShares(view.getJpBuyAction().getComboBoxItem().toString());
                LinkedList<String> companyTable= new LinkedList<>();

                int cont =0;
                for (int i = 0; i < shares.size(); i++) {
                    for (int j = 0; j < shares.size(); j++) {
                        if (shares.get(i).getCompany().getName().equals(shares.get(j).getCompany().getName())){
                            if (j>i){
                                cont=0;
                                break;
                            }
                            cont=cont+shares.get(j).getNumber();
                        }
                    }
                    if (cont!=0){
                        companyTable.add(shares.get(i).getCompany().getName());
                        companyTable.add(String.valueOf(cont));
                        float shareValue = CompanyDAO.getShareValue(shares.get(i).getCompany().getName());
                        companyTable.add(String.valueOf((shareValue)));
                    }
                    cont=0;
                }
                view.getJpBuyAction().createTable(companyTable);

        }

    }

    /**
     * Updates the view of the Companies when some transaction has been made
     * @param company company name
     * @param share   value of the new share
     */

    public void updateCompanyNumberShares(String company,int share){
        view.getJpBuyAction().updateCompanyNumberShare(company,share);
    }

    /**
     * gets the combobox item selected
     * @return
     */

    public String getComboBoxItemUser(){
        return view.getJpBuyAction().getComboBoxItem().toString();
    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if (source instanceof JLabel) {
            JLabel jlAux = (JLabel) source;
            switch(jlAux.getName()) {
                case USERSTABLE_COMMAND:
                case BOTENGINE_COMMAND:
                case TOPCHART_COMMAND:
                    System.out.println(jlAux.getName() + " hey");
                    view.changeCard(jlAux.getName());
                    break;
            }
            System.out.println(jlAux.getName() + " hey");
        }

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public LinkedList<Company> getCompaniesDB() {
        return CompanyDAO.getCompanies();
    }


    public LinkedList<Company> getCompanies() {
        LinkedList<Company> companies = getCompaniesDB();
        for (int i = 0; i < companies.size(); i++) {
            CompanyInfoCollector info = service.getCompanyInfoCollectors().get(i);
            //TODO  Reactivar si s'activen tots els Threads infocollectors
            companies.get(i).setCandles(info.getCandles());
            companies.get(i).setSample(info.getShareValueSamples().get(0));
        }
        return companies;
    }

     public LinkedList<Bot> getBotsDB(){
        return BotDAO.getBots();
    }

    public LinkedList<Integer> getUsersDB(){
        return UserDAO.getUsersId();
    }

    public LinkedList<String> getUsersNameDB(){
        return UserDAO.getUsersName();
    }


    /**
     * check if the parameters introduce are correct (numbers between 0 and 100)
     * @param a 1st number
     * @param b 2nd number
     * @return
     */


    public static boolean checkBotsParameters(String a, String b){
        try {
            Float.parseFloat(a);
            Float.parseFloat(b);
            if (Float.parseFloat(a)>=0 && Float.parseFloat(a)<=100) {
                return true;
            }
        } catch (NumberFormatException nfe){
            return false;
        }
        return false;
    }

}
