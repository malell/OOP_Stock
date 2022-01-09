package view.companyDetail;

import model.Candle;
import view.YAxisText;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CandleChart extends JPanel {
    private ArrayList<CandlePanel> candlePanels;
    private JPanel jpYAxis;
    private ArrayList<Candle> candles;
    private final int TIMES = 10;
    private float minValue, maxValue;
    private float[] y_axis;
    private final Color BACKGROUND = Color.WHITE;

    public CandleChart(ArrayList<Candle> candles) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBackground(BACKGROUND);

        JPanel jpChart = new JPanel(new GridLayout(1,candles.size()));
        jpChart.setBackground(BACKGROUND);
        jpChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setMaxMin(candles);

        //Y axis on the chart
        jpYAxis = new JPanel(new GridLayout(TIMES, 1));
        jpYAxis.setBackground(BACKGROUND);

        y_axis = new float[TIMES];
        setYAxisValues();

        for (int i = TIMES-1; i >= 0; i--) {
            YAxisText yAxisText = new YAxisText(y_axis[i]);
            jpYAxis.add(yAxisText);
        }
        add(jpYAxis, BorderLayout.WEST);

        //Create and add single candle panels
        candlePanels = new ArrayList<>();
        for (Candle c : candles) {
            CandlePanel cp = new CandlePanel(c, maxValue, minValue, y_axis);
            candlePanels.add(cp);
            jpChart.add(cp);

        }
        add(jpChart, BorderLayout.CENTER);


    }

    public void setMaxMin(ArrayList<Candle> candles) {
        //Takes max and miv values to set scale
        minValue = Float.MAX_VALUE;
        maxValue = Float.MIN_VALUE;
        for (Candle c : candles) {
            if (c.getMaxPrice() != -1) {
                minValue = Math.min(minValue, c.getMinPrice());
                maxValue = Math.max(maxValue, c.getMaxPrice());
            }
        }
        minValue = (float)Math.floor(minValue);
        maxValue = (float)Math.ceil(maxValue);
    }

    public void setYAxisValues() {
        float dif = (maxValue - minValue) / TIMES;
        for (int i = TIMES-1; i >= 0; i--) {
            y_axis[i] = minValue + i*dif;
        }
    }

    public void updateChart(ArrayList<Candle> candles) {
        setMaxMin(candles);
        setYAxisValues();
        int e = TIMES-1;
        for (Component c : jpYAxis.getComponents())
            ((YAxisText)c).updateNum(y_axis[e--]);
        for (int i = 0; i < candles.size(); i++) {
            candlePanels.get(i).updateCandlePanel(candles.get(i), maxValue, minValue, y_axis);
        }
        repaint();
    }

}
