package io.concurrency.chapter03.exam01;

public class BasicSleepExample {
    public static void main(String[] args) {
        // Thread.sleep() 호출 시 유저모드에서 커널모드로 전환된다. -> 네이티브 메서드 호출 ( System Call )
        // 이때 현재 쓰레드의 상태는 TIMED_WAITING 상태로 변경되며 컨텍스트 스위칭이 발생한다 ( 스케줄러가 현재 대기중(RUNNABLE)인 쓰레드에게 CPU를 할당한다. )
        // Thread.sleep(0) -> 같은 우선순위를 가진 쓰레드가 있으면서 해당 쓰레드가 RUNNABLE 상태라면 컨텍스트 스위칭이 일어난다. 하지만 존재하지 않는다면 유저모드에서
        // 커널모드로 변경되고 다시 돌아와서 실행된다 ( 컨텍스트 스위칭이 일어나지 않는다. )
        // 확실하게 컨텍스트 스위칭이 일어나게 하려면 ( 다른 쓰레드에게 CPU 자원을 할당하게 하려면 ) Thread.sleep(1) 값은 할당을 해야한다.
        try {
            System.out.println("2초 후에 메시지가 출력됩니다");
            Thread.sleep(2000);
            System.out.println("Hello World");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
