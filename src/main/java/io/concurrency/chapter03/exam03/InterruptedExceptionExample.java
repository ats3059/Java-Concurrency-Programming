package io.concurrency.chapter03.exam03;

public class InterruptedExceptionExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("인터럽트 상태 1: " + Thread.currentThread().isInterrupted());
                Thread.sleep(5000);
                System.out.println("스레드 잠자기 완료");
            } catch (InterruptedException e) { // InterruptedException 예외가 발생하면 인터럽트 상태가 초기화 된다 : false
                System.out.println("스레드가 인터럽트 되었습니다.");
                System.out.println("인터럽트 상태 2: " + Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
            }
        });

        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 지금 thread의 상태는 sleep() 즉 block 상태이다 ( 자바에서 말하는 BLOCK 상태를 말하는 것이 아님 )
        // 지금 상태에서 interrupt를 호출 시 InterruptedException 발생하며 예외처리를 진행하게 된다.
        thread.interrupt();
        // 쓰레드의 작업이 끝날때까지 대기한다.
        thread.join();
        // true
        System.out.println("인터럽트 상태 3: " + thread.isInterrupted());
    }
}
