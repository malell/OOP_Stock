package controller;

import model.ClientModel;
import model.Company;
import model.Error;
import model.Share;
import network.*;
import view.companyDetail.CompanyInfoPanel;
import view.companyDetail.CompanyDetailPanel;
import view.companyDetail.TransactionPanel;
import view.main.CompanyRow;
import view.main.MainView;

import javax.swing.*;
import java.awt.event.*;

public class MainController implements ActionListener, KeyListener, MouseListener {
    private ClientModel client;
    private ServerComunication com;
    private MainView view;
    private ClientController controller;

    public static final String LOGOUT_COMMAND = "LOGOUT";
    public static final String BUY_COMMAND = "BUY";
    public static final String SELL_COMMAND = "SELL";
    public static final String SELLALL_COMMAND = "SELLALL";
    public static final String ADD = "ADD";
    public static final String RETURN_COMPANY_COMMAND = "RETURN_COMPANY";
    public static final String RETURN_PROFILE_COMMAND = "RETURN_PROFILE";
    public static final String COMPANYSHARE_TABLE = "TABLE";
    public static final String COMPANY_DETAILS = "DETAILS";
    public static final String SETIMAGE_COMMAND = "SET_IMAGE";
    public static final String PROFILE_COMMAND = "PROFILE";

    //TODO Join returns, maybe header should has profile user as attribute

    public MainController(ClientModel client, ServerComunication com, ClientController controller){
        this.client = client;
        this.com = com;
        this.controller = controller;
    }

    public ClientModel getClient () {
        return client;
    }

    public void setView (MainView mainView){
        this.view = mainView;
    }

    public MainView getView() {
        return view;
    }

    /**
     * Update share value of determinated company
     * @param companyName to update share value
     * @param shareValue updated
     */
    public void updateShareValue(String companyName, float shareValue) {
        client.updateShareValue(companyName, shareValue);
    }

