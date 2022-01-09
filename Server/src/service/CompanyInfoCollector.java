package service;

import controller.Commons;
import model.Candle;
import network.NewCandleDTO;
import network.ShareValueSampleDTO;
import service.CompanyService;

import java.util.ArrayList;

public class CompanyInfoCollector extends Thread {
    private static final int MAX_CANDLES = 15;
    private static final int SECOND = 1000;             //1 second = 1000 ms
    private static final int MIN_SECS = 60;             //1 minute = 60 seconds
    private static final int FIVEMIN_SECS = 300;        //5 minutes = 300 seconds
    private static final int SAMPLES_PER_SEC = 4;        //SAMPLE_PER_SEC >= 1
    private static final int MARGINFOR_FIVESEC_SAMPLE = 1;

    private String companyName;
    private CompanyService service;
    private ArrayList<Float> shareValueSamples;
    private ArrayList<Candle> candles;

    public CompanyInfoCollector(String companyName, float shareValue, CompanyService service) {
        this.companyName = companyName;
        this.service = service;
        shareValueSamples = new ArrayList<>();
        candles = new ArrayList<>();
        for (int i = 0; i < FIVEMIN_SECS; i++)
            shareValueSamples.add(shareValue);
        for (int i = 0; i < MAX_CANDLES; i++)
            candles.add(new Candle(-1,-1,-1,-1));
        this.start();
    }

    public String getCompanyName() {
        return companyName;
    }

    public ArrayList<Float> getShareValueSamples() {
        return shareValueSamples;
    }

    public ArrayList<Candle> getCandles() {
        return candles;
    }

    @Override
    public void run() {
        while(true) {
            float max = Float.MIN_VALUE;
            float min = Float.MAX_VALUE;
            //float openPrice = company.getPricePerShare();
            float openPrice = service.getShareValue(companyName);
            long start_minute = System.currentTimeMillis();
            //Waiting for overpassing 1 minute
            for (int i = 0; i < MIN_SECS; i++) {
                for (int j = 0; j < SAMPLES_PER_SEC; j++) {
                    long start_sample = System.currentTimeMillis();
                    float value = service.getShareValue(companyName);
                    if (max < value)   max = value;
                    if (min > value)   min = value;
                    try {
                        Thread.sleep((SECOND / SAMPLES_PER_SEC) - MARGINFOR_FIVESEC_SAMPLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //while (System.currentTimeMillis() - start_sample < (SECOND/SAMPLES_PER_SEC) - MARGINFOR_FIVESEC_SAMPLE);
                }
                float valueSample = service.getShareValue(companyName);
                //TODO  Comprovar llista de samples
                //if(companyName.equals("Google"))    System.out.println(shareValueSamples + " (" + shareValueSamples.size() + ")\n");
                Commons.addShareValueSample(valueSample, shareValueSamples);
                ShareValueSampleDTO message = new ShareValueSampleDTO(shareValueSamples.get(0), companyName);
                service.sendBroadcast(message);
                //while (System.currentTimeMillis() - start_minute < SECOND);
                try {
                    long wait_ms = MIN_SECS - (System.currentTimeMillis() - start_minute);
                    Thread.sleep(wait_ms < 0 ? 0 : wait_ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            float closePrice = service.getShareValue(companyName);
            Candle candle = new Candle(max, min, openPrice, closePrice);
            Commons.addCandle(candle, candles);
            //if(companyName.equals("Google"))    System.out.println(candles + " (" + candles.size() + ")\n");
            NewCandleDTO newCandleDTO = new NewCandleDTO(candle, companyName);
            service.sendBroadcast(newCandleDTO);

        }

    }


}