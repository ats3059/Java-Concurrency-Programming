package io.concurrency.chapter03.exam03;

public class InterruptedExample1 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            // 인터럽트에 걸리며 while문을 빠져나오게된다.
            // 인터럽트를 사용하는 이유는 쓰레드의 실행을 중단하거나 , 작업취소, 강제종료등으로 사용한다.
            while (!Thread.interrupted()) {
                System.out.println("스레드가 작동 중입니다.");
            }
            System.out.println("스레드가 인터럽트 되었습니다.");
            System.out.println("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });
        thread.start();

        try {
            // Thread가 block 상태일때 interrupt 된다면 InterruptedException 을 처리한다.
            // 현재는 아무상황도 안일어남 main 쓰레드를 잠시 멈춘행위일뿐임.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