    //We should use one controller for each window
    // TODO Controller on each company name on table
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        switch (action) {
            case LOGOUT_COMMAND:
                //Remove main view and starts init view
                view.rid();
                controller.stopClientSession();
                client = null;
                //Informs Server of log out
                LogOutRequestDTO logOutRequest = new LogOutRequestDTO();
                com.sendRequest(logOutRequest);
                break;
            /*case LOGOUT_PROFILE_COMMAND:
                profileUser.dispose();
                view.rid();
                controller.stopClientSession();
                client = null;
                //Informs Server of log out
                LogOutRequestDTO logOutRequest = new LogOutRequestDTO();
                com.sendRequest(logOutRequest);
                break;*/
            case SELLALL_COMMAND:
            case BUY_COMMAND:
            case SELL_COMMAND:
                int numShares;
                //CompanyDetailLeftPanel companyDetailLeftPanel = ((CompanyDetailPanel) view.getGlassPane()).getCompanyDetailLeftPanel();
                CompanyInfoPanel companyInfoPanel = view.getCompanyDetailPanel().getCompanyInfoPanel();
                TransactionPanel transactionPanel = view.getCompanyDetailPanel().getTransactionPanel();
                if (action.equals(SELLALL_COMMAND)) {
                    numShares = companyInfoPanel.getUserShares();
                }
                else {
                    //Check empty field of number of shares to buy/sell
                    String text = transactionPanel.getNumShares();
                    if (text.equals("")) {
                        controller.setError(Error.BLANK_FIELD);
                        System.out.println("Introdueix el número de shares que vols comprar");
                        break;
                    }
                    numShares = Integer.parseInt(text);
                }
                //Send request of transaction
                boolean transactionType = action.equals(BUY_COMMAND) ? Commons.BUY : Commons.SELL;
                String companyName = companyInfoPanel.getCompanyName();
                TransactionDTO transactionRequest = new TransactionDTO(transactionType, numShares, companyName, client.getUser().getUsername());
                com.sendRequest(transactionRequest);
                transactionPanel.setJtfShares("");
                break;
            case ADD:
                String cashIntroduced = view.getMoneyAvailablePanel().getUserCash();
                if(cashIntroduced.equals("")){
                    controller.setError(Error.BLANK_FIELD);
                    break;
                }
                float cash = Float.parseFloat(view.getMoneyAvailablePanel().getUserCash());
                view.getMoneyAvailablePanel().clearText();
                UpdateCashDTO updateCashDTO = new UpdateCashDTO(client.getUser().getUsername(),cash);
                System.out.println("ENVIAT A SERVER: Afegir " + updateCashDTO.getNewCash() + "€ al compte de " + updateCashDTO.getUsername());
                com.sendRequest(updateCashDTO);
                break;

            case RETURN_PROFILE_COMMAND:
                ProfileDTO profileDTO = new ProfileDTO(view.getProfileUser().getJlUserName(), view.getProfileUser().getJlImgUser(), view.getProfileUser().getJtDescription());
                com.sendRequest(profileDTO);
                view.getGlassPane().setVisible(false);
                view.setVisible(true);
                view.getHeaderPanel().setImage(Commons.getJlImgUser(view.getProfileUser().getJlImgUser()));
                //TODO HACERLO COMO RESPUESTA DEL SERVIDOR (CONFIRMACIÓN)
                client.getUser().setImage(view.getProfileUser().getJlImgUser());
                client.getUser().setDescription(view.getProfileUser().getJtDescription());
                break;

            case SETIMAGE_COMMAND:
                JComboBox jcSetImage = (JComboBox) actionEvent.getSource();
                String selectedItem = jcSetImage.getSelectedItem().toString();
                view.getProfileUser().setImage(selectedItem);
                client.getUser().setImage(view.getProfileUser().getJlImgUser());

        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        Object o = keyEvent.getSource();
        if (o instanceof JTextField) {
            JTextField jtfShares = (JTextField) o;
            char key = keyEvent.getKeyChar();
            if (jtfShares.getText().equals("") && key == '0') {
                jtfShares.setEditable(false);
            }
            else jtfShares.setEditable(key >= '0' && key <= '9' || key == KeyEvent.VK_BACK_SPACE);
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }
    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if (source instanceof JPanel) {
            if (source instanceof CompanyRow) {
                CompanyRow row = (CompanyRow) mouseEvent.getSource();
                Company c = getClient().getCompanyByName(row.getCompanyName());
                int userShares = 0;
                for (Share s : getClient().getUser().getShares())
                    if (s.getCompany().getName().equals(c.getName()))
                        userShares += s.getNumber();
                CompanyDetailPanel companyDetailPanel = new CompanyDetailPanel(c, userShares);
                companyDetailPanel.registerController(this);
                view.setCompanyDetailPanel(companyDetailPanel);
                view.getCards().add(companyDetailPanel, COMPANY_DETAILS);
                view.changeCard(MainController.COMPANY_DETAILS);
                view.getHeaderPanel().updateHeaderLabels(RETURN_COMPANY_COMMAND, c.getName());
                view.setVisible(false);
                view.setVisible(true);
                view.repaint();

            }
        } else if (source instanceof JLabel){
            JLabel jl_aux = (JLabel) source;
            if(jl_aux.getName().equals(PROFILE_COMMAND)){
                view.getProfileUser().updatePanel(client.getUser());
                view.getGlassPane().setVisible(true);
            }
            else if (jl_aux.getName().equals(RETURN_COMPANY_COMMAND)) {
                view.setCompanyDetailPanel(new CompanyDetailPanel());
                view.changeCard(MainController.COMPANYSHARE_TABLE);
                view.getHeaderPanel().updateHeaderLabels("", "");
                view.setVisible(false);
                view.setVisible(true);
            }

        } else if (source instanceof JTextArea){
            JTextArea jt_aux = (JTextArea) source;
            if(jt_aux.getName().equals("jtdescription") && jt_aux.getText().equals("Add a description...")){
                jt_aux.setText("");
            }

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

}
