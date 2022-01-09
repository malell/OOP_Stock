package view;

import javax.swing.*;
import java.awt.*;

public class ActionRow extends JPanel {

    private int pos;
    private String companyName;
    private String sharePrice;


    public ActionRow(String companyName, String share, String sharePrice, String totalValue, int pos ){
        this.companyName=companyName;
        this.sharePrice=sharePrice;
        setLayout(new GridLayout(1,4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK)));
        this.add(new Label(companyName));
        this.add(new Label(share));
        this.add(new Label(String.valueOf(sharePrice)));
        this.add(new Label(String.valueOf(totalValue)));

    }

    public int getPos(){
        return pos;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSharePrice() {
        return sharePrice;
    }
}
