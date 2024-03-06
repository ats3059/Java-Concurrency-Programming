package io.concurrency.chapter04.exam02;

public class FlagThreadStopExample {
    /*
     각각의 cpu마다 캐시메모리를 갖고있다 ( 성능을 좋게 하기 위해서 )
     각 쓰레드는 메모리에서 값을 가져오는 것 보다 cpu 캐시 메모리에서 값을 가져오는게 빠르기 때문에
     캐시 메모리에 먼저 반영하고 그리고 메모리에 반영하게 된다.

     방법이 두가지인데 Thread.sleep() 호출 후 문맥전환(컨텍스트 스위칭)이 일어나게 한다 ->
     문맥전환 시 캐시메모리를 비워야하기 때문에 현재 쓰레드 1의 캐시메모리에는 값이 없다.
     그 후 쓰레드 2번이 running의 상태를 false로 변경 후 메모리에 반영
     문맥전환 후 캐시메모리가 비워진 쓰레드 1은 메모리에 접근하여 running 값을 가져오게됨

     방법2
     volatile 키워드 추가 캐시메모리를 사용하지 않고 메모리에서 데이터를 접근하여 사용하겠다는 키워드.

*/
   volatile boolean running = true;
//    boolean running = true;

    public void volatileTest() {
        new Thread(() -> {
            int count = 0;
            while (running) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                count++;
            }
            System.out.println("Thread 1 종료. Count: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            System.out.println("Thread 2 종료 중..");
            running = false;
        }).start();
    }

    public static void main(String[] args) {
        new FlagThreadStopExample().volatileTest();
    }
}
