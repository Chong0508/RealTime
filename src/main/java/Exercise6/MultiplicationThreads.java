package Exercise6;

class MultiplicationTask extends Thread {
    private int number;
    private String name;

    public MultiplicationTask(int number, String name) {
        this.name = name; // Set thread name
        this.number = number;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(name + ": " + number + " * " + i + " = " + (number * i));
        }
    }
}

public class MultiplicationThreads {
    public static void main(String[] args) {
        // Create and start 3 threads for numbers 1, 2, and 3
        for (int i = 1; i <= 3; i++) {
            MultiplicationTask task = new MultiplicationTask(i, "Thread-" + (i - 1));
            task.start();
        }
    }
}
