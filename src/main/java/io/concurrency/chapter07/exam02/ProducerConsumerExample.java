package io.concurrency.chapter07.exam02;

import java.util.LinkedList;
import java.util.Queue;

class SharedQueue {
    private Queue<Integer> queue = new LinkedList<>();
    private final int CAPACITY = 5;  // 큐의 최대 용량
    private final Object lock = new Object();

    // 아이템을 큐에 추가
    public void produce(int item) throws InterruptedException {
        synchronized (lock) {
            /*
                Condition Variable 조건이 while문인 이유
                1. notifyAll() 에 의해서 대기에서 풀려난 스레드가 스택에서 다음 구문을 실행할 때
                다시 조건을 봐야하기 때문. 만약 if문이라면 바로 조건에 해당하지 않는데 실행을 하기때문.
                2.알 수 없는 이유로 스레드가 꺠어나는 현상이 있는데 이걸 spurious wakeups라고 하고 notify()에 의해서
                스레드가 깨어나야 하는데 이미 깨어나 활동 할 가능성이 있어서 <- 버그라고함
                3.대기에서 꺠어난 스레드가 락을 획득한 후 wait() 구문에서 리턴하여 진행을 하는데 그 전에 조건을 확인해보니
                다시 wait() 실행해야 하는 상태가 발생할 수 있어서.
             */
            while (queue.size() == CAPACITY) {  // 큐가 가득 찼으면
                System.out.println("큐가 가득 찼습니다. 생산 중지...");
                lock.wait();  // 대기 -> 더이상 조건에 부합하는 스레드가 없기 때문에 wait() lock 반납 하면 consumer에서 실행 상호협력
            }

            queue.add(item);
            System.out.println("생산: " + item);

            lock.notifyAll();  // 대기 중인 모든 소비자에게 알림
        }
    }

    // 아이템을 큐에서 제거
    public void consume() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {  // 큐가 비어 있으면
                System.out.println("큐가 비었습니다. 소비 중지...");
                lock.wait();  // 대기
            }

            int item = queue.poll();
            System.out.println("소비: " + item);

            lock.notifyAll();  // 대기 중인 모든 생산자에게 알림
        }
    }
}

public class ProducerConsumerExample {

    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue();

        // 생산자 스레드
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    sharedQueue.produce(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"생산자");



        // 소비자 스레드
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    sharedQueue.consume();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"소비자");



        producer.start();
        consumer.start();

    }
}
