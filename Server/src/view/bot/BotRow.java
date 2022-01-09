package view.bot;

import controller.ServerController;

import javax.swing.*;
import java.awt.*;

public class BotRow extends JPanel {
    private BotRowEliminateButton bEliminate;
    private JRadioButton jrb, jrb2;


    public BotRow(String botName, String companyName, String probability, String time, boolean status, int posicio) {

        setLayout(new GridLayout(1,6));
        setBackground(Color.white);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK)));
        this.add(new Label(botName));
        this.add(new Label(companyName));
        this.add(new Label(String.valueOf(probability)));
        this.add(new Label(String.valueOf(time)));
        ButtonGroup group = new ButtonGroup();
        jrb = new BotRowChangeStatus(posicio,"ON");
        jrb.setBackground(Color.white);
        jrb2 = new BotRowChangeStatus(posicio,"OFF");
        jrb2.setBackground(Color.white);
        if (status){
            jrb.setSelected(true);
        }else{
            jrb2.setSelected(true);
        }
        group.add(jrb);
        group.add(jrb2);
        JPanel jpRadio = new JPanel(new GridLayout(1,2));
        jpRadio.add(jrb);
        jpRadio.add(jrb2);
        this.add(jpRadio);

        bEliminate = new BotRowEliminateButton(posicio,"Delete");
        bEliminate.setBackground(Color.red);
        bEliminate.setForeground(Color.white);
        this.add(bEliminate);

    }


    public void registerController(ServerController sc) {
        bEliminate.setActionCommand(ServerController.ELIMINATE_BOT);
        bEliminate.addActionListener(sc);
        jrb.setActionCommand(ServerController.STATUS_ON);
        jrb2.setActionCommand(ServerController.STATUS_OFF);
        jrb.addActionListener(sc);
        jrb2.addActionListener(sc);

    }

    public BotRowEliminateButton getbEliminate() {
        return bEliminate;
    }
}
