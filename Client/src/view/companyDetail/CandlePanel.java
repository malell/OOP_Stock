package view.companyDetail;

import model.Candle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class CandlePanel extends JPanel {
    private final float CANDLE_WIDTH_PERCENT = 55f;
    private float max, min;
    private float width, height, lsb;
    private float[] y_axis;
    private Candle candle;


    //http://edu4java.com/es/game/game1.html
    //https://jairogarciarincon.com/clase/interfaces-de-usuario-con-java-swing/la-clase-graphics
    //https://www.codejava.net/java-se/graphics/drawing-rectangles-examples-with-graphics2d
    public CandlePanel(Candle candle, float max, float min, float[] y_axis) {
        super();
        this.candle = candle;
        this.max = max;
        this.min = min;
        this.y_axis = y_axis;
    }

    public void updateCandlePanel(Candle candle, float max, float min, float[] y_axis) {
        this.candle = candle;
        this.max = max;
        this.min = min;
        this.y_axis = y_axis;
        repaint();
    }

    private float getPXof(float y_eur) {
        return height - ((y_eur - min) * lsb);
    }

    @Override
    public void paint (Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        height = getHeight();
        width = getWidth();
        int width_candle = Math.round(width*(CANDLE_WIDTH_PERCENT/100));
        lsb = height/(max - min);                                               //px/€
        float openPrice = candle.getOpenPrice();
        float closePrice = candle.getClosePrice();

        float[] dashingPattern = {10f, 10f, 10f, 10f};
        Stroke stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 0.0f);
        g2D.setStroke(stroke);
        for (float y_axi : y_axis) {
            int y_dashed = Math.round(getPXof(y_axi));
            g2D.drawLine(0, y_dashed, Math.round(width), y_dashed);
        }
        if (candle.getMaxPrice() != -1) {
            int y_max = Math.round(getPXof(candle.getMaxPrice()));
            int y_min = Math.round(getPXof(candle.getMinPrice()));
            int x_center = Math.round(width / 2);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3f));
            g2D.drawLine(x_center, y_max, x_center, y_min);

            int x_rect = Math.round((width - width_candle) / 2);
            int y_rect = Math.round(getPXof(Math.max(openPrice, closePrice)));
            int height_rect = Math.round(lsb * (Math.max(openPrice, closePrice) - Math.min(openPrice, closePrice)));
            if (openPrice >= closePrice) g2D.setColor(Color.RED);
            else g2D.setColor(Color.BLUE);
            g2D.fillRect(x_rect, y_rect, width_candle, height_rect);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(1.5f));
            g2D.drawRect(x_rect, y_rect, width_candle, height_rect);
/*
            System.out.println(candle);
            System.out.println("Panel: width[" + width + "], height[" + height + "]\n");
            System.out.println("Line: max[" + candle.getMaxPrice() + "], min[" + candle.getMinPrice() + "]\n" +
                    "y_max = " + y_max + "\n" +
                    "y_min = " + y_min + "\n" +
                    "x_center = " + x_center + "\n");
            System.out.println("Rectangle: open[" + openPrice + "], close[" + closePrice + "]\n" +
                    "x_rect = " + x_rect + "\n" +
                    "y_rect = " + y_rect + "\n" +
                    "height_rect = " + height_rect + "\n");
*/
        }
    }
}