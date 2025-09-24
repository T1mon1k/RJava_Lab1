import java.util.Random;

/**
 * Лабораторна робота №1.
 * Тема: Масиви в мові програмування Java.
 * Виконав: Співак Артем Михайлович.
 * Група: ІО-35.
 * Варіант: 3520.
 * Завдання:
 * Дія з матрицею(ями): C = aB, де a — константа (C5 = 0).
 * Тип елементів матриці: float (C7 = 6).
 * Дія з матрицею С: Обчислити суму найменших елементів кожного стовпця матриці (C11 = 0).
 */
public class Main {
    /**
     * Точка входу програми.
     * Виконує створення матриць, їх валідацію, множення на скаляр, друк матриць та обчислення суми мінімальних
     * елементів стовпців.
     */
    public static void main(String[] args) {
        try {
            int bRows = 5;      // (Вказуємо) Кількість рядків у матриці B
            int bColumns = 3;   // (Вказуємо) Кількість стовпців у матриці B
            float a = 5.3f;     // (Вказуємо) Змінна a для множення
            float sum;

            validateRowsAndCols(bRows, bColumns);

            float[][] bMatrix = new float[bRows][bColumns];
            float[][] cMatrix = new float[bRows][bColumns];
            validateMatrix(bMatrix);
            validateMatrix(cMatrix);

            generateMatrix(bRows, bColumns, bMatrix);

            multiplyAndRound(bRows, bColumns, a, bMatrix, cMatrix);

            System.out.println("Матриця B:");
            printMatrix(bRows, bColumns, bMatrix);
            System.out.println();
            System.out.println("C = a * B, де a = " + a);
            System.out.println();
            System.out.println("Матриця C:");
            printMatrix(bRows, bColumns, cMatrix);
            System.out.println();

            sum = computeMinsSum(bRows, bColumns, cMatrix);
            System.out.printf("Сума найменших елементів кожного стовпця матриці C: %.2f%n", sum);

        } catch (IllegalArgumentException ex) {
            System.err.println("Помилкові вхідні дані: " + ex.getMessage());
        } catch (ArithmeticException ex) {
            System.err.println("Арифметична помилка: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Непередбачена помилка: " + ex.getMessage());
        }
    }

    /**
     * Перевіряє, що кількість рядків та стовпців більше нуля.
     */
    private static void validateRowsAndCols(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException(
                    "Кількість рядків і стовпців повинна бути > 0 (отримано " + rows + "×" + cols + ")."
            );
        }
    }

    /**
     * Перевіряє, що матриця не є null, жоден її рядок не null та всі рядки мають однакову довжину.
     */
    private static void validateMatrix(float[][] m) {
        if (m == null) {
            throw new IllegalArgumentException("Матриця є null.");
        }
        int cols = m.length > 0 ? m[0].length : 0;
        for (int i = 0; i < m.length; i++) {
            if (m[i] == null) {
                throw new IllegalArgumentException("Рядок " + i + " матриці є null.");
            }
            if (m[i].length != cols) {
                throw new IllegalArgumentException(
                        "Матриця нерівномірна: рядок " + i + " має довжину " + m[i].length + ", а рядок 0 — " +
                                cols + "."
                );
            }
        }
    }

    /**
     * Генерує випадкові значення для матриці у діапазоні [-50; 50) та округлює кожне значення до сотих.
     */
    private static void generateMatrix(int rows, int cols, float[][] m) {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float raw = random.nextFloat() * 100f - 50f; // Генерація в діапазоні (спочатку генерується [0;1), далі
                                                             // множиться на 100 і віднімається 50, тому і виходить
                                                             // [-50; 50) (це, щоб більш-менш не великі числа були)))
                m[i][j] = Math.round(raw * 100f) / 100f;     // Округлення до 2 знаків після коми
                if (!Float.isFinite(m[i][j])) {
                    throw new ArithmeticException(
                            "Неприпустиме значення при генерації B[" + i + "][" + j + "] = " + m[i][j]
                    );
                }
            }
        }
    }

    /**
     * Множить матрицю на скаляр та округлює результат до сотих.
     */
    private static void multiplyAndRound(int rows, int cols, float a, float[][] sMtx, float[][] fMtx) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float raw = a * sMtx[i][j];
                if (!Float.isFinite(raw)) {
                    throw new ArithmeticException("Неприпустиме значення після множення: a * B[" + i + "][" + j +
                            "] = " + raw);
                }
                fMtx[i][j] = Math.round(raw * 100f) / 100f;   // Округлення до 2 знаків після коми
            }
        }
    }

    /**
     * Обчислює суму найменших елементів кожного стовпця матриці.
     */
    private static float computeMinsSum(int rows, int cols, float[][] m) {
        float sum = 0.0f;
        for (int j = 0; j < cols; j++) {
            float min = m[0][j];
            for (int i = 1; i < rows; i++) {
                float v = m[i][j];
                if (!Float.isFinite(v)) {
                    throw new ArithmeticException("Неприпустиме значення в C[" + i + "][" + j + "] = " + v);
                }
                if (v < min) {
                    min = v;
                }
            }
            sum += min;
            if (!Float.isFinite(sum)) {
                throw new ArithmeticException("Переповнення при додаванні мінімумів: sum = " + sum);
            }
        }
        return sum;
    }

    /**
     * Друкує матрицю з вирівнюванням по ширині. Кожне значення форматуються з двома знаками після коми.
     */
    private static void printMatrix(int rows, int cols, float[][] m) {
        int width = 0;
        // Шукаємо потрібну ширину для вирівнювання
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String s = String.format("%.2f", m[i][j]);
                width = Math.max(width, s.length());
            }
        }
        width += 2;   // Додаємо відступ
        // Виводимо матрицю
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%" + width + "s", String.format("%.2f", m[i][j]));
            }
            System.out.println();
        }
    }
}
