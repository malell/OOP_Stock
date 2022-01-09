package model;

import java.util.LinkedList;

import static java.lang.Thread.sleep;

/**
 * Aquest Thread ens permet agafar el valor de la acció per cada segon o minut (a decidir)
 *
 */

public class ValueChangeThread extends Thread{
    private static final int SECOND = 1000;
    private static LinkedList<Float> minuteValues;
    private Company company;
    private boolean stop;

    //TODO atribut: vista taula companyies

    public ValueChangeThread(Company company){
        this.company = company;
        minuteValues = new LinkedList<>();
        stop = true;
    }

    @Override
    public void run() {
        while(!stop) {
            //Adds actual share value to arraylist
            if (minuteValues.size() > 300) {
                minuteValues.removeLast();
            }
            minuteValues.add(company.getShareValue());
            try {
                Thread.sleep(SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    //The idea is that controller will ask this function in order to calculate currency and percentage change of share value
    public float getMinChange(int minute){
        int numValues = minute*60;

        return minuteValues.get(numValues);
    }

}
