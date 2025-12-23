package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;
    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            // synchronized
            synchronized (task) {
                // генерируем задание
                double base = 1 + Math.random() * 9;
                task.setFunction(new Log(base));
                task.setLeftX(Math.random() * 100);
                task.setRightX(100 + Math.random() * 100);
                task.setStep(Math.random());
                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());

                task.notifyAll();
                // ждем пока интегратор обработает
                try {
                    task.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Выполнено " + task.getTasksCount() + " заданий.");
    }
}