package io.concurrency.chapter09.exam01;


import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadCASExample {

    private static AtomicInteger value = new AtomicInteger(0);
    private static int NUM_THREADS = 3;
    public static void main(String[] args){
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    int expectedValue;
                    int newValue;
                    do {
                        expectedValue = value.get();
                        newValue = expectedValue + 1;
                        // cpu 차원에서 원자적으로 보장해준다 -> 지금 메모리에 저장된 값과 expectedValue 값이 같으면 메모리의 값을 newValue 값으로 변경해준다.
                    }while(!value.compareAndSet(expectedValue, newValue));
                    System.out.println(Thread.currentThread().getName() + ":" + expectedValue + ":" + newValue);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Final value : " + value.get());
    }

}
