package io.concurrency.chapter04.exam05;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolThreadLocalExample {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        /*
         스레드풀을 사용하는 경우 스레드를 재사용하기 때문에 항상 threadLocal.remove()를 사용해줘야한다.
         그렇지 않으면 해당 스레드가 다음 작업을 진행할때 이전 작업에서의 데이터를 갖고 있기 때문이다.
         */
        ExecutorService executor = Executors.newFixedThreadPool(2); // 2개의 스레드를 가진 스레드 풀 생성

        // 첫 번째 작업: ThreadLocal 값을 설정
        executor.submit(() -> {
            threadLocal.set("작업 1의 값");
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            // threadLocal.remove() 호출 시
//            pool-1-thread-1: 작업 1의 값
//            pool-1-thread-2: null
//            pool-1-thread-1: null
//            pool-1-thread-2: null
//            pool-1-thread-1: null
//            pool-1-thread-2: null
//            threadLocal.remove();
        });

        // 잠시 대기
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 여러 번의 두 번째 작업: ThreadLocal 값을 설정하지 않고 바로 값을 가져와 출력
        // pool-1-thread-1: 작업 1의 값
        // pool-1-thread-2: null
        // pool-1-thread-1: 작업 1의 값
        // pool-1-thread-2: null
        // pool-1-thread-1: 작업 1의 값
        // pool-1-thread-2: null
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get()); // 예상치 못한 값 출력 가능
            });
        }

        executor.shutdown();
    }
}
