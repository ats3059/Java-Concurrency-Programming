package io.concurrency.chapter07.exam01;

public class SynchronizedExamples {

    /*
        자바에서 synchronized 키워드를 사용하면 묵시적으로 모니터락을 사용해준다.
        이때 하나의 객체 ( 클래스 , 인스턴스 )에는 두개의 모니터가 존재한다.
        하나는 정적 모니터 (static) 그리고 인스턴스 (this)가 있다.

        만약 아래의 staticMethod()를 thread1번이 lock을 획득하여 사용중이라면
        staticBlock() 메서드에 들어오는 스레드는 thread1번의 staticMethod() 작업을 종료 후 ( lock을 반환 후 )
        사용이 가능하다. ( 모니터가 같기 때문 ) 인스턴스도 마찬가지이다.
     */

    private int instanceCount = 0;
    private static int staticCount = 0;

    public synchronized void instanceMethod() {
        instanceCount++;
        System.out.println("인스턴스 메서드 동기화: " + instanceCount);
    }

    public static synchronized void staticMethod() {
        staticCount++;
        System.out.println("정적 메서드 동기화: " + staticCount);
    }

    public void instanceBlock() {
        synchronized (this) {
            instanceCount++;
            System.out.println("인스턴스 블록 동기화: " + instanceCount);
        }
    }

    public static void staticBlock() {
        synchronized (SynchronizedExamples.class) {
            staticCount++;
            System.out.println("정적 블록 동기화: " + staticCount);
        }
    }

    public static void main(String[] args) {
        SynchronizedExamples example = new SynchronizedExamples();

        new Thread(example::instanceMethod).start();
        new Thread(example::instanceBlock).start();
        new Thread(SynchronizedExamples::staticMethod).start();
        new Thread(SynchronizedExamples::staticBlock).start();
    }
}
