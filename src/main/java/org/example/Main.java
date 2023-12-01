package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            while (menu(scanner)) {
            }
        } finally {
            scanner.close();
        }
    }

    private static boolean menu(Scanner scanner) {
        System.out.println("0. Выход");
        System.out.println("1. Решить уравнение");
        System.out.println("2. Вычислительный эксперимент");
        int point = inputInt(scanner, "-> ", p -> p >= 0 && p <= 2);
        switch (point) {
            case 0:
                return false;
            case 1:
                solveEquation(scanner);
                break;
            case 2:
                computationalExperiment(scanner);
                break;
        }
        return true;
    }

    private static int inputInt(Scanner scanner, String message, Predicate<Integer> predicate) {
        int result;
        while (true) {
            try {
                System.out.println(message);
                result = Integer.parseInt(scanner.nextLine());
                if (predicate.test(result)) {
                    break;
                } else {
                    System.out.println("Некорректный ввод");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод");
            }
        }
        return result;
    }

    private static void solveEquation(Scanner scanner) {
        System.out.println("Считывание данных");
        System.out.println("1. Из консоли");
        System.out.println("2. Из файла");
        int point = inputInt(scanner, "-> ", p -> p >= 1 && p <= 2);
        TridiagonalMatrix matrix = null;
        Vector vector = null;
        switch (point) {
            case 1:
                readFromConsole(scanner, matrix, vector);
                break;
            case 2:
                readFromFile(scanner, matrix, vector);
                break;
            default:
                readFromConsole(scanner, matrix, vector);
                break;
        }
        System.out.println("Решение методом прогонки: " + Solve.tridiagonalMatrixAlgorithm(matrix, vector).toString());
        System.out.println("Решение неустойчивым методом: " + Solve.unstableSolve(matrix, vector).toString());
    }

    private static void readFromConsole(Scanner scanner, TridiagonalMatrix matrix, Vector vector) {
        System.out.println("Введите верхнюю диагональ:");
        Vector topCodiagonal = Vector.parse(scanner.nextLine());
        System.out.println("Введите главную диагональ:");
        Vector mainDiagonal = Vector.parse(scanner.nextLine());
        System.out.println("Введите нижнюю диагональ:");
        Vector bottomCodiagonal = Vector.parse(scanner.nextLine());
        if (topCodiagonal.getLength() != mainDiagonal.getLength() || mainDiagonal.getLength() != bottomCodiagonal.getLength()) {
            throw new RuntimeException("Диагонали имеют разные размеры");
        }
        matrix = new TridiagonalMatrix(topCodiagonal, mainDiagonal, bottomCodiagonal);

        System.out.println("Введите правую часть: ");
        vector = Vector.parse(scanner.nextLine());
    }

    private static void readFromFile(Scanner scanner, TridiagonalMatrix matrix, Vector vector) {
        System.out.println("Введите название файла");
        String fileName = scanner.nextLine();
        while (!new File(fileName).exists()) {
            System.out.println("Файл не найден, повторите ввод");
            fileName = scanner.nextLine();
        }

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            String[] lines = {fileScanner.nextLine(), fileScanner.nextLine(), fileScanner.nextLine()};
            matrix = TridiagonalMatrix.parse(lines);
            vector = Vector.parse(fileScanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void computationalExperiment(Scanner scanner) {
        System.out.println("Введите нижнюю границу диапазона:");
        int lowBorder = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите верхнюю границу диапазона:");
        int upBorder = Integer.parseInt(scanner.nextLine());
        printTable(lowBorder, upBorder);
    }

    private static void printTable(int lowBorder, int upBorder) {
        System.out.println("Хорошо обусловленная матрица");
        System.out.printf("%-6s|%-23s|%-20s%n", "Размер", "Прогонка", "Неустойчивый");
        int size = 2;
        for (int i = 0; i < 11; i++) {
            TridiagonalMatrix matrix = TridiagonalMatrix.createRandomWellConditionMatrix(size, lowBorder, upBorder);
            Vector solution = Vector.createRandVector(size, lowBorder, upBorder);
            double traditionalError = solution.subtract(Solve.tridiagonalMatrixAlgorithm(matrix, matrix.multiply(solution))).norm();
            double unstableError = solution.subtract(Solve.unstableSolve(matrix, matrix.multiply(solution))).norm();
            System.out.printf("%-6d|%-22e|%-20e%n", size, traditionalError, unstableError);
            size *= 2;
        }
        System.out.println();
        System.out.println("Плохо обусловленная матрица");
        System.out.printf("%-6s|%-23s|%-20s%n", "Размер", "Прогонка", "Неустойчивый");
        size = 2;
        for (int i = 0; i < 11; i++) {
            TridiagonalMatrix matrix = TridiagonalMatrix.createRandomPoorlyConditionMatrix(size, lowBorder, upBorder);
            Vector solution = Vector.createRandVector(size, lowBorder, upBorder);
            double traditionalError = solution.subtract(Solve.tridiagonalMatrixAlgorithm(matrix, matrix.multiply(solution))).norm();
            double unstableError = solution.subtract(Solve.unstableSolve(matrix, matrix.multiply(solution))).norm();
            System.out.printf("%-6d|%-22e|%-20e%n", size, traditionalError, unstableError);
            size *= 2;
        }
    }
}
