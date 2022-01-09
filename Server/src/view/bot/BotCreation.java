package view.bot;

import controller.ServerController;
import model.Company;
import view.SButton;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//TODO 1. Resize frame/panel when bot added to list
//TODO 2. Remove bot from view when deleted

public class BotCreation extends JPanel{

    private JComboBox<String> jcCompanies;
    private JTextField percentatge;
    private JTextField activationTime;
    private SButton jbbot;


    public BotCreation(LinkedList<Company> companies) {

        JPanel dos = new JPanel(new GridLayout(1,4));
        JPanel crearBots = new JPanel(new GridLayout(2,1));
        dos.setBackground(Color.white);

        crearBots.add(new JLabel("   Company"));
        jcCompanies = new JComboBox<>();

        for (Company company : companies) {
            jcCompanies.addItem(company.getName());
        }

        percentatge = new JTextField();
        activationTime = new JTextField();
        crearBots.add(jcCompanies);
        dos.add(crearBots);
        JPanel crearBots2 = new JPanel(new GridLayout(2,1));

        crearBots2.add(new JLabel("   Buy Percentatge"));
        crearBots2.add(percentatge);
        dos.add(crearBots2);

        JPanel crearBots3 = new JPanel(new GridLayout(2,1));

        crearBots3.add(new JLabel("   Activation Time"));
        crearBots3.add(activationTime);
        dos.add(crearBots3);

        jbbot = new SButton("Create", "white", "black");
        dos.add(jbbot);
        add(dos);

    }


    public String getActivationTime(){ return activationTime.getText(); }

    public String getPercentatge(){ return percentatge.getText(); }

    public Object getComboBoxItem(){return jcCompanies.getSelectedItem();}

    public void clearTextFields(){
        percentatge.setText("");
        activationTime.setText("");
    }

    public void clearActivationTime(){;}

    public void registerController(ServerController serverController){
        jbbot.setActionCommand(ServerController.CREATE_BOT);
        jbbot.addActionListener(serverController);

    }

}
