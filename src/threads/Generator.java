package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore; // семафор для синхронизации доступа

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    // основной метод потока
    public void run() {
        Random random = new Random();// генератор случайных чисел
        int count = 0;//  счетчик для  100 задач

        try {
            while (count < 100 && !isInterrupted()) {
                try {
                    // захватываем семафор
                    semaphore.acquire();
                    if (task.getFunction() != null) {
                        semaphore.release();
                        Thread.sleep(1);
                        continue;
                    }

                    // генерация случайных параметров для задачи:
                    double a = random.nextDouble() * 9 + 1;
                    task.setFunction(new Log(a));
                    task.setLeftX(random.nextDouble() * 100);
                    task.setRightX(100 + random.nextDouble() * 100);
                    task.setStep(random.nextDouble());

                    System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());
                    count++;

                } finally {
                    // всегда освобождаем семафор
                    semaphore.release();
                }
                // пауза между генерациями
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    if (count < 100) {
                        System.out.println("Generator прерван после " + count + " задач");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Generator error: " + e.getMessage());
        }

        System.out.println("Generator: " + count + "/100 задач");
    }
}