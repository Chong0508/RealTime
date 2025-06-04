package Assignment2;

import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class BankAccountWithLock {
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public BankAccountWithLock(double initialBalance) {
        this.balance = initialBalance;
    }

    // Read balance (shared lock)
    public double getBalance() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reads balance: " + balance);
            return balance;
        } finally {
            readLock.unlock();
        }
    }

    // Deposit money (exclusive lock)
    public void deposit(double amount) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " deposits: " + amount);
            balance += amount;
        } finally {
            writeLock.unlock();
        }
    }

    // Withdraw money (exclusive lock)
    public void withdraw(double amount) {
        writeLock.lock();
        try {
            if (balance >= amount) {
                System.out.println(Thread.currentThread().getName() + " withdraws: " + amount);
                balance -= amount;
            } else {
                System.out.println(Thread.currentThread().getName() + " insufficient funds for: " + amount);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        BankAccountWithLock account = new BankAccountWithLock(1000.00);
        Scanner scan = new Scanner(System.in);
        boolean operation = true;

        while (operation) {
            System.out.println("\n1. Read balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            if (scan.hasNextInt()) {
                int choice = scan.nextInt();

                switch (choice) {
                    case 1: {
                        Thread reader = new Thread(() -> {
                            account.getBalance();
                        }, "Reader-1");
                        reader.start();
                        try {
                            reader.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case 2: {
                        System.out.print("Enter your deposit amount: ");
                        if (scan.hasNextDouble()) {
                            double depositAmount = scan.nextDouble();
                            Thread depositor = new Thread(() -> {
                                account.deposit(depositAmount);
                            }, "Depositor-1");
                            depositor.start();
                            try {
                                depositor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }

                    case 3: {
                        System.out.print("Enter your withdraw amount: ");
                        if (scan.hasNextDouble()) {
                            double withdrawAmount = scan.nextDouble();
                            Thread withdrawer = new Thread(() -> {
                                account.withdraw(withdrawAmount);
                            }, "Withdrawer-1");

                            withdrawer.start();
                            try {
                                withdrawer.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }

                    case 4:
                        operation = false;
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scan.next();
            }
        }
        scan.close();
    }
}

