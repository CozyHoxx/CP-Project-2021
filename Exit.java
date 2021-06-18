package cp.assignment.gui;

import java.util.Random;
public class Exit {
    Turnstile t1;
    Turnstile t2;
    Turnstile t3;
    Turnstile t4;
    String currentTurnstileString;

    public Exit(){
        this.t1 = new Turnstile();
        this.t2 = new Turnstile();
        this.t3 = new Turnstile();
        this.t4 = new Turnstile();
    }

    public void exit(int ticketNumber) throws InterruptedException {
        Random random = new Random();
        int randomTurnstile = random.nextInt(4) + 1;
        switch (randomTurnstile) {
            case 1:
                t1.close();
                currentTurnstileString = "T1";
                Thread.sleep(100);
                t1.open();
                break;
            case 2:
                t2.close();
                currentTurnstileString = "T2";
                Thread.sleep(100);
                t2.open();
                break;
            case 3:
                t3.close();
                currentTurnstileString = "T3";
                Thread.sleep(100);
                t3.open();
                break;
            case 4:
                t4.close();
                currentTurnstileString = "T4";
                Thread.sleep(100);
                t4.open();
                break;
        }
    }

    public String toString() {
        return currentTurnstileString;
    }
}
