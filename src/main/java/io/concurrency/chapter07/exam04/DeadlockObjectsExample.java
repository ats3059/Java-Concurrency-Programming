package io.concurrency.chapter07.exam04;

public class DeadlockObjectsExample {
    /*
        스레드1
            ResourceA 인스턴스 모니터 락 획득 락을 반납하지 않고 ResourceB의 인스턴스 모니터락을 획득 시도
        스레드2
            ResourceB 인스턴스 모니터 락 획득 락을 반납하지 않고 ResourceB 인스턴스 모니터락을 획득 시도

        두개의 스레드가 서로 락을 획득한 뒤 반납하지 않고 다른 한쪽의 락을 획득하기 위해서 무한대기 -> 데드락

     */
    public static void main(String[] args) {

        ResourceA resourceA = new ResourceA();
        ResourceB resourceB = new ResourceB();

        Thread thread1 = new Thread(() -> {
            resourceA.methodA(resourceB);
        });

        Thread thread2 = new Thread(() -> {
            resourceB.methodB(resourceA);
        });

        thread1.start();
        thread2.start();
    }
}

class ResourceA {

    public synchronized void methodA(ResourceB resourceB) {
        System.out.println(Thread.currentThread().getName() + ": methodA 실행");
        try {
            Thread.sleep(100);  // 각 메소드에 지연을 추가하여 데드락 가능성 높임
        } catch (InterruptedException e) {}
        resourceB.methodB2();
    }

    public synchronized void methodA2() {
        System.out.println(Thread.currentThread().getName() + ": methodA2 실행");
    }
}

class ResourceB {

    public synchronized void methodB(ResourceA resourceA) {
        System.out.println(Thread.currentThread().getName() + ": methodB 실행");
        try {
            Thread.sleep(100);  // 각 메소드에 지연을 추가하여 데드락 가능성 높임
        } catch (InterruptedException e) {}
        resourceA.methodA2();
    }

    public synchronized void methodB2() {
        System.out.println(Thread.currentThread().getName() + ": methodB2 실행");
    }
}
