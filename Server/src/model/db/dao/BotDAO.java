package model.db.dao;

import controller.ServerController;
import model.Bot;
import model.db.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class BotDAO {

    public static void addBot(Bot b){
        String query = "INSERT INTO Bot(idcompany, buyprob, activetime, state) VALUES (" + CompanyDAO.getCompanyId(b.getCompany().getName())
                + ", " + b.getProbability() + ", " + b.getTimeSleep() + ", " + b.isStatus() + ");";
        System.out.println(query);
        DBConnector.getInstance().insertQuery(query);
    }

    public static LinkedList<Bot> getBots(){
        LinkedList<Bot> bots = new LinkedList<>();
        String query = "SELECT * FROM Bot;";
        ResultSet response = DBConnector.getInstance().selectQuery(query);
        try{
            while(response.next()){
                int id =  response.getInt("idcompany");
                float buyprob = response.getFloat("buyprob");
                float active = response.getFloat("activetime");
                boolean state = response.getBoolean("state");
                bots.add(new Bot(buyprob, active, state, CompanyDAO.getCompany(id)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bots;
    }

    public static void removeBot(Bot b){
        int idcompany = CompanyDAO.getCompanyId(b.getCompany().getName());
        int state = b.isStatus() ? 1 : 0;
        String query = "DELETE FROM Bot WHERE idcompany = " + idcompany + " AND buyprob = " + b.getProbability() + " AND activetime = " + b.getTimeSleep() + " AND state = " + state + ";";
        System.out.println(query);
        DBConnector.getInstance().deleteQuery(query);
    }

    public static void updateStatusBot(Bot b, boolean status){
        int idcompany = CompanyDAO.getCompanyId(b.getCompany().getName());
        int botState = b.isStatus() ? 1 : 0;
        int state = status ? 1 : 0;

        String query = "UPDATE Bot SET state = " + state + " WHERE idcompany = " + idcompany + " AND buyprob = " + b.getProbability() + " AND activetime = " + b.getTimeSleep() + " AND state = " + botState + ";";
        System.out.println(query);
        DBConnector.getInstance().updateQuery(query);
    }
}
