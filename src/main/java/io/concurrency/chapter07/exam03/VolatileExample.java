package io.concurrency.chapter07.exam03;

public class VolatileExample {
    // volatile 키워드 추가 -> 가시성 확보
//   volatile boolean running = true;
   boolean running = true;

    public void volatileTest() {
        /*
         volatile 설명하는 가장 좋은 예시이다.
         현재 while 문이 종료하는 조건은 running 상태가 false로 변경되는 것이다.
         만약 running 변수에 volatile 키워드가 붙지 않는다면 어떻게 진행될까?
         while문을 실행하는 스레드는 계속해서 cpu 캐시에서 데이터를 읽게된다.
         ( 스레드의 문맥에 저장된 정보를 CPU에서 처리 시 성능의 이점을 위해서 CPU 캐시에서 값을 사용하기 때문)
         Thread 2는 running 값을 false로 변경하며 종료하게 되는데 현재 while문을 실행하는 스레드는 계속해서 cpu 캐시의 데이터만
         바라보고 작업을 진행하기 때문에 어플리케이션이 종료되지 않게된다.

         이런 문제가 가시성의 문제에 해당하는데 해당 문제를 volatile 키워드를 통해서 항상 메인 메모리에 접근하여 값을 읽기,쓰기하게 변경
         해주기 때문에 Thread2에서 running값을 false로 변경할 경우 Thread1번은 cpu 캐시가 아닌 메인 메모리를 바라보게 되면서
         running 값이 변경된 것을 확인하고 종료하게된다.
         */
        new Thread(() -> {
            int count = 0;
            while (running) {
                /*try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/
                count++;
            }
            System.out.println("Thread 1 종료. Count: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Thread 2 종료 중..");
            running = false;
        }).start();
    }

    public static void main(String[] args) {
        new VolatileExample().volatileTest();
    }
}
