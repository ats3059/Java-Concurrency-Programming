package io.concurrency.chapter06.exam02;

public class BinarySemaphore implements CommonSemaphore {
    /*
        이진 세마포어는 Mutex와 거의 같다. 단지 signal을 lock으로 대체한다는점
     */
    private int signal = 1;

    public synchronized void acquired() {
        while (this.signal == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 설정
            }
        }
        this.signal = 0;
    }

    public synchronized void release() {
        this.signal = 1;
        this.notify();
    }
}
