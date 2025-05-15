package Exercise8;

class SharedResource {
    int count = 0;

    void increment() {
        count++;
    }

    synchronized void synchronizedIncrement() {
        count++;
    }
}

class NormalThread extends Thread {
    SharedResource resource;
    NormalThread(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 100000; i++) {
            resource.increment();
        }
    }
}

class SynchronizedThread extends Thread {
    SharedResource resource;

    SynchronizedThread(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 100000; i++) {
            resource.synchronizedIncrement();
        }
    }
}

public class ComparisonsOfThreads {
    public static void main(String[] args) throws InterruptedException {
        SharedResource normalResource = new SharedResource();
        Thread[] normalThreads = new Thread[10];

        long startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            normalThreads[i] = new NormalThread(normalResource);
            normalThreads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            normalThreads[i].join();
        }
        long endTime = System.nanoTime();
        double normalTime = (endTime - startTime) / 1000000000.0;

        System.out.println("Normal Thread = " + normalTime + " seconds");

        SharedResource syncResource = new SharedResource();
        Thread[] syncThreads = new Thread[10];

        startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            syncThreads[i] = new SynchronizedThread(syncResource);
            syncThreads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            syncThreads[i].join();
        }
        endTime = System.nanoTime();
        double syncTime = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("Synchronized Thread = " + syncTime + " seconds");
    }
}


