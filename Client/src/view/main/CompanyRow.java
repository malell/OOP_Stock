package view.main;

import model.Company;
import model.Share;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

import static view.Typo.*;

public class CompanyRow extends JPanel {
    private String companyName;
    private JLabel jlName, jlPrice, jlChange, jlChangePerCent, jlEmpty;
    private LinkedList<JLabel[]> jlShares;

    public CompanyRow (Company c, LinkedList<Share> shares) {
        companyName = c.getName();
        jlShares = getJLShareValues(c, shares);

        setLayout(new GridLayout(jlShares.size(), 6));
        setBorder(BorderFactory.createEmptyBorder(2,0,2,0));

        float price = c.getShareValue();
        float change = price - c.getSample();

        jlName = new JLabel(companyName);
        jlName.setBorder(new EmptyBorder(0,10,0,0));
        jlPrice = new JLabel(df_cashEuro.format(price));
        jlPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        jlPrice.setBorder(new EmptyBorder(0,0,0,10));
        jlChange = new JLabel(df_cashEuro.format(change));
        jlChange.setHorizontalAlignment(SwingConstants.RIGHT);
        jlChange.setBorder(new EmptyBorder(0,0,0,10));
        jlChangePerCent = new JLabel(df_perCent.format(change/price));
        jlChangePerCent.setHorizontalAlignment(SwingConstants.RIGHT);
        jlChangePerCent.setBorder(new EmptyBorder(0,0,0,10));

        fillCompanyRow();
    }

    public String getCompanyName() {
        return companyName;
    }

    private void fillCompanyRow() {
        add(jlName);
        add(jlPrice);
        add(jlChange);
        add(jlChangePerCent);
        if (jlShares.size() == 0) {
            JLabel jl0 = new JLabel();
            jl0.setHorizontalAlignment(SwingConstants.RIGHT);
            jl0.setText("0");
            jl0.setBorder(new EmptyBorder(0,0,0,10));
            add(jl0);
            jlEmpty = new JLabel("");
            jlEmpty.setHorizontalAlignment(SwingConstants.RIGHT);
            jlEmpty.setBorder(new EmptyBorder(0,0,0,10));
            add(jlEmpty);
        }
        else {
            int i = 0;
            do {
                add(jlShares.get(i)[0]);
                add(jlShares.get(i)[1]);
                i++;
                if (i < jlShares.size()) {
                    add(new JLabel(""));
                    add(new JLabel(""));
                    add(new JLabel(""));
                    add(new JLabel(""));
                }
            } while (i < jlShares.size());
        }
    }

    private LinkedList<JLabel[]> getJLShareValues(Company c, LinkedList<Share> shares) {
        LinkedList<JLabel[]> jlShares = new LinkedList<>();
        int rowShares = 0;
        float shareValue = c.getShareValue();
        float profitLoss;
        for (Share s : shares) {
            if (companyName.equals(s.getCompany().getName())) {
                JLabel[] jlValues = new JLabel[2];
                jlValues[0] = new JLabel(Integer.toString(s.getNumber()));
                jlValues[0].setHorizontalAlignment(SwingConstants.RIGHT);
                jlValues[0].setBorder(new EmptyBorder(0,0,0,10));
                profitLoss = (shareValue - s.getPurchasePrice())/shareValue;
                JLabel jl_aux = new JLabel(df_perCent.format(profitLoss));
                jl_aux.setHorizontalAlignment(SwingConstants.RIGHT);
                jl_aux.setBorder(new EmptyBorder(0,0,0,10));
                posNegColorBy(profitLoss, jl_aux);
                jlValues[1] = jl_aux;
                jlShares.add(rowShares++, jlValues);
            }
        }
        return jlShares;
    }

    private void posNegColorBy(float parameter, JLabel jl) {
        if (parameter >= 0) jl.setForeground(Color.GREEN);
        else                jl.setForeground(Color.RED);
    }


    public void updatePriceChangeValue(Company c) {
        float price = c.getShareValue();
        float change = price - c.getSample();
        posNegColorBy(change, jlChange);
        posNegColorBy(change, jlChangePerCent);
        jlPrice.setText(df_cashEuro.format(price));
        jlChange.setText(df_cashEuro.format(change));
        jlChangePerCent.setText(df_perCent.format(change/price));
        repaint();
        setVisible(true);
    }

    public void updateProfitLossValues(Company c, LinkedList<Share> shares) {
        removeAll();
        jlShares = getJLShareValues(c, shares);
        setLayout(new GridLayout(jlShares.size(), 6));
        fillCompanyRow();
        repaint();
        setVisible(false);
        setVisible(true);
    }
}
