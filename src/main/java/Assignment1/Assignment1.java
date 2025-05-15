package Assignment1;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Assignment1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Testing path: src/main/java/Assignment1/Exercise4/src/main/java/org/example
        System.out.println("(Testing path: src/main/java/Assignment1/Exercise4/src/main/java/org/example)");
        System.out.print("Enter the directory path to check for Java files: ");
        String path = scan.nextLine();
        path = path.replace('"',' ').trim();
        path = path.replace('\\','/');
        System.out.println("Path detected: "+ path);

        scan.close();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".java"));

        if (listOfFiles == null) {
            System.out.println("Invalid directory path or no Java files found!");
            return;
        }

        int javaFileCount = listOfFiles.length;
        AtomicInteger issuesCount = new AtomicInteger(0);
        Thread[] threads = new Thread[javaFileCount];

        System.out.println("Java File Names: ");
        for (int i = 0; i < javaFileCount; i++) {
            final File file = listOfFiles[i];
            System.out.println((i+1)+". "+file.getName());

            threads[i] = new Thread(() -> {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().toUpperCase();
                        if (line.contains("// SOLVED")) {
                            issuesCount.incrementAndGet();
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + file.getName());
                }
            });
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }

        System.out.println("Number of Java Files = " + javaFileCount);
        System.out.println("Number of Issues = " + issuesCount.get());
    }
}