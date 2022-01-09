package service;

import controller.ServerController;
import model.Company;
import model.db.dao.CompanyDAO;
import model.network.Server;
import network.DataTransferObject;

import java.util.LinkedList;

public class CompanyService {
    private LinkedList<CompanyInfoCollector> companyInfoCollectors;
    private ServerController controller;
    private Server server;

    public CompanyService(ServerController controller, Server server) {
        this.controller = controller;
        this.server = server;
        companyInfoCollectors = new LinkedList<>();
        LinkedList<Company> companies = this.controller.getCompaniesDB();

        //TODO  Inicialitzar ClientDTO correctament modificant getCompanies() de ServerController
        for (Company c : companies) {
            CompanyInfoCollector infoCollector = new CompanyInfoCollector(c.getName(), c.getShareValue(), this);
            companyInfoCollectors.add(infoCollector);
        }

        //CompanyInfoCollector infoCollector = new CompanyInfoCollector("Samsung", this);
        //companyInfoCollectors.add(infoCollector);

    }

    public void sendBroadcast(DataTransferObject message) {
        server.sendBroadcast(message);
    }

    public float getShareValue(String companyName) {
        return CompanyDAO.getShareValue(companyName);
    }

    public LinkedList<CompanyInfoCollector> getCompanyInfoCollectors() {
        return companyInfoCollectors;
    }

}
