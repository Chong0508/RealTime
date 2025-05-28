package Chapter8_Concurrency_Collection;

public class ProducerConsumerDemo_notifyAll {
    static class SharedData {
        private boolean dataReady = false;
        private String data;

        public synchronized void produce() {
            try {
                System.out.println("Producer: Preparing data...");
                Thread.sleep(1000);
                data = "Hello from producer";
                dataReady = true;
                System.out.println("Producer:  Data is ready.");
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public synchronized void consume() {
            try {
                while(!dataReady) {
                    System.out.println("Consumer: Waiting for data...");
                    wait();
                }
                System.out.println("Consumer: Received -> " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SharedData sharedData = new SharedData();

        Thread consumerThread1 = new Thread(sharedData::consume);
        Thread consumerThread2 = new Thread(sharedData::consume);

        consumerThread1.start();
        consumerThread2.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Thread producerThread = new Thread(sharedData::produce);
        producerThread.start();
    }
}
