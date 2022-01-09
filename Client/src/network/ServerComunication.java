package network;

import controller.ClientController;
import controller.Commons;
import model.*;
import model.Error;

import java.io.*;
import java.net.Socket;

public class ServerComunication extends Thread {
    private boolean isOn;
    private ClientController controller;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket serverSocket;

    public ServerComunication() throws IOException {
        try {
            isOn = false;
            serverSocket = new Socket(ClientConfiguration.getServerIP(), ClientConfiguration.getServerPORT());
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            stopServerComunication();
            //Excepció llançada per poder saber a l'exterior si s'ha connectat correctament amb el Server
            //Suggerencia: crear excepcions, no només per aquest cas (ServerNotConnectedException)
            throw new IOException();
        }
    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Client starts the comunication with Server
     */
    public void startServerComunication() {
        isOn = true;
        this.start();
        System.out.println("Connected to Server");
    }

    public void stopServerComunication() {
        isOn = false;
        this.interrupt();
        System.out.println("Disconnected from Server");
    }

    public void run() {
        while(isOn) {
            try {
                Object o = in.readObject();
                manageDataReceived(o);
            }
            catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.out.println("Server disconnected");
                stopServerComunication();
            }

        }
    }

    /**
     * It detects the type of response from Server and it runs the appropiate routine
     * @param o: object received from Client
     */
    private void manageDataReceived (Object o) {
        if (o instanceof ShareValueUpdateDTO) {
            ShareValueUpdateDTO shareValueUpdateDTO = (ShareValueUpdateDTO) o;
            String name = shareValueUpdateDTO.getCompanyName();
            controller.updateShareValue(name, shareValueUpdateDTO.getShareValue());
            ClientModel client = controller.getMainController().getClient();
            Company company = client.getCompanyByName(name);
            int index = client.getIndexCompanyByName(name);
            controller.getMainController().getView().getViewCompanies().updateChangePriceValue(company, index);
            controller.getMainController().getView().getViewCompanies().updateProfitLossValues
                    (company, index, client.getUser().getShares());
            if (controller.getMainController().getView().getCompanyDetailPanel().isVisible())
                if (name.equals(controller.getMainController().getView().getCompanyDetailPanel().getCompanyName()))
                    controller.getMainController().getView().getCompanyDetailPanel().getCompanyInfoPanel().updateShareValues(shareValueUpdateDTO.getShareValue());
        }
        //TODO  Revisar ShareValueSample i New Candle
        else if (o instanceof ShareValueSampleDTO) {
            ShareValueSampleDTO shareValueSampleDTO = (ShareValueSampleDTO) o;
            ClientModel client = controller.getMainController().getClient();
            String name = shareValueSampleDTO.getCompanyName();
            Company c = client.getCompanyByName(name);
            c.setSample(shareValueSampleDTO.getShareValue());
            System.out.println(name + ": " + shareValueSampleDTO.getShareValue());
            controller.getMainController().getView().getViewCompanies().updateChangePriceValue
                    (client.getCompanyByName(name), client.getIndexCompanyByName(name));
        }
        else if (o instanceof NewCandleDTO) {
            NewCandleDTO newCandleDTO = (NewCandleDTO) o;
            Company c = controller.getMainController().getClient().getCompanyByName(newCandleDTO.getCompanyName());
            //Adds the new candle in the FIFO of candles
            Commons.addCandle(newCandleDTO.getCandle(), c.getCandles());
            //Checks if the company details of this company are displayed (CompanyDetailPanel visible)
            if (controller.getMainController().getView().getCompanyDetailPanel().isVisible())
                if (c.getName().equals(controller.getMainController().getView().getCompanyDetailPanel().getCompanyName()))
                    //If it is the case, update the candle chart panel
                    controller.getMainController().getView().getCompanyDetailPanel().updateChart(c.getCandles());
            System.out.println(newCandleDTO.getCompanyName() + ": " + newCandleDTO.getCandle() + "\n");
        }
        else if (o instanceof UserShareUpdateDTO) {
            UserShareUpdateDTO userShareUpdateDTO = (UserShareUpdateDTO) o;
            controller.getMainController().getClient().getUser().setCash(userShareUpdateDTO.getMoney());
            Company c = controller.getMainController().getClient().getCompanyByName(userShareUpdateDTO.getCompanyName());
            int index = controller.getMainController().getClient().getIndexCompanyByName(c.getName());
            for (Share s : userShareUpdateDTO.getSharesOf())
                s.setCompany(c);
            controller.getMainController().getClient().getUser().updateUserShares(c.getName(), userShareUpdateDTO.getSharesOf());
            controller.getMainController().getView().getViewCompanies().updateProfitLossValues(c, index, userShareUpdateDTO.getSharesOf());
            controller.getMainController().getView().getMoneyAvailablePanel().refreshMoney(userShareUpdateDTO.getMoney());
            if (controller.getMainController().getView().getCompanyDetailPanel().isVisible()) {
                if (userShareUpdateDTO.getCompanyName().equals(controller.getMainController().getView().getCompanyDetailPanel().getCompanyName())) {
                    int numShares = 0;
                    for (Share s : userShareUpdateDTO.getSharesOf())
                        numShares += s.getNumber();
                    controller.getMainController().getView().getCompanyDetailPanel().getCompanyInfoPanel().updateUserSharesNumber(numShares);
                }
            }
        }
        else if (o instanceof UpdateCashDTO){
            UpdateCashDTO updateCashDTO = (UpdateCashDTO) o;
            controller.getMainController().getClient().getUser().setCash(updateCashDTO.getNewCash());
            controller.getMainController().getView().getMoneyAvailablePanel().refreshMoney(updateCashDTO.getNewCash());
        }
        else if (o instanceof ErrorDTO) {
            Error error = ((ErrorDTO) o).getError();
            controller.setError(error);
        }
        else if (o instanceof ClientDTO) {
            ClientDTO clientDTO = (ClientDTO) o;
            ClientModel client = new ClientModel(clientDTO.getCompanies(), clientDTO.getUser());
            controller.startClientSession(client, this);
            /*
            for (Company c : client.getCompanies()) {
                System.out.println(c.getName() + ": " + c.getCandles());
            }
            */
        }
    }

    /**
     * Sends message to Server
     * @param message to send
     */
    public void sendRequest(DataTransferObject message) {
        try {
            out.writeObject(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Cash function
}
