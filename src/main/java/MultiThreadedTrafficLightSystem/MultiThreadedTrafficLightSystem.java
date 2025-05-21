package MultiThreadedTrafficLightSystem;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class MultiThreadedTrafficLightSystem {
    public static void main(String[] args) {
        TrafficController controller = new TrafficController();
        controller.startTrafficCycle();
    }
}

class TrafficController {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition[] trackConditions = new Condition[4];
    private int currentGreen = 0;
    private final String[] states = {"🟥", "🟥", "🟥", "🟥"};

    public TrafficController() {
        for (int i = 0; i < 4; i++) {
            trackConditions[i] = lock.newCondition();
        }
    }

    public void startTrafficCycle() {
        for (int i = 0; i < 4; i++) {
            int trackId = i;
            new Thread(() -> runTrafficLight(trackId)).start();
        }

        // Start with Track 0
        lock.lock();
        try {
            trackConditions[0].signal();
        } finally {
            lock.unlock();
        }
    }

    private void runTrafficLight(int trackId) {
        while (true) {
            lock.lock();
            try {
                while (trackId != currentGreen) {
                    trackConditions[trackId].await();
                }

                // GREEN
                states[trackId] = "🟩";
                showStatus();
                Thread.sleep(2000);

                // YELLOW
                states[trackId] = "🟨";
                showStatus();
                Thread.sleep(1000);

                // RED
                states[trackId] = "🟥";
                showStatus();

                // Move to next track
                currentGreen = (currentGreen + 1) % 4;
                trackConditions[currentGreen].signal();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private void showStatus() {
        for (int i = 0; i < 4; i++) {
            System.out.print("Track " + (i + 1) + ": " + states[i]);
            if (i < 3) System.out.print(" | ");
        }
        System.out.println();
    }
}
