package io.concurrency.chapter07.exam04;

public class NonDeadlockOrderExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    /*
        데드락이 걸리지 않는 이유
        thread1 실행 후 lock1 , lock2 획득 ( thread2는 현재 lock1을 획득하려고 대기 )
        thread1의 모든 작업을 종료 후 thread2가 lock을 획득하면서 데드락 회피

        여러개의 락을 사용하려면 락의 점유 순서를 일정한 순서로 정해주도록 한다.
     */
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            process1();
        });

        Thread thread2 = new Thread(() -> {
            process2();
        });

        thread1.start();
        thread2.start();
    }

    private static void process1() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + " 이 lock1 을 획득하였습니다.");

            try {
                // 스레드 간의 경쟁 조건을 만들기 위해 잠시 대기
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " 이 lock2 을 획득하였습니다.");
            }
        }
    }

    private static void process2() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + " 이 lock2 을 획득하였습니다.");

            try {
                // 스레드 간의 경쟁 조건을 만들기 위해 잠시 대기
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " 이 lock1 을 획득하였습니다.");
            }
        }
    }
}
