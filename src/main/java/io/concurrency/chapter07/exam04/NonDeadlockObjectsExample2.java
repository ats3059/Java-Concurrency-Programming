package io.concurrency.chapter07.exam04;

public class NonDeadlockObjectsExample2 {
    /*
        메서드 오픈이란 락을 획득하지 않고 메서드를 호출하는 것을 뜻한다. 락이 필요한 임계영역만 보호하도록 한다.
        thread1 ResourceA의 인스턴스락을 획득한 후 메서드 전체가 아닌 필요한 부분만 작업 후 락을 반납하고 resourceB의 인스턴스 락을 획득하려고 시도한다.
        thread2 ResourceB의 인스턴스락을 획득한 후 메서드 전체가 아닌 필요한 부분만 작업 후 락을 반납하고 resourceA의 인스턴스 락을 획득하려고 시도한다.

        두개의 스레드 모두 메서드에서 필요한 부분만을 실행 후 락을 반납 후 또 다른쪽의 락을 획득하기 때문에 데드락이 회피된다.
        ( 동시에 여러개의 락을 획득 후 계속해서 점유하는게 아닌 본인이 필요한 임계영역을 작업 후 락을 반납하고 다른 락을 획득하기 때문이다. )
        기존의 데드락 문제들은 결국 여러개의 락을 획득하고 반납하지 않은 상태에서 다른 락을 획득하려고 시도하게 되는데 다른쪽에서도 똑같이 여러개의
        락을 획득하고 반납하지 않고 락을 획득하려고 시도하기 때문에 이미 락이 걸려있는 상태의 자원을 서로 획득하기 위해서 대기하는 상황이 만들어지는게 문제.
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

    static class ResourceA {

        public void methodA(ResourceB resourceB) { // 메서드 오픈
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + ": methodA 실행");
            }
            resourceB.methodB2();
        }

        public synchronized void methodA2(ResourceB resourceB) {
            System.out.println(Thread.currentThread().getName() + ": methodA2 실행");
        }
    }

    static class ResourceB {

        public void methodB(ResourceA resourceA) { // 메서드 오픈
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + ": methodB 실행");
            }
            resourceA.methodA2(this);
        }

        public synchronized void methodB2() {
            System.out.println(Thread.currentThread().getName() + ": methodB2 실행");
        }
    }
}




