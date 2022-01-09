package model.db;

import model.db.dao.UserDAO;
import model.ServerConfiguration;

public class DAOTests {

    public static void main(String[] args) {
        new ServerConfiguration();
        //User u = new User("Jaume", "jose@gmail.com", "kedise",10000);
        //UserDAO.addUser(u);
        System.out.println(UserDAO.findUserByEmail("jo@gmail.com"));
    }
}
