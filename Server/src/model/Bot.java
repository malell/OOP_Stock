package model;

import controller.Commons;
import controller.ServerController;

import java.util.Random;

public class Bot extends Thread {
    private float probability;
    private float timeSleep;
    private boolean status;
    private Company company;
    private ServerController sc;
    private Message msg;

    public Bot(float probability, float timeSleep, boolean status, Company company) {
        this.probability = probability;
        this.timeSleep = timeSleep;
        this.status = status;
        this.company = company;
        this.msg=new Message("Semafor");
    }


    //https://www.journaldev.com/1037/java-thread-wait-notify-and-notifyall-example


    public void setSc(ServerController sc) {
        this.sc = sc;
    }

    /**
     * if the bots status is activated the bot generate a transaction, buying or selling. if not, he waits.
     */

    @Override
    public void run(){
        Random rand = new Random();

        while (true){
            //System.out.println("comencem iteracio while(true)"+"   -   "+this.getName()+"  -  "+this.company.getName()+"  -  "+this.status+"   -   "+ this.hashCode());
            try {

                if (status){
                    Thread.sleep((long) (this.getTimeSleep()*1000));
                    float randomNum =  100 * rand.nextFloat();
                    if (randomNum<this.probability){
                        //System.out.println(this.getCompany().getName()+"COMPRA");
                        sc.transaction(this.company.getName(), Commons.BUY);

                    }else{
                        //System.out.println(this.getCompany().getName()+"ven");
                        sc.transaction(this.company.getName(), Commons.SELL);
                    }
                }else{
                    synchronized (msg) {
                        //System.out.println("waiit"+"   -   "+this.getName()+"  -  "+this.company.getName()+"  -  "+this.status+"   -   "+ this.hashCode());

                        msg.wait();
                    }

                }



            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                //e.printStackTrace();
            }
        }

    }

    /**
     * stops the bot
     */
    public void killThread(){
        status = false;
        interrupt();
    }

    /**
     * pauses the bot
     */

    public void pauseThread() {

        //System.out.println("comencem pauseThread"+"   -   "+this.getName()+"  -  "+this.company.getName()+"  -  "+this.status+"   -   "+ this.hashCode());
        status=false;
    }

    /**
     * resume the bot
     */
    public void resumeThread(){

        //System.out.println("comencem resumeThread"+"   -   "+this.getName()+"  -  "+this.company.getName()+"  -  "+this.status+"   -   "+ this.hashCode());

        if (!status){
            synchronized (msg){
                status=true;
                msg.notify();
            }

        }


    }


    public float getProbability() {
        return probability;
    }

    public float getTimeSleep() {
        return timeSleep;
    }

    public boolean isStatus() {
        return status;
    }

    public Company getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "company=" + company.getName() +
                '}';
    }
}
