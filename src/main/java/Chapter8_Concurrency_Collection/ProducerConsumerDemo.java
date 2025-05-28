package Chapter8_Concurrency_Collection;

public class ProducerConsumerDemo {

    static class SharedData {
        private boolean dataReady = false;
        private String data;

        // Producer thread
        public synchronized void produce() {
            try {
                System.out.println("Producer: Preparing data...");
                Thread.sleep(1000);
                data = "Hello from producer";
                dataReady = true;
                System.out.println("Producer: Data is ready.");
                notify(); // Notify waiting consumer
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Consumer thread
        public synchronized void consume() {
            try {
                while(!dataReady) {
                    System.out.println("Consumer: Waiting for data...");
                    wait(); // Release lock and wait to be notified
                }
                System.out.println("Consumer: Received -> " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SharedData sharedData = new SharedData();

        // Create consumer thread
        Thread consumerThread = new Thread(() -> sharedData.consume());

        // Create producer thread
        Thread producerThread = new Thread(() -> sharedData.produce());

        // Start consumer thread first
        consumerThread.start();

        // Delay starting the producer thread
        try {
            Thread.sleep(500); // Let the consumer start and wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        producerThread.start();
    }
}
