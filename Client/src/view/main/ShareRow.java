package view.main;

import model.Company;
import view.SButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ShareRow extends JPanel {
    private JLabel jlCompanyName;
    private JLabel jlCompanyValue;
    private JLabel jlNumShares;
    private JLabel changeRate;
    private SButton erase = new SButton("Sell All", "white", "red");


    public ShareRow (Company c){
        jlCompanyName = new JLabel(c.getName());
        jlCompanyName.setHorizontalAlignment(SwingConstants.RIGHT);
        jlCompanyName.setBorder(new EmptyBorder(0,0,0,10));
        jlCompanyValue = new JLabel(String.valueOf(c.getShareValue()));
        jlCompanyValue.setHorizontalAlignment(SwingConstants.RIGHT);
        jlCompanyValue.setBorder(new EmptyBorder(0,0,0,10));
        jlNumShares = new JLabel(String.valueOf(c.getnShares()));
        jlNumShares.setHorizontalAlignment(SwingConstants.RIGHT);
        jlNumShares.setBorder(new EmptyBorder(0,0,0,10));

        this.setLayout(new GridLayout(1, 5));
        this.add(jlCompanyName);
        this.add(jlCompanyValue);
        this.add(jlNumShares);
        //TODO add change rate
            this.add(new JLabel("-3.32%"));
        this.add(erase);
    }

    public void setJlCompanyValue(float value){
        this.jlCompanyValue.setText(String.valueOf(value));
    }

    public void setJlNumShares(int num){
        this.jlNumShares.setText(String.valueOf(num));
    }

    public void registerController(ActionListener actionListener){
        erase.addActionListener(actionListener);
    }
}
