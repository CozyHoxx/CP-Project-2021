import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

class Main {
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(900);
    private static BlockingQueue<DelayedQueue> currentVisitor = new DelayQueue<>();
    static Clock clock = new Clock();
    static int ticketNumber = 0;

    private static void updateTime() throws InterruptedException {
        // System.out.println(clock.getTime()); // View clock continuously
        clock.updateClock();
        Thread.sleep(1000);
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
                        System.out.println(currentTime + " Ticket T" + String.format("%04d", ticketNumber)
                                + " has been purchased.");
                    }
                }
            } else if (ticketNumber >= 900) {
                System.out.println("The daily limit of ticket sold is reached.");
                break;
            } else {
                System.out.println(clock.getTime()+" The museum is now closed.");
                System.exit(0);
            }
            int temporaryStop = (random.nextInt(4) * 1000) + 1000;
            // System.out.println("stopped for "+temporaryStop);
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
                    System.out.println("Sorry, the museum is full. Please wait for visitors to exit.");
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
                    System.out.println(clock.getTime() + " Ticket T" + String.format("%04d", value)
                            + " entered through NE" + northEntrance + " .Staying for " + duration / 1000 + " minutes.");
                } else {
                    // use SouthEntrance
                    southEntrance.enter(ticketNumber);
                    System.out.println(clock.getTime() + " Ticket T" + String.format("%04d", value)
                            + " entered through SE" + southEntrance + " .Staying for " + duration / 1000 + " minutes.");
                }
            }
        }
    }

    private static void exit() throws InterruptedException {
        Random random = new Random();
        Exit eastExit = new Exit();
        Exit westExit = new Exit();
        while (true) {
            int ticketNumber = currentVisitor.take().getTicketValue(); // to get the value of DelayedQueue Object
            int randomizeExit = random.nextInt(10);
            if (randomizeExit % 2 == 0) {
                // use EastExit
                eastExit.exit(ticketNumber);
                System.out.println(clock.getTime() + " Ticket T" + String.format("%04d", ticketNumber)
                        + " exited through EE" + eastExit);
            } else {
                // use WestExit
                westExit.exit(ticketNumber);
                System.out.println(clock.getTime() + " Ticket T" + String.format("%04d", ticketNumber)
                        + " exited through WE" + westExit);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

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