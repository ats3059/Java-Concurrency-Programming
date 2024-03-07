package io.concurrency.chapter04.exam05;

public class ThreadLocalExample {
    // ThreadLocal 변수 생성. 초기값은 null

    //    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "Hello World");

    public static void main(String[] args) throws InterruptedException {

        /*
         스레드 로컬은 스레드마다 각각 독립적인 공간에 저장된다
         처음에 초기값을 안준다면 null 준다면 해당 값을 반환한다.
         스레드 로컬에 초기값을 줬다면 해당 스레드 로컬은 remove() 호출 이후에도 기본값으로 갖고있다.
         스레드 로컬은 독립적인 ( 스레드마다 생성되기 때문에 ) 동시성 이슈가 없다. -> 자원을 공유해서 훼손 할 일이 없기때문.
         */

        String s = Thread.currentThread().getName() + threadLocal.get();
        System.out.println(s);
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            threadLocal.set("스레드 1의 값");
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            threadLocal.remove();
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            threadLocal.set("스레드 2의 값");
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
        }, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        threadLocal.remove();
        System.out.println(threadLocal.get());
    }
}
