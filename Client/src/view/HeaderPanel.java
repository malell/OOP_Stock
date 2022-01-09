package view;

import controller.Commons;
import controller.MainController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static controller.MainController.PROFILE_COMMAND;
import static view.Typo.fontBoldLarge;
import static view.Typo.fontBoldMedium;

public class HeaderPanel extends JPanel {
    private JLabel jlImgLogo;
    private JLabel jlImgUser;
    private JLabel jlCompanyName;

    public HeaderPanel(User user) {
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
        add(jpWest, BorderLayout.WEST);

        //Company name (visible when company details required)
        JPanel jpName = new JPanel();
        jlCompanyName = new JLabel("");
        jlCompanyName.setBorder(BorderFactory.createEmptyBorder(35,0,0,0));
        jlCompanyName.setFont(fontBoldLarge);
        jlCompanyName.setHorizontalAlignment(SwingConstants.CENTER);
        jpName.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        jpName.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
        jlCompanyName.setVerticalAlignment(SwingConstants.BOTTOM);
        jpName.add(jlCompanyName);
        jpName.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        jpName.setBackground(Color.WHITE);
        add(jpName, BorderLayout.CENTER);

        //User image and username Panel
        //Image
        JPanel jpEast = new JPanel(new BorderLayout());
        jpEast.setBackground(Color.WHITE);
        jpEast.setBorder(BorderFactory.createEmptyBorder(30,0,10,50));
        jlImgUser = new JLabel(new ImageIcon("Client/Images/"+ Commons.getJlImgUser(user.getImage()) + ".png"));
        jlImgUser.setHorizontalAlignment(SwingConstants.CENTER);
        jlImgUser.setVerticalAlignment(SwingConstants.CENTER);
        jpEast.add(jlImgUser, BorderLayout.CENTER);
        //Username
        JLabel jlUsername = new JLabel(user.getUsername());
        jlUsername.setFont(fontBoldMedium);
        jlUsername.setHorizontalAlignment(JLabel.CENTER);
        jlUsername.setVerticalAlignment(JLabel.CENTER);
        jlUsername.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
        jpEast.add(jlUsername, BorderLayout.WEST);
        add(jpEast, BorderLayout.EAST);

/*
        jlImgLogo = new JLabel(" ");
        ImageIcon image = new ImageIcon("Client/Images/stock_client.png");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add (jlImgLogo, constraints);
        jlImgLogo.setIcon(image);
        jlImgLogo.setVisible(true);

        jlNorth = new JLabel();
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        jlNorth.setText(user.getUsername());
        jlNorth.setFont(new Font (Font.SANS_SERIF,Font.BOLD,15));
        jlNorth.setHorizontalAlignment(JLabel.CENTER);
        jlNorth.setVerticalAlignment(JLabel.CENTER);
        jlNorth.setBorder(new EmptyBorder(45, 400, 45, 30));
        add(jlNorth,constraints);

        jlImgUser = new JLabel(" ");
        jlImgUser.setName("imgUser");
        ImageIcon imageUser = new ImageIcon("Client/Images/"+ Commons.getJlImgUser(user.getImage()) + ".png");
        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add (jlImgUser, constraints);
        jlImgUser.setIcon(imageUser);
        jlImgUser.setVisible(true);

*/
    }

    public void registerController(MainController controller) {
        jlImgUser.addMouseListener(controller);
        jlImgUser.setName(PROFILE_COMMAND);
        jlImgLogo.addMouseListener(controller);
        jlImgLogo.setName(MainController.RETURN_COMPANY_COMMAND);
    }

    public void setImage (String image){
        ImageIcon imageUser = new ImageIcon("Client/Images/"+image+".png");
        jlImgUser.setIcon(imageUser);
    }

    public void setJlNameText(String name) {
        jlCompanyName.setText(name);
        setVisible(true);
    }

    public void setJlImgLogoName(String command) {
        jlImgLogo.setName(command);
    }

    public void updateHeaderLabels(String command, String name) {
        jlImgLogo.setName(command);
        jlCompanyName.setText(name);
        setVisible(true);
    }

}
