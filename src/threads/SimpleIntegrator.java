package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            // synchronized
            synchronized (task) {
                try {
                    // ждем пока генератор создаст задание
                    while (task.getFunction() == null) {
                        task.wait();
                    }

                    // берем текущие данные
                    double leftX = task.getLeftX();
                    double rightX = task.getRightX();
                    double step = task.getStep();

                    // вычисляем интеграл
                    double integral = Functions.Integral(task.getFunction(), leftX, rightX, step);
                    System.out.println("Result " + leftX + " " + rightX + " " + step + " " + integral);

                    // очищаем
                    task.setFunction(null);
                    task.notifyAll();

                } catch (Exception e) {
                    System.out.println("Integrator error: " + e.getMessage());
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}