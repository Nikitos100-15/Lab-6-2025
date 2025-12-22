package functions;

import java.io.*;

import java.util.StringTokenizer;
public class TabulatedFunctions {

    // запрещаем создание объектов
    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создавать объекты класса TabulatedFunctions");
    }

    public static TabulatedFunction tabulate(Function function, double leftX,  double rightX, int pointsCount) {
        // проверки параметров
        if (function == null) {
            throw new IllegalArgumentException("функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount должен быть >= 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("leftX должен быть < rightX");
        }
        // проверка, что отрезок в области определения функции
        if (leftX < function.getLeftDomainBorder() ||  rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Отрезок табулирования [" + leftX + ", " + rightX + "] " +  "выходит за область определения функции [" + function.getLeftDomainBorder() + ", " +  function.getRightDomainBorder() + "]"
            );
        }

        // создаем массивы для точек
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];

        // ищем шаг
        double step = (rightX - leftX) / (pointsCount - 1);

        // заполняем массивы
        for (int i = 0; i < pointsCount; i++) {
            xValues[i] = leftX + i * step;
            yValues[i] = function.getFunctionValue(xValues[i]);
        }
        // возвращаем табулированную функцию
        return new ArrayTabulatedFunction(leftX, rightX, yValues);
    }
        // 7 ЗАДАНИЕ МЕТОДЫ ВВОДА/ВЫВОДА

        public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)
                throws IOException {
            // try-with-resources автоматически закроет поток
            try (DataOutputStream dos = new DataOutputStream(out)) {
                // записываем количество точек
                int pointsCount = function.getPointsCount();
                dos.writeInt(pointsCount);

                // записываем координаты всех точек
                for (int i = 0; i < pointsCount; i++) {
                    dos.writeDouble(function.getPointX(i));
                    dos.writeDouble(function.getPointY(i));
                }
            }
        }
        public static TabulatedFunction inputTabulatedFunction(InputStream in)
                throws IOException {
            try (DataInputStream dis = new DataInputStream(in)) {
                // читаем количество точек
                int pointsCount = dis.readInt();

                // также создаем массивыы для точке
                double[] xValues = new double[pointsCount];
                double[] yValues = new double[pointsCount];

                // и также чиитаем координаты всех точек
                for (int i = 0; i < pointsCount; i++) {
                    xValues[i] = dis.readDouble();  // читаем X
                    yValues[i] = dis.readDouble();  // читаем Y
                }
                // Можно выбрать любую реализацию - здесь ArrayTabulatedFunction
                return new ArrayTabulatedFunction(xValues[0], xValues[pointsCount-1], yValues);
            }
            // поток  закрывается благодаря try-with-resources
        }

        public static void writeTabulatedFunction(TabulatedFunction function, Writer out)
                throws IOException {
            try (BufferedWriter bw = new BufferedWriter(out)) {
                // 1. Записываем количество точек на отдельной строке
                int pointsCount = function.getPointsCount();
                bw.write(String.valueOf(pointsCount));
                bw.newLine();  // переходим на новую строку

                // записываем каждую точку на отдельной строке
                for (int i = 0; i < pointsCount; i++) {
                    bw.write(function.getPointX(i) + " " + function.getPointY(i));
                    bw.newLine();
                }
            }
        }

        public static TabulatedFunction readTabulatedFunction(Reader in)
                throws IOException {

            try (BufferedReader br = new BufferedReader(in)) {
                // читаем первую строку - количество точек
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Нет данных в потоке");
                }
                int pointsCount = Integer.parseInt(line.trim());

                // создаем массивы для координат
                double[] xValues = new double[pointsCount];
                double[] yValues = new double[pointsCount];

                // читаем строки с координатами точек
                for (int i = 0; i < pointsCount; i++) {
                    line = br.readLine();
                    if (line == null) {
                        throw new IOException("Недостаточно данных: ожидалось " + pointsCount + " точек, получено " + i);
                    }
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    if (tokenizer.countTokens() < 2) {
                        throw new IOException("Неверный формат строки: " + line);
                    }

                    // парсим координаты
                    xValues[i] = Double.parseDouble(tokenizer.nextToken());
                    yValues[i] = Double.parseDouble(tokenizer.nextToken());
                }

                // создаем и возвращаем функцию
                return new ArrayTabulatedFunction(xValues[0], xValues[pointsCount-1], yValues);
            }
        }
    }
