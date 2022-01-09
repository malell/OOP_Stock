package view.companyDetail;

import controller.MainController;
import model.Candle;
import model.Company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class CompanyDetailPanel extends JPanel {
    private String companyName;
    private CompanyInfoPanel companyInfoPanel;
    private TransactionPanel transactionPanel;
    private CandleChart candleChart;

    //TODO Return button not shown
    //TODO Sometimes numshares shown are not from the user (Try colegarre and sell apple (it shows 9999 shares))
    //TODO Once clicked return, how does it shows the mainview?

    public CompanyDetailPanel() {
        super();
        this.companyName = "";
    }

    public CompanyDetailPanel(Company company, int userShares){
        this.companyName = company.getName();

        setBackground(Color.white);

        //this.setLayout (new GridBagLayout());
        setLayout(new BorderLayout());

        JPanel jpWest = new JPanel(new BorderLayout());
        companyInfoPanel = new CompanyInfoPanel(company, userShares);
        companyInfoPanel.setBorder(new EmptyBorder(5,25,10,10));
        jpWest.add(companyInfoPanel, BorderLayout.CENTER);

        transactionPanel = new TransactionPanel();
        jpWest.add(transactionPanel, BorderLayout.SOUTH);
        add(jpWest, BorderLayout.WEST);

        candleChart = new CandleChart(company.getCandles());
        candleChart.setBorder(BorderFactory.createEmptyBorder(5,10,10,25));
        add(candleChart, BorderLayout.CENTER);


    }

    public void registerController(MainController controller) {
        transactionPanel.registerController(controller);
    }

    public CompanyInfoPanel getCompanyInfoPanel() {
        return companyInfoPanel;
    }

    public TransactionPanel getTransactionPanel() {
        return transactionPanel;
    }

    public CandleChart getCandleChart() {return candleChart; }

    public void updateChart(ArrayList<Candle> candles) {
        candleChart.updateChart(candles);

    }

    public String getCompanyName() {
        return companyName;
    }
}
