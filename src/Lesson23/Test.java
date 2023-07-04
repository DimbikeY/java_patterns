package Lesson23;
import java.util.LinkedList;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    pc.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        consumer.start();
    }
}

class ProducerConsumer{
    private Queue<Integer> queue = new LinkedList<>(); // it's not thread-safe class (not Blocking Queue) there are no such features like put and take (thread safe)
    private final int LIMIT = 10;
    private final Object lock = new Object();
    private int digit = 0;
    public void produce() throws InterruptedException {
        while(true){
            synchronized (lock){
                if(queue.size() == 10){
                    lock.wait();
                }
                System.out.println("The added number is " + digit);
                queue.offer(digit++);
                lock.notify();
            }
            Thread.sleep(500);
        }
    }
    public void consume() throws InterruptedException {
        while(true){
            synchronized (lock){
                if(queue.size() == 0){
                    lock.wait();
                }
                System.out.println("Deleted number is equal to " + queue.poll());
                lock.notify();
            }
            // Пишем вне блока, чтобы освободить монитор блока и отдать его produce
            Thread.sleep(100);
        }
    }
}
