import model.ServerConfiguration;
import view.TopChart;

import javax.swing.*;

public class TopTest extends JFrame {

    public TopTest() {
        TopChart topChart = new TopChart();
        add(topChart);

        setTitle("Stock Server");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1280, 720);
        setVisible(true);
    }
    public static void main(String[] args) {
        new ServerConfiguration();
        new TopTest();
    }
}
