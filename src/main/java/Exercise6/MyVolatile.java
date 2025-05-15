package Exercise6;

import java.io.IOException;

class MyThread extends Thread {
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Thread is running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
        System.out.println("Thread stopped.");
    }

    public void shutdown() {
        running = false;
    }
}


public class MyVolatile {
    public static void main(String[] args) throws IOException {
        MyThread thread = new MyThread();
        thread.start();

        System.out.println("Press ENTER to stop the thread...");
        System.in.read();

        thread.shutdown();
    }
}

