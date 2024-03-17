package io.concurrency.chapter07.exam04;

public class DeadlockDynamicOrderExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    // 동적락 예제

    /*
        스레드1 lock1 모니터락 획득 후 반납하지 않고 lock2 획득 시도
        스레드2 lock2 모니터락 획득 후 반납하지 않고 lock1 획득 시도
        -> 서로가 락을 획득하려고 대기하면서 데드락 발생
     */
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            methodWithLocks(lock1, lock2);
        });

        Thread thread2 = new Thread(() -> {
            methodWithLocks(lock2, lock1);
        });
        thread1.start();
        thread2.start();
    }

    private static void methodWithLocks(Object lockA, Object lockB) {

        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + ":  " + lockA + " 획득");

            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + ":  " + lockB + " 획득");
            }
        }
    }
}
