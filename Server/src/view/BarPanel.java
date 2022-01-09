package view;

import model.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.PLAIN;
import static view.Typo.df_cashEuro;

public class BarPanel extends JPanel {

    //private float width, height;
    //private float companyValue, max, min;
    private static final int TOP_BUFFER = 10; // where additional text is drawn
    private static final int AXIS_OFFSET = 20;

    private ArrayList<Company> companies;

    private int chartwidth, chartheight, chartX, chartY;

    private String[] xLabels, yLabels, axisLabels;

    public BarPanel(ArrayList<Company> companies){
        super();
        xLabels = new String[10];
        yLabels = new String[10];
        axisLabels = new String[8];
        getChartInfo(companies);

    }

    public void getChartInfo(ArrayList<Company> companies) {
        this.companies = companies;

        float max = (float) Math.ceil(companies.get(companies.size() - 1).getShareValue());
        float min = (float) Math.floor(companies.get(0).getShareValue());
        float dif = (max - min)/8;

        for(int i = 0; i < this.companies.size(); i++){
            xLabels[i] = companies.get(i).getName();
            yLabels[i] = df_cashEuro.format(companies.get(i).getShareValue());
        }

        axisLabels[0] = df_cashEuro.format(min);
        axisLabels[7] = df_cashEuro.format(max);

        for (int i = 1; i < 7; i++)
            axisLabels[i] = df_cashEuro.format(i*dif);
    }


    public void updateChart(ArrayList<Company> companies) {
        getChartInfo(companies);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        //setup counts
        //FET AL CONSTRUCTOR!

        //computeSize
        int width = this.getWidth();
        int height = this.getHeight();

        // chart area size
        chartwidth = width - 2*AXIS_OFFSET;
        chartheight = height- 2*AXIS_OFFSET - TOP_BUFFER;

        // Chart origin coords
        chartX = AXIS_OFFSET;
        chartY = height - AXIS_OFFSET;
        //int width_bar = Math.round(width*());

        //drawBars
        int numBars = companies.size();
        double max = 0.;
        double maxY, minY = 0.;

        for (Company c: companies) {
            if (max < c.getShareValue())
                max = c.getShareValue();
            else if (minY >= c.getShareValue())
                minY = c.getShareValue();
        }



        //System.out.println("max " + max);
        // No volem una amplada total resultant del numero de barres
        int barWidth = chartwidth/numBars;
        //System.out.println(barWidth);

        float shareValue;
        int xLeft, yTopLeft;
        int counter = 0;
        double heightaux;
        for (Company c: companies) {
            shareValue = c.getShareValue();

            heightaux = Math.ceil((shareValue/max)*chartheight);

            xLeft = AXIS_OFFSET + counter * barWidth;
            yTopLeft = chartY - (int)heightaux;

            g2D.fillRect(xLeft + 20 , yTopLeft, barWidth -20, chartY - yTopLeft);

            g2D.drawString(xLabels[counter], xLeft + chartX, chartY + 20);
            g2D.drawString(yLabels[counter], xLeft + chartX, yTopLeft - 20);

            counter++;
        }

        //draw Y levels
        for (int i = 0; i < 8; i++){
            g2D.drawString(axisLabels[i], chartX - 40, (int)(chartY + i*chartheight/8));
        }

        //draw Axes

        int rightX = chartX + chartwidth;
        int topY = chartY - chartheight;

        g2D.drawLine(chartX, chartY, rightX, chartY);

        g2D.drawLine(chartX, chartY, chartX, topY);

        //g2D.drawString(xLabel, chartX + chartwidth/2, chartY + AXIS_OFFSET/2 +3) ;

        // draw vertical string
        /*
        Font original = g2D.getFont();

        Font font = new Font(null, original.getStyle(), original.getSize());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2D.setFont(rotatedFont);
        g2D.drawString(yLabel,AXIS_OFFSET/2+3, chartY - chartheight/2);
        g2D.setFont(original); */

        /*
        g2D.setColor(original);

        int x = 29;
        int y = 48;
        int width = 413;
        int height = 118;

        g2D.fillRect(x, y, width, height);
        //equals
        //g2D.drawRect(29, 48, 413, 118);

        g2D.setColor(Color.BLACK);
        g2D.setFont(new Font("ARIAL",PLAIN,14));
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        g2D.drawString(df.format(companyValue) +"€", width + x, height + y);*/
    }
}
