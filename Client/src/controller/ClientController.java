package controller;

import model.ClientModel;
import model.Error;
import network.ServerComunication;
import view.ErrorView;
import view.main.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientController implements ActionListener {
    private InitController initController;
    private MainController mainController;
    private ErrorView errorView;

    public static final String OK_COMMAND = "OK";

    public ClientController(InitController initController) {
        this.initController = initController;
        this.mainController = null;
        this.errorView = null;
    }

    public InitController getInitController() {
        return initController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setError (Error error) {
        errorView = new ErrorView(error);
        errorView.registerController(this);
    }

    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case OK_COMMAND:
                //Remove view
                errorView.rid();
                break;
        }
    }
    /**
     * Starts client session (main program view)
     * @param model information required to start session
     * @param com connection with server manager
     */
    public void startClientSession(ClientModel model, ServerComunication com) {
        initController.initViewVisible(false);
        //Inits the Main program View and controller
        mainController = new MainController(model, com,this);
        MainView view = new MainView(mainController);
        mainController.setView(view);
        view.registerController(mainController);

        //A partir d'aquí comença la sessió del client
    }

    /**
     *
     */
    public void stopClientSession() {
        //TODO  Per això peta moltes vegades el programa. S'han de desactivar els Threads que puguin estar utilitzant el mainController
        mainController = null;
        initController.restartInitView();
    }

    /**
     * Update share value of determinated company
     * @param companyName to update share value
     * @param shareValue updated
     */
    public void updateShareValue(String companyName, float shareValue) {
        mainController.updateShareValue(companyName, shareValue);
    }
}
