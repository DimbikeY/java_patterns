package Lesson27;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(200); // 200 потоков
        Connection connection = Connection.getConnection();
        for(int i = 0; i < 200; i++){
            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.doWork();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.DAYS); // Максимум - данная дата на заморозку основонго потока


    }
}

// Реализуем паттерн проектирования: SingleTon (Без возможности создания объекта класса)
// Его создание - это добавление static объекта + static метода по изъятию объекта
class Connection {
    private static Connection connection = new Connection();
    private Semaphore semaphore = new Semaphore(10); // Максимум 10 потоков разрешённых. Опреации: acqure release
    private Connection(){

    }
    public static Connection getConnection(){
        return connection;
    }
    private void work(){
        System.out.println(semaphore.availablePermits());
    }
    public void doWork() throws InterruptedException {
        semaphore.acquire();
        try{
            work(); // Может выпасть ошибка, которую мы закроем release (освободим запас потоков)
        }finally {
            Thread.sleep(1000);
            semaphore.release();
        }
    }
}
