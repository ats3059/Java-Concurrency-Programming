package io.concurrency.chapter04.exam03;

public class DaemonThreadLifeCycleExample {
    /*
        사용자 스레드와 데몬스레드의 생명주기 차이
        사용자 스레드는 모든 사용자 스레드가 종료되어야 어플리케이션이 종료된다.
        ThreadPoolExecutor -> 사용자 스레드
        데몬 스레드는 모든 사용자 스레드가 종료된다면 데몬스레드도 종료된다.
        ForkJoinPool -> 데몬스레드
     */
    public static void main(String[] args) throws InterruptedException {
        Thread userThread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("사용자 스레드 실행 중..");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread daemonThread = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1);
                    System.out.println("데몬 스레드 실행 중..");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        daemonThread.setDaemon(true);
//        daemonThread.setDaemon(false);

        userThread.start();
        daemonThread.start();


        userThread.join();

        System.out.println("메인 스레드 종료");
    }
}
