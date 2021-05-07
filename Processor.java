import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class Processor {
    private Object lock = new Object();
    private Object lock2 = new Object();

    private BlockingQueue<DelayedQueue> currentVisitor = new DelayQueue<>();
    ArrayList<Visitor> queue = new ArrayList<Visitor>();
    Clock clock = new Clock();

    public void generateVisitor() throws InterruptedException {
        int groupSize;
        Random random = new Random();
        int ticketNumber = 1;
        int groupNumber = 0;
        synchronized(this){
            while (clock.getHOUR() >= 8 && clock.getHOUR() <= 9) {
                groupSize = random.nextInt(3) + 1;
                groupNumber++;
                for (int i = 0; i < groupSize; i++) {
                    queue.add(new Visitor(clock.getTime(), groupNumber, ticketNumber));
                    System.out.println(clock.getTime() + " Ticket T" + String.format("%04d", ticketNumber)
                    + " has been purchased.");
                    ticketNumber++;
                }
                notify();
                Thread.sleep(200);
                wait();
            }
        }
        
    }

    public void visitorEntering() throws InterruptedException {
        Random random = new Random();
        int queueCounter = 0;
        int groupCounter = 1;
        synchronized(this){
            while (currentVisitor.size() <= 100 && clock.getHOUR() >= 8) {
                int duration = random.nextInt(100) + 50; // long stay
                // int duration = random.nextInt(10) + 5; // short stay
                if ( !queue.isEmpty()) {
                    int nextVisitor = queue.get(queueCounter).getTicketNumber();
                    if (nextVisitor > groupCounter){
                        int ticketNumber = queue.get(queueCounter).getTicketNumber();
                        currentVisitor.put(new DelayedQueue(duration, ticketNumber));
                        System.out.println(clock.getTime() + " Ticket " + "T" + String.format("%04d", ticketNumber)
                        + " entered. Staying for " + duration + " minutes.");
                    }
                    queueCounter++;
                }
                notifyAll();
                Thread.sleep(200);
                wait();
            }
        }
    }

    public void updateTime() throws InterruptedException {        
        synchronized(this){

            while (clock.getHOUR() <= 9) {
                System.out.println(clock.getTime());
                clock.updateClock();
                notify();
                Thread.sleep(200);
                wait();
            }
        }
    }
}
