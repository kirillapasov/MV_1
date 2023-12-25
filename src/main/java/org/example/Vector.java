package org.example;

import java.util.Random;

/**@Author Kirill Apasov
 *  Класс представляет собой вектор
 *  и содержит методы для работы с векторами.
 */

class Vector {
    private final double[] valueArray;
    private static final Random rand = new Random();

    public int getLength() {
        return valueArray.length;
    }

    public Vector(int size) { //Конструктор для создания вектора заданного размера с элементами, инициализированными нулями.
        valueArray = new double[size];
        for (int i = 0; i < getLength(); ++i)
            setValue(i + 1, 0);
    }

    public Vector() {
        this(10);
    } //Конструктор по умолчанию, создающий вектор длиной 10.

    public Vector(double[] other) { //Конструктор, создающий вектор на основе переданного массива.
        valueArray = new double[other.length];
        System.arraycopy(other, 0, valueArray, 0, getLength());
    }

    public Vector(Vector other) {
        this(other.valueArray);
    } //Конструктор, создающий копию вектора.

    public double getValue(int index) { // Метод для получения значения элемента по индексу.
        if (index < 1 || index > getLength())
            throw new RuntimeException("Выход за пределы вектора!");
        return valueArray[index - 1];
    }

    public void setValue(int index, double value) {//Метод для установки значения элемента по индексу
        if (index < 1 || index > getLength())
            throw new RuntimeException("Выход за пределы вектора!");
        valueArray[index - 1] = value;
    }

    public Vector add(Vector other) { //Метод для сложения двух векторов.
        if (getLength() != other.getLength())
            throw new RuntimeException("Векторы разных размеров!");
        Vector resultVector = new Vector(getLength());
        for (int i = 1; i <= getLength(); ++i)
            resultVector.setValue(i, getValue(i) + other.getValue(i));
        return resultVector;
    }

    public Vector subtract(Vector other) { //Метод для вычитания одного вектора из другого.
        if (getLength() != other.getLength())
            throw new RuntimeException("Векторы разных размеров!");
        Vector resultVector = new Vector(getLength());
        for (int i = 1; i <= getLength(); ++i)
            resultVector.setValue(i, getValue(i) - other.getValue(i));
        return resultVector;
    }

    public Vector multiply(double val) { //Метод для умножения вектора на число
        Vector resultVector = new Vector(getLength());
        for (int i = 1; i <= getLength(); ++i)
            resultVector.setValue(i, getValue(i) * val);
        return resultVector;
    }

    public double norm() { //Метод для вычисления нормы вектора
        double resultNorm = Math.abs(getValue(1));
        for (int i = 2; i <= getLength(); ++i)
            resultNorm = Math.max(resultNorm, Math.abs(getValue(i)));
        return resultNorm;
    }


    public static Vector createRandVector(int length, int lowBorder, int upBorder) { //Метод для создания случайного вектора в заданном диапазоне.
        Vector resultVector = new Vector(length);
        for (int i = 1; i <= length; ++i)
            resultVector.setValue(i, rand.nextInt(upBorder - lowBorder + 1) + lowBorder);
        return resultVector;
    }

    public static Vector parse(String str) { // Метод для создания вектора из строки.
        String[] substr = str.split(" ");
        Vector result = new Vector();
        for (int i = 0; i < substr.length; i++) {
            result.setValue(i + 1, Double.parseDouble(substr[i]));
        }
        return result;
    }

    @Override
    public Vector clone() {
        return new Vector(valueArray);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (double value : valueArray) {
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }
}