package view;

import controller.ServerController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.LinkedList;

public class BuyAction extends JPanel {

    private JComboBox<String> jcUsers;
    private JButton jbsearch;
    private LinkedList<String> users;
    private JTable jtCompanies;
    private DefaultTableModel model;


    public BuyAction(LinkedList<String> users){

        this.users = users;
        jcUsers = new JComboBox<>();

        JLabel jltitle = new JLabel("Accions Comprades");

        for (String user : users) {
            jcUsers.addItem(user.toString());
        }
        JPanel jp = new JPanel(new GridLayout(1,6));
        add(jltitle,BorderLayout.NORTH);

        jbsearch = new JButton("Select");

        jp.add(jcUsers, BorderLayout.NORTH);
        jp.add(jbsearch, BorderLayout.NORTH);

        model = new DefaultTableModel();

        jtCompanies = new JTable(model);

        JTableHeader tableHeader = jtCompanies.getTableHeader();
        tableHeader.setBackground(Color.darkGray);
        tableHeader.setForeground(Color.white);

        model.addColumn("Company");
        model.addColumn("Shares");
        model.addColumn("Share Price");
        model.addColumn("Total Value");
        JScrollPane spCompanies = new JScrollPane(jtCompanies);
        add(spCompanies, BorderLayout.CENTER);

        add(jp, BorderLayout.NORTH);

        setVisible(true);
        setPreferredSize(new Dimension(1000, 200));


    }

    public void registerController(ServerController sc) {
        jbsearch.setActionCommand(ServerController.SEARCH_COMPANY);
        jbsearch.addActionListener(sc);
    }


    public void createTable(LinkedList<String> companies){

        if (jtCompanies.getRowCount()!=0){
            model.setRowCount(0);
        }

        for (int i = 0; i < companies.size(); i++) {
            float d = Integer.parseInt(companies.get(i+1))*Float.parseFloat(companies.get(i+2));
            model.addRow(new Object[]{companies.get(i),companies.get(i+1),companies.get(i+2),d});
            i=i+2;
        }

        setVisible(true);

    }

    public Object getComboBoxItem(){return jcUsers.getSelectedItem();}


    public void updateCompanySharePrice(String companyName, float shareValue) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String value = jtCompanies.getModel().getValueAt(i, 0).toString();
            if (value.equals(companyName)){
                int share = Integer.parseInt(model.getValueAt(i,1).toString());
                float totalValue = shareValue*share;
                model.setValueAt(shareValue,i,2);
                model.setValueAt(totalValue,i,3);

            }

        }

    }

    public void updateCompanyNumberShare(String companyName, int share) {

        for (int i = 0; i < model.getRowCount(); i++) {
            String value = jtCompanies.getModel().getValueAt(i, 0).toString();
            if (value.equals(companyName)){
                model.setValueAt(share,i,1);
            }

        }

    }
}
