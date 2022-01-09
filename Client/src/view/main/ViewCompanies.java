package view.main;

import controller.MainController;
import model.ClientModel;
import model.Company;
import model.Share;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ViewCompanies extends JPanel {
    private final JLabel companies = new JLabel("Companies");
    private final JLabel instruction = new JLabel("Click on company name to see more options:");

    private final String[] columns = {"Company", "Price", "Change (5min)", "%Change (5min)", "numShares", "%Profit/Loss"};
    private ArrayList<CompanyRow> companyRows;
    private JPanel jpList;
    private JLabel jlHeader;

    public ViewCompanies (LinkedList<Company> comps, LinkedList<Share> shares){
        companyRows = new ArrayList<>();

        this.setLayout(new BorderLayout());

        JPanel jpHeaderList = new JPanel(new GridLayout(1, columns.length));

        jpList = new JPanel(new GridLayout(comps.size(), 1));
        jpHeaderList.setBackground(Color.black);
        for (String str : columns) {
            jlHeader = new JLabel(str);
            jlHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            jlHeader.setForeground(Color.white);
            jlHeader.setHorizontalAlignment(SwingConstants.CENTER);
            jpHeaderList.add(jlHeader);
            jpHeaderList.setBorder(new EmptyBorder(10, 0, 10, 0));
            this.add(jpHeaderList, BorderLayout.NORTH);
        }

        JPanel jpList = new JPanel();
        jpList.setLayout(new BoxLayout(jpList, BoxLayout.Y_AXIS));
        int num = 0;
        for (Company c : comps) {
            CompanyRow row = new CompanyRow(c, shares);
            row.setBorder(new EmptyBorder(10, 0, 10, 0));
            if(num%2==0)    row.setBackground(Color.decode("#E8E8E8"));
            else            row.setBackground(Color.white);
            companyRows.add(row);
            jpList.add(row);
            num++;
        }

        JScrollPane jsTable = new JScrollPane(jpList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsTable.getVerticalScrollBar().setUnitIncrement(16);
        UIManager.put("ScrollBar.thumb",new ColorUIResource(Color.decode("#A9A9A9")));
        jsTable.getVerticalScrollBar().setUI(new BasicScrollBarUI());

        this.add(jsTable, BorderLayout.CENTER);

    }

    public void updateChangePriceValue(Company c, int index) {
        companyRows.get(index).updatePriceChangeValue(c);
    }

    public void updateProfitLossValues(Company c, int iCompany, LinkedList<Share> shares) {
        companyRows.get(iCompany).updateProfitLossValues(c, shares);
    }

    public void registerController(MainController mainController){
        for (CompanyRow cRow : companyRows)
            cRow.addMouseListener(mainController);
    }

}
