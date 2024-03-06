package io.concurrency.chapter04.exam03;

public class UserAndDaemonInheritanceExample {
    /*
        사용자 스레드에서 스레드를 생성한다면 사용자 스레드가 생성되고
        ex) main 스레드 내부에서 스레드를 생성한다면 main 스레드(사용자 스레드)의 자식 즉 사용자 스레드로 생성됨
        데몬 스레드에서 스레드를 생성한다면 데몬 스레드가 된다

     */
    public static void main(String[] args) {

        // 사용자 스레드 생성
        Thread userThread = new Thread(() -> {
            Thread childOfUserThread = new Thread(() -> {
                System.out.println("사용자 스레드의 자식 스레드의 데몬 상태: " + Thread.currentThread().isDaemon());
            });
            childOfUserThread.start();
            System.out.println("사용자 스레드의 데몬 상태: " + Thread.currentThread().isDaemon());
        });

        // 데몬 스레드 생성
        Thread daemonThread = new Thread(() -> {
            Thread childOfDaemonThread = new Thread(() -> {
                System.out.println("데몬 스레드의 자식 스레드의 데몬 상태: " + Thread.currentThread().isDaemon());
            });
            childOfDaemonThread.start();
            System.out.println("데몬 스레드의 데몬 상태: " + Thread.currentThread().isDaemon());
        });
        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();
    }
}
