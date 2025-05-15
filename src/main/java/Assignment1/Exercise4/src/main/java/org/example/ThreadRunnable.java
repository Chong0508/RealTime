package org.example;

public class ThreadRunnable {                          // SOLVED
    public static void main(String[] args) {
        Thread t = new Thread();
        t.start();
        System.out.println(t.getState());
    }
}
