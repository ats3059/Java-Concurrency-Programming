package io.concurrency.chapter05.exam03;

public class CriticalSectionExample {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread t1 = new Thread(resource::increment);
        Thread t2 = new Thread(resource::increment);

        t1.start();
        t2.start();
    }
}

class SharedResource {
    private int counter = 0;

    public void increment() {

        for (int i = 0; i < 100000; i++) {
            // 동기화 처리를 위해 진입하는 구역 = Entry Section
//            synchronized (this) { // Entry Section
                // 임계영역
                // Critical Section
                counter++;
                System.out.println(Thread.currentThread().getName() + ": " + counter);

//            }// Exit Section
        }

        // Remainder Section
        doOtherWork();
    }

    private void doOtherWork() {
        System.out.println(Thread.currentThread().getName() + " 는 critical section 외부에서 작업을 수행하고 있다");
    }

    public int getCounter() {
        return counter;
    }
}
