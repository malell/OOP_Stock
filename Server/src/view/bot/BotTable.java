package view.bot;

import controller.ServerController;
import model.Bot;
import model.Company;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class BotTable extends JPanel {


    private LinkedList<BotRow> botsRows;
    private JPanel jpTable2;
    private ServerController sc;

    public BotTable(LinkedList<Bot> botsList) {
        botsRows = new LinkedList<>();
        JPanel jpTable = new JPanel(new GridLayout(1,6));
        jpTable.setBackground(Color.gray);

        jpTable.add(new Label("   Id"));
        jpTable.add(new Label("Company"));
        jpTable.add(new Label("Buy Percentatge"));
        jpTable.add(new Label("Activation Time"));
        jpTable.add(new Label("            Status"));
        jpTable.add(new Label("     Delete Bot"));

        //jpTable2 = new JPanel(new FlowLayout());
        jpTable2 = new JPanel();
        jpTable2.setLayout(new BoxLayout(jpTable2, BoxLayout.Y_AXIS));

        /*
        jpTable2.removeAll();
        jpTable2.revalidate();
        jpTable2.repaint();
         */

        JScrollPane jspList = new JScrollPane(jpTable2);

        generateBotsList(botsList);

        setLayout(new BorderLayout());
        add(jspList, BorderLayout.CENTER);
        add(jpTable, BorderLayout.NORTH);
        this.repaint();
    }

    public void setTable(LinkedList<Bot> botsList){
        generateBotsList(botsList);
    }

    private void generateBotsList(LinkedList<Bot> botsList) {
        botsRows = new LinkedList<>();
        for (int i = 0; i < botsList.size(); i++) {
            Bot bot = botsList.get(i);
            Company c = bot.getCompany();
            BotRow botRow = new BotRow("Bot "+i, c.getName(), String.valueOf(bot.getProbability()), String.valueOf(bot.getTimeSleep()), bot.isStatus(), i);
            botRow.registerController(sc);
            jpTable2.add(botRow);
            botsRows.add(botRow);
        }
    }

    public void refreshBotsList(LinkedList<Bot> bots){
        botsRows.clear();
        jpTable2.removeAll();
        generateBotsList(bots);
        revalidate();
        jpTable2.repaint();
        setVisible(true);
    }


    public void registerControllers(ServerController sc) {
        for (BotRow br : botsRows) {
            br.registerController(sc);
        }
    }

    public void deleteBotRow(LinkedList<Bot> bots, int pos){
        botsRows.remove(pos);
        for (int i = 0; i < botsRows.size(); i++) {
            botsRows.get(i).getbEliminate().setPosicio(i);
        }
        refreshBotsList(bots);
    }

    public void startBots(){

    }


}