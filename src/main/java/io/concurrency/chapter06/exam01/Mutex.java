package io.concurrency.chapter06.exam01;
public class Mutex {
    private boolean lock = false;

    public synchronized void acquired() {
        // lock을 획득하는 행위도 원자적으로 동작해야한다.
        /*
            1번 스레드 -> while 통과 후 lock 획득
            임계영역에서 데이터 작업 후 unlock 진행하며 대기중인 스레드의 상태를 깨운다.
            그 후 2번 스레드가 동작 ( 위와 같은 동작을 반복 )
            결국 동시성이 문제란 공유 데이터를 훼손시키지 않기 위해서 사용하는 것
            Mutex -> 상호배제
            공유 자원에 대한 경쟁상태를 방지하고 동시성 제어를 위한 락 메커니즘

            문제점 1. 데드락
            문제점 2. 우선 순위 역전
            문제점 3. 오버헤드
            문제점 4. 성능저하
            문제점 5. 잘못된 사용
         */
        while (lock) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.lock = true;
    }
    public synchronized void release() {
        this.lock = false;
        this.notify();
    }
}
