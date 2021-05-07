public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        final Processor processor = new Processor();

        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    processor.updateTime();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    processor.generateVisitor();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(new Runnable(){
            public void run(){
                    try{
                        processor.visitorEntering();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

    }

}
