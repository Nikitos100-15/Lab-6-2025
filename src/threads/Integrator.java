package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore; //семафор для синхронизации доступа
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    // основной метод потока
    public void run() {
        int task_count = 0;//  счетчик обработанных задач

        try {
            // работает пока поток не прервали
            while (task_count < 100 && !isInterrupted()) {
                try {
                    // захватываем семафор
                    semaphore.acquire();

                    if (task.getFunction() != null) {
                        // чтение параметров задачи, сгенерированных Generator
                        double leftX = task.getLeftX();
                        double rightX = task.getRightX();
                        double step = task.getStep();
                        // вычисление интеграла функции на заданном интервале
                        double integral = Functions.Integral(task.getFunction(), leftX, rightX, step);
                        // вывод результата вычислений
                        System.out.println("Result " + leftX + " " + rightX + " " + step + " " + integral);

                        task.setFunction(null); // очищаем
                        task_count++;
                    }

                } finally {
                    semaphore.release();
                }
                // пауза между вычислениями
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    if (task_count < 100) {
                        System.out.println("Integrator прерван после " + task_count + " задач");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Integrator error: " + e.getMessage());
        }

        System.out.println("Integrator: " + task_count + "/100 задач");
    }
}
