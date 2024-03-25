package io.concurrency.chapter10.exam01;


import java.util.LinkedList;
import java.util.Queue;

public class SimpleThreadPool {
    /*
     */
    private int numThreads;
    private Queue<Runnable> taskQueue;
    private Thread[] threads;
    private volatile boolean isShutdown;

    public SimpleThreadPool(int numThreads) {
        this.numThreads = numThreads;
        this.taskQueue = new LinkedList<>();
        this.threads = new Thread[numThreads];
        this.isShutdown = false;

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public void submit(Runnable task) {
        if (!isShutdown) {
            /*
             taskQueue 내부에 작업을 넣고 현재 taskQueue의 인스턴스 락을 획득하기 위해서 대기중인
             스레드들을 깨운다. 해당 작업을 위해 synchronized 키워드를 사용한다.
             */
            synchronized (taskQueue){
                taskQueue.offer(task);
                taskQueue.notifyAll();
            }
        }
    }

    public void shutdown(){
        isShutdown = true;
        // shutdown 호출 시 taskQueue에서 모든 스레드들을 깨우고 thread.join을 사용하여 모든 스레드들의 작업이 종료될때까지 기다린다 -> 스레드는 작업이 끝났기 때문에 종료.
        synchronized (taskQueue) {
            taskQueue.notifyAll();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown){
                Runnable task;
                synchronized (taskQueue) {
                    // 만약 실행 할 작업이 없다면
                    while (taskQueue.isEmpty() && !isShutdown){
                        try {
                            // taskQueue 인스턴스 모니터에서 조건이 변경될때까지 대기시킨다 -> 작업이 추가되기 전까지.
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                // 잠깐 사이에 작업이 사라질 경우를 위해서 확인하는 작업
                if(!taskQueue.isEmpty()){
                    task = taskQueue.poll();
                }else{
                    continue;
                }
                task.run();
            }
        }
    }

}
