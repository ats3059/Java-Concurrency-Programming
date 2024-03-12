package io.concurrency.chapter07.exam01;

class BankAccount {
    private double balance;
    private final Object lock = new Object();

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        synchronized (lock) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        synchronized (lock) {
            if (balance < amount) {
                return false;
            }
            balance -= amount;
            return true;
        }
    }

    public boolean transfer(BankAccount to, double amount) {
        // 실행부분에서 AccountBookA의 lock
        synchronized (this.lock) {
            // 실행부분에서 AccountBookB의 lock
            if (this.withdraw(amount)) {
                /*
                 AccountB에 lock을 획득해야 하는 이유는 계좌간의 거래가 끝날때까지 원자성이 보장되어야 하기 때문
                 AccountB와 AccountA 둘 다.
                 */
                synchronized (to.lock) {
                    to.deposit(amount);
                    return true;
                }
            }
            return false;
        }
    }

    public double getBalance() {
        synchronized (lock) {
            return balance;
        }
    }

}

public class MultipleMonitorsExample {

    public static void main(String[] args) {
        BankAccount accountA = new BankAccount(1000);
        BankAccount accountB = new BankAccount(1000);

        // accountA에서 accountB로 송금하는 스레드
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                boolean result = accountA.transfer(accountB, 10);
                if (result) {
                    System.out.println("accountA에서 accountB로 10 송금 성공");
                } else {
                    System.out.println("accountA에서 accountB로 10 송금 실패");
                }
            }
        });

        // accountB에서 accountA로 송금하는 스레드
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                boolean result = accountB.transfer(accountA, 10);
                if (result) {
                    System.out.println("accountB에서 accountA로 10 송금 성공");
                } else {
                    System.out.println("accountB에서 accountA로 10 송금 실패");
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("accountA의 최종 잔액: " + accountA.getBalance());
        System.out.println("accountB의 최종 잔액: " + accountB.getBalance());
    }
}