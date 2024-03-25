package io.concurrency.chapter09.exam02;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanExample {

    private static AtomicBoolean flag = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                // 원자적 연산이다.
                while (!flag.compareAndSet(false, true)) {
                    System.out.println("스레드 1 이 바쁜 대기 중.." + flag.get());
                }
                System.out.println(" 스레드 1 이 임계영역 수행중 ..");
                flag.set(false);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                // 원자적 연산이다.
                while (!flag.compareAndSet(false, true)) {
                    System.out.println("스레드 2 가 바쁜 대기 중.." + flag.get());
                }
                System.out.println(" 스레드 2 가 임계영역 수행중 ..");
                flag.set(false);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }
}

