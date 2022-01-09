package view;

import controller.Commons;
import controller.MainController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ProfileUser extends JPanel {

    private final JTextArea jtDescription;
    private final JLabel jlImgUser;
    private final JLabel jlUserName;
    private final JComboBox<String> jcImgs;
    private final SButton jbReturn;
    private final SButton jbLogOut;

    public ProfileUser () {

        setLayout(new GridBagLayout());
        setBackground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();
        setBorder(new EmptyBorder(30,30,30,30));

        jbReturn = new SButton("Return","white","black");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add(jbReturn,constraints);

        jlImgUser = new JLabel(" ");
        jlImgUser.setName("imgUser");
        ImageIcon imageUser = new ImageIcon("Client/Images/Default.png");
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 3;
        jlImgUser.setBorder(new EmptyBorder(0, 100, 15, 100));
        add (jlImgUser, constraints);
        jlImgUser.setIcon(imageUser);
        jlImgUser.setVisible(true);

        jlUserName = new JLabel();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        jlUserName.setFont(new Font (Font.SANS_SERIF,Font.BOLD,15));
        jlUserName.setHorizontalAlignment(JLabel.CENTER);
        jlUserName.setVerticalAlignment(JLabel.CENTER);
        jlUserName.setBorder(new EmptyBorder(0, 0, 40, 0));
        add(jlUserName,constraints);

        jcImgs = new JComboBox<>();
        jcImgs.addItem("Default");
        jcImgs.addItem("Doggo");
        jcImgs.addItem("Grumpy Cat");
        jcImgs.addItem("Boromir");
        jcImgs.addItem("Horse");
        jcImgs.addItem("Sculpture");
        jcImgs.addItem("Gentleman");
        jcImgs.addItem("Ceus");
        jcImgs.addItem("Super Taldo");
        jcImgs.addItem("Pedobear");
        jcImgs.setBackground(Color.BLACK);
        jcImgs.setForeground(Color.white);
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        add(jcImgs,constraints);

        jtDescription = new JTextArea("Add a description...");
        jtDescription.setName("jtdescription");
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        jtDescription.setLineWrap(true);
        jtDescription.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK,1),BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        add(jtDescription,constraints);

        jbLogOut = new SButton("Log out","white","red");
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add(jbLogOut,constraints);

        setPreferredSize(new Dimension(500,400));
        setVisible(true);
    }

    public void registerController(MainController controller) {
        jlUserName.setText(controller.getClient().getUser().getUsername());
        jbReturn.setActionCommand(MainController.RETURN_PROFILE_COMMAND);
        jbReturn.addActionListener(controller);
        jcImgs.addActionListener(controller);
        jcImgs.setActionCommand(MainController.SETIMAGE_COMMAND);
        jtDescription.addMouseListener(controller);
        jbLogOut.setActionCommand(MainController.LOGOUT_COMMAND);
        jbLogOut.addActionListener(controller);
    }

    public String getJtDescription() {
        return jtDescription.getText();
    }

    public void setJtDescription(String txt){
        jtDescription.setText(txt);
    }

    public int getJlImgUser() {
        switch (jlImgUser.getName()){
            case "Doggo":
                return 1;
            case "Grumpy Cat":
                return 2;
            case "Boromir":
                return 3;
            case "Horse":
                return 4;
            case "Sculpture":
                return 5;
            case "Gentleman":
                return 6;
            case "Ceus":
                return 7;
            case "Super Taldo":
                return 8;
            case "Pedobear":
                return 9;
            default:
                return 0;
        }
    }

    public String getJlUserName() {
        return jlUserName.getText();
    }

    public void setImage (String image){
        ImageIcon imageUser = new ImageIcon("Client/Images/"+image+".png");
        jlImgUser.setIcon(imageUser);
        jlImgUser.setName(image);
    }

    public void updatePanel(User user) {
        setImage(Commons.getJlImgUser(user.getImage()));
        jcImgs.setSelectedItem(Commons.getJlImgUser(user.getImage()));
        jtDescription.setText(user.getDescription());
        repaint();
        setVisible(true);
    }

    public JComboBox<String> getJcImgs() {
        return jcImgs;
    }



}
