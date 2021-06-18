/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp.assignment.gui;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Jason Pang
 */
public class Processes {

    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(900);
    private static BlockingQueue<DelayedQueue> currentVisitor = new DelayQueue<>();
    static Clock clock = new Clock();
    static int ticketNumber = 0;
    private static StringProperty processString;

    private static void updateTime() throws InterruptedException {
        // System.out.println(clock.getTime()); // View clock continuzously
        Thread.sleep(100);
        clock.updateClock();
        Thread.sleep(900); // Update clock every 1000ms
    }

    private static void appendToProcessString(String stringToAppend) {
        processString.set(processString.concat(stringToAppend + "\n").get());
    }
    
    public static StringProperty getProcess(){
        return processString;
    }

    private static void generateVisitor() throws InterruptedException {
        Random random = new Random();
        while (true) {
            if (ticketNumber <= 899 && clock.getHOUR() >= 8 && clock.getHOUR() < 18) {
                if (true || (clock.getHOUR() == 8 && clock.getMINUTE() == 0)) {
                    int groupSize = random.nextInt(3) + 1;
                    String currentTime = clock.getTime();
                    for (int i = 0; i < groupSize && (ticketNumber + groupSize <= 900); i++) {
                        ticketNumber++;
                        queue.put(ticketNumber);
                        String toPrintGenerate = currentTime + " Ticket T" + String.format("%04d", ticketNumber)
                                + " has been purchased.";
                        appendToProcessString(toPrintGenerate);
                        System.out.println(toPrintGenerate);
                    }
                }
            } else if (ticketNumber >= 900) {
                String soldOutTicket = "The daily limit of ticket sold is reached.";
                appendToProcessString(soldOutTicket);
                System.out.println(soldOutTicket);
                break;
            } else {
                String closingStatement = clock.getTime() + " The museum is now closed.";
                appendToProcessString(closingStatement);
                System.out.println(closingStatement);
                System.exit(0);
            }
            int temporaryStop = (random.nextInt(4) * 1000) + 1000;
            Thread.sleep(temporaryStop);
        }
    }

    private static void currentVisitor() throws InterruptedException {
        Random random = new Random();
        Entrance northEntrance = new Entrance();
        Entrance southEntrance = new Entrance();
        boolean warned = false;
        Thread.sleep(1000);
        while (true) {
            Thread.sleep(500);
            if (currentVisitor.size() >= 100) {
                if (!warned) {
                    String warning = "Sorry, the museum is full. Please wait for visitors to exit.";
                    appendToProcessString(warning);
                    System.out.println(warning);
                    warned = true;
                }
            } else {
                warned = false;
            }
            while (currentVisitor.size() < 100 && clock.getHOUR() >= 9 && clock.getHOUR() < 18) {
                int duration = random.nextInt(100) + 50; // long stay
                // int duration = random.nextInt(10) + 5; // short stay
                duration *= 1000;
                int value = queue.take();
                currentVisitor.put(new DelayedQueue(duration, value));
                int randomizeEntrance = random.nextInt(10);
                if (randomizeEntrance % 2 == 0) {
                    // use NorthEntrance
                    northEntrance.enter(ticketNumber);
                    String toPrintVisitor = clock.getTime() + " Ticket T" + String.format("%04d", value)
                            + " entered through NE" + northEntrance + " .Staying for " + duration / 1000 + " minutes.";
                    appendToProcessString(toPrintVisitor);
                    System.out.println(toPrintVisitor);
                } else {
                    // use SouthEntrance
                    southEntrance.enter(ticketNumber);
                    String toPrintVisitor = clock.getTime() + " Ticket T" + String.format("%04d", value)
                            + " entered through SE" + southEntrance + " .Staying for " + duration / 1000 + " minutes.";
                    appendToProcessString(toPrintVisitor);
                    System.out.println(toPrintVisitor);
                }
            }
        }
    }

    private static void exit() throws InterruptedException {
        Random random = new Random();
        Exit eastExit = new Exit();
        Exit westExit = new Exit();
        while (true) {
            int ticketNumber = currentVisitor.take().getTicketValue(); // to get the value(ticketNumber) of DelayedQueue Object
            int randomizeExit = random.nextInt(10);
            if (randomizeExit % 2 == 0) {
                // use EastExit
                eastExit.exit(ticketNumber);
                String toPrintExit = clock.getTime() + " Ticket T" + String.format("%04d", ticketNumber)
                        + " exited through EE" + eastExit;
                appendToProcessString(toPrintExit);
                System.out.println(toPrintExit);
            } else {
                // use WestExit
                westExit.exit(ticketNumber);
                String toPrintExit = clock.getTime() + " Ticket T" + String.format("%04d", ticketNumber)
                        + " exited through WE" + westExit;
                appendToProcessString(toPrintExit);
                System.out.println(toPrintExit);
            }
        }
    }
    
    public Processes(){
        this.processString = new SimpleStringProperty("");
        
    }
    
    public String getTime() {
        return clock.getTime();
    }

    public void startProcess() throws InterruptedException {
        

        Thread clockThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        updateTime();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // System.out.println(clock.getTime()); // check if clock is running
                }
            }
        });

        Thread queueThread = new Thread(new Runnable() {
            public void run() {
                try {
                    generateVisitor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        Thread enterThread = new Thread(new Runnable() {
            public void run() {
                try {

                    currentVisitor();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        Thread exitThread = new Thread(new Runnable() {
            public void run() {
                try {
                    exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        clockThread.setPriority(10);
        clockThread.start();
        queueThread.start();
        enterThread.start();
        exitThread.start();

        clockThread.join();
        queueThread.join();
        enterThread.join();
        exitThread.join();

    }
    
}


