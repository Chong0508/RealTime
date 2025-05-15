package Exercise5;

public class TestSleep implements Runnable{

    private int threadNumber;

    public TestSleep(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public void run() {
        System.out.println("Thread #"+threadNumber);
        System.out.println("ONE");
        System.out.println("TWO");
        System.out.println("THREE");
        System.out.println("xxxxxxxxxx");

    }

    public static void main(String[] args) throws InterruptedException{
        for(int i=1;i<=20;i++) {
            Thread thread = new Thread(new TestSleep(i));
            thread.start();

            thread.sleep(100);
        }
    }
}

