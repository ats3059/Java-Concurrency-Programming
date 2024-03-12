package io.concurrency.chapter07.exam01;

class Parent {
    public synchronized void parentMethod() {
        System.out.println("부모 메서드를 호출함");
    }
}

class Child extends Parent {

    public synchronized void childMethod() {
        /*
         부모 메서드에도 현재 synchronized 메서드 즉 모니터를 사용중이다.
         우리가 기존에 학습할때 객체당 클래스(정적) , 인스턴스 2개의 모니터가 존재한다고 배웠다.
         그럼 지금 자식의 LOCK을 획득한 상황에서 부모에 LOCK도 획득해야할까?
         정답은 아니다. 자바는 상속의 경우에 같은 모니터로 사용하기 때문에 부모의 LOCK을 얻을 필요가 없다.
         */

        System.out.println("자식 메서드를 호출함");
        parentMethod();  // 부모 메서드를 호출
    }
}

public class ReentrantSynchronizedExample {

    public static void main(String[] args) {
        Child child = new Child();

        Thread t1 = new Thread(() -> {
            child.childMethod();
        });

        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
