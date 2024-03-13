package io.concurrency.chapter07.exam03;

public class VolatileExample2 {

    /*
        volatile 키워드가 동시성 처리는 불가능하지만 가시성을 확보해주기 때문에
        읽기 스레드 N : 쓰기 스레드 1인 상태에서는 사용이 가능하다
        이게 가능한 이유는 volatile 키워드로 인해 CPU 캐시에서 값을 읽고 , 저장하지 않기 때문이다.
        스레드가 값을 가져올때 메인메모리만 바라보고 값을 가져오기 때문에 쓰기를 실행하는 스레드는 계속해서
        메인 메모리에 저장을 진행하고 읽기를 진행하는 스레드는 메인메모리에서 값을 가져오기 때문.
     */
    private volatile int counter = 0;

    // 쓰기 작업 가시성 보장
    public void increment() {
        counter++;
    }

    // 읽기 작업 가시성 보장
    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        VolatileExample2 example = new VolatileExample2();

        // 쓰기 스레드
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
            System.out.println("쓰기 스레드가 쓰기 작업을 마쳤습니다.");
        });

        // 읽기 스레드
        Runnable reader = () -> {
            int localValue = -1;
            while (localValue < 1000) {
                localValue = example.getCounter();
                System.out.println(Thread.currentThread().getName() + " 읽은 값: " + localValue);
                try {
                    Thread.sleep(100); // Reader는 값을 더 천천히 읽는다.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        writer.start();
        for (int i = 0; i < 5; i++) {
            new Thread(reader).start();
        }
    }
}
