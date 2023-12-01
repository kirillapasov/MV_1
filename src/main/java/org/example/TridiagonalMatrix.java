package org.example;

import java.util.Random;

class TridiagonalMatrix {
    private final Vector topCodiagonal;
    private final Vector mainDiagonal;
    private final Vector bottomCodiagonal;
    private static final Random rand = new Random();

    public TridiagonalMatrix(Vector topDiagonal, Vector diagonal, Vector bottomDiagonal) {
        mainDiagonal = diagonal.clone();
        topCodiagonal = topDiagonal.clone();
        bottomCodiagonal = bottomDiagonal.clone();

        topCodiagonal.setValue(topCodiagonal.getLength(), 0);
        bottomCodiagonal.setValue(1, 0);
    }


    public Vector getTopCodiagonal() {
        return topCodiagonal;
    }

    public Vector getMainDiagonal() {
        return mainDiagonal;
    }

    public Vector getBottomCodiagonal() {
        return bottomCodiagonal;
    }

    public int getSize() {
        return mainDiagonal.getLength();
    }

    public TridiagonalMatrix add(TridiagonalMatrix other) {
        if (getSize() != other.getSize())
            throw new RuntimeException("Матрицы разных размеров");
        return new TridiagonalMatrix(
                topCodiagonal.add(other.topCodiagonal),
                mainDiagonal.add(other.mainDiagonal),
                bottomCodiagonal.add(other.bottomCodiagonal)
        );
    }

    public TridiagonalMatrix subtract(TridiagonalMatrix other) {
        if (getSize() != other.getSize())
            throw new RuntimeException("Матрицы разных размеров");
        return new TridiagonalMatrix(
                topCodiagonal.subtract(other.topCodiagonal),
                mainDiagonal.subtract(other.mainDiagonal),
                bottomCodiagonal.subtract(other.bottomCodiagonal)
        );
    }

    public Vector multiply(Vector vector) {
        if (vector.getLength() != getSize())
            throw new RuntimeException("Векторы разных длин");
        Vector result = new Vector(vector.getLength());
        result.setValue(1, topCodiagonal.getValue(1) * vector.getValue(2) + mainDiagonal.getValue(1) * vector.getValue(1));
        for (int i = 2; i < getSize(); i++) {
            result.setValue(i, topCodiagonal.getValue(i) * vector.getValue(i + 1)
                    + mainDiagonal.getValue(i) * vector.getValue(i)
                    + bottomCodiagonal.getValue(i) * vector.getValue(i - 1));
        }
        result.setValue(getSize(),
                mainDiagonal.getValue(getSize()) * vector.getValue(getSize())
                        + bottomCodiagonal.getValue(getSize()) * vector.getValue(getSize() - 1));
        return result;
    }

    public static TridiagonalMatrix createRandomMatrix(int size, int lowBorder, int upBorder) {
        Vector topDiagonal = Vector.createRandVector(size, lowBorder, upBorder);
        Vector mainDiagonal = Vector.createRandVector(size, lowBorder, upBorder);
        Vector bottomDiagonal = Vector.createRandVector(size, lowBorder, upBorder);
        topDiagonal.setValue(1, 0);
        bottomDiagonal.setValue(size, 0);
        return new TridiagonalMatrix(topDiagonal, mainDiagonal, bottomDiagonal);
    }

    public static TridiagonalMatrix parse(String[] lines) {
        if (lines.length != 3) {
            throw new RuntimeException("Неверный формат");
        }
        Vector top = Vector.parse(lines[0]);
        Vector main = Vector.parse(lines[1]);
        Vector bottom = Vector.parse(lines[2]);
        return new TridiagonalMatrix(top, main, bottom);
    }


    public static TridiagonalMatrix createRandomWellConditionMatrix(int size, int lowBorder, int upBorder) {
        Vector mainDiagonal = Vector.createRandVector(size, lowBorder, upBorder);
        Vector topDiagonal = new Vector(size);
        Vector bottomDiagonal = new Vector(size);
        for (int i = 1; i <= size; ++i) {
            topDiagonal.setValue(i, rand.nextInt(Math.max((int) mainDiagonal.getValue(i) / 2, lowBorder + 1)));
            bottomDiagonal.setValue(i, rand.nextInt(Math.max((int) mainDiagonal.getValue(i) / 2, lowBorder + 1)));
        }
        topDiagonal.setValue(size, 0);
        bottomDiagonal.setValue(1, 0);
        return new TridiagonalMatrix(topDiagonal, mainDiagonal, bottomDiagonal);
    }

    public static TridiagonalMatrix createRandomPoorlyConditionMatrix(int size, int lowBorder, int upBorder) {
        Vector mainDiagonal = Vector.createRandVector(size, lowBorder, upBorder);
        Vector topDiagonal = new Vector(size);
        Vector bottomDiagonal = new Vector(size);
        for (int i = 1; i <= size; ++i) {
            topDiagonal.setValue(i, rand.nextInt((int) mainDiagonal.getValue(i)));
            bottomDiagonal.setValue(i, rand.nextInt((int) mainDiagonal.getValue(i)));
        }
        topDiagonal.setValue(size, 0);
        bottomDiagonal.setValue(1, 0);
        return new TridiagonalMatrix(topDiagonal, mainDiagonal, bottomDiagonal);
    }


}