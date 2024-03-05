package io.concurrency.chapter03.exam03;

public class InterruptedExample3 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("스레드 작동 중");
                if (Thread.interrupted()) {
                    System.out.println("인터럽트 상태 초기화 되었습니다.");
                    break;
                }
            }
            System.out.println("인터럽트 상태: " + Thread.currentThread().isInterrupted());
            // 만약 다른 쓰레드가 해당 쓰레드의 interrupt 상태를 참조하면서 작업을 진행중이라면
            // 현재 쓰레드(thread)가 interrupt에 걸리고 false -> true 상태로 변경
            // 하지만 쓰레드의 상태를 체크하는 메서드가 Thread.interrupted() 를 사용중이기 때문에 false로 변경된다.
            // 여기에서 위에서 설명한 다른 쓰레드가 해당 쓰레드의 interrupt 상태를 참조중이다 라고 가정한다면 다시 원복을
            // 시켜야 하기 때문에 자기 자신의 쓰레드에 interrupt() 메서드를 호출해준다.
            Thread.currentThread().interrupt();
            System.out.println("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });

        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();
    }
}
