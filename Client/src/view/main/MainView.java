package view.main;

import com.sun.tools.javac.Main;
import controller.Commons;
import controller.MainController;
import model.Company;
import model.User;
import view.HeaderPanel;
import view.ProfileUser;
import view.companyDetail.CompanyDetailPanel;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static controller.MainController.COMPANYSHARE_TABLE;

public class MainView extends JFrame {
    private HeaderPanel headerPanel;
    private MoneyAvailablePanel moneyAvailablePanel;

    private CompanyDetailPanel companyDetailPanel;
    private ViewCompanies viewCompanies;
    private ProfileUser profileUser;
    private JPanel cards;
    private CardLayout cardLayout;

    private MainController controller;

    public MainView(MainController mainController) {
        this.controller = mainController;

        this.getContentPane().setLayout (new BorderLayout());
        this.getContentPane().setBackground(Color.white);

        Image icon = Toolkit.getDefaultToolkit().getImage("Client/Images/s_logo.png");
        setIconImage(icon);

        User user = controller.getClient().getUser();

        headerPanel = new HeaderPanel(user);
        headerPanel.setImage(Commons.getJlImgUser(user.getImage()));
        headerPanel.setBorder(new EmptyBorder(0,0,15,0));
        this.getContentPane().add(headerPanel,BorderLayout.NORTH);

        viewCompanies = new ViewCompanies(mainController.getClient().getCompanies(), mainController.getClient().getUser().getShares());
        viewCompanies.setBackground(Color.white);
        viewCompanies.setBorder(new EmptyBorder(0,20,0,20));
        viewCompanies.setVisible(true);

        companyDetailPanel = new CompanyDetailPanel();

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(viewCompanies, COMPANYSHARE_TABLE);
        cards.add(companyDetailPanel, MainController.COMPANY_DETAILS);
        this.getContentPane().add(cards, BorderLayout.CENTER);
        ((CardLayout)cards.getLayout()).show(cards, COMPANYSHARE_TABLE);
        //cardLayout.show(cards, COMPANYSHARE_TABLE);

        profileUser = new ProfileUser();
        setGlassPane(profileUser);
        getGlassPane().setVisible(false);

        moneyAvailablePanel = new MoneyAvailablePanel(user.getCash());
        moneyAvailablePanel.setBorder(new EmptyBorder(15,0,15,0));
        this.getContentPane().add(moneyAvailablePanel,BorderLayout.SOUTH);

        setSize(1280,720);
        setTitle("Stock");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    public void registerController(MainController controller) {
        headerPanel.registerController(controller);
        moneyAvailablePanel.registerController(controller);
        viewCompanies.registerController(controller);
        profileUser.registerController(controller);
    }

    public void rid() {
        this.dispose();
    }

    public void changeCard(String name) {
        cardLayout.show(cards, name);
        cards.repaint();
        cards.setVisible(false);
        cards.setVisible(true);
    }

    public MoneyAvailablePanel getMoneyAvailablePanel(){
        return moneyAvailablePanel;
    }

    public ViewCompanies getViewCompanies() {
        return viewCompanies;
    }

    public CompanyDetailPanel getCompanyDetailPanel() {
        return companyDetailPanel;
    }

    public void setCompanyDetailPanel(CompanyDetailPanel companyDetailPanel) {
        this.companyDetailPanel = companyDetailPanel;
    }

    public JPanel getCards() {
        return cards;
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public ProfileUser getProfileUser() {
        return profileUser;
    }
}
