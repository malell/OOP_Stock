package view;

import controller.Commons;
import controller.ServerController;
import model.User;

import javax.swing.*;
import java.awt.*;

import static controller.ServerController.*;
import static view.Typo.fontBoldLarge;
import static view.Typo.fontBoldMedium;

public class HeaderPanel extends JPanel {
    private JLabel jlUsersTable, jlTopChart, jlBotEngine;
    private JLabel jlImgLogo;

    public HeaderPanel() {
        //setLayout (new GridBagLayout());
        setLayout(new BorderLayout());
        setBackground(Color.white);

        //Stock logo image
        JPanel jpWest = new JPanel();
        jpWest.setBackground(Color.WHITE);
        jpWest.setBorder(BorderFactory.createEmptyBorder(30,50,10,0));
        jlImgLogo = new JLabel();
        jlImgLogo.setIcon(new ImageIcon("Client/Images/stock_client.png"));
        jlImgLogo.setHorizontalAlignment(SwingConstants.CENTER);
        jlImgLogo.setVerticalAlignment(SwingConstants.CENTER);
        jpWest.add(jlImgLogo);
        add(jpWest, BorderLayout.CENTER);

        //Menu
        JPanel jpSouth = new JPanel(new GridLayout(1, 3));

        jlUsersTable = new JLabel("USERS TABLE");
        jlUsersTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jlUsersTable.setFont(fontBoldLarge);
        jlUsersTable.setHorizontalAlignment(SwingConstants.CENTER);
        jlUsersTable.setVerticalAlignment(SwingConstants.CENTER);
        jpSouth.add(jlUsersTable);

        jlTopChart = new JLabel("TOP CHART");
        jlTopChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jlTopChart.setFont(fontBoldLarge);
        jlTopChart.setHorizontalAlignment(SwingConstants.CENTER);
        jlTopChart.setVerticalAlignment(SwingConstants.CENTER);
        jpSouth.add(jlTopChart);

        jlBotEngine = new JLabel("BOTS ENGINE");
        jlBotEngine.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jlBotEngine.setFont(fontBoldLarge);
        jlBotEngine.setHorizontalAlignment(SwingConstants.CENTER);
        jlBotEngine.setVerticalAlignment(SwingConstants.CENTER);
        jpSouth.add(jlBotEngine);

        add(jpSouth, BorderLayout.SOUTH);
    }

    public void registerController(ServerController controller) {
        jlUsersTable.setName(USERSTABLE_COMMAND);
        jlUsersTable.addMouseListener(controller);
        jlBotEngine.setName(BOTENGINE_COMMAND);
        jlBotEngine.addMouseListener(controller);
        jlTopChart.setName(TOPCHART_COMMAND);
        jlTopChart.addMouseListener(controller);
    }


}
