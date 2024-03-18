package io.concurrency.chapter08.exam01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockOrderExample {
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        // synchronized

        new Thread(() -> {
            try{
                System.out.println("스레드가 1번 락을 획득했습니다.");
                lock1.lock();
                try{
                    System.out.println("스레드가 2번 락을 획득했습니다.");
                    lock2.lock();
                }finally {
                    System.out.println("스레드가 1번 락을 해제했습니다.");
                    lock1.unlock();
                }
            }finally {
                lock2.unlock();
                System.out.println("스레드가 2번 락을 해제했습니다.");
            }
        });
    }
}
