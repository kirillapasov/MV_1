package org.example;

class Solve {
    public static Vector tridiagonalMatrixAlgorithm(TridiagonalMatrix matrix, Vector rightPart) {
        if (matrix.getSize() != rightPart.getLength() || matrix.getSize() < 2)
            throw new RuntimeException("Матрица и вектор разных размеров");

        int n = rightPart.getLength();

        Vector d = rightPart;
        Vector c = matrix.getTopCodiagonal();
        Vector b = matrix.getMainDiagonal();
        Vector a = matrix.getBottomCodiagonal();

        Vector L = new Vector(n + 1);
        Vector M = new Vector(n + 1);

        for (int i = 1; i <= n; ++i) {
            double den = b.getValue(i) - (a.getValue(i) * L.getValue(i));
            L.setValue(i + 1, c.getValue(i) / den);
            M.setValue(i + 1, (d.getValue(i) - a.getValue(i) * M.getValue(i)) / den);
        }

        Vector result = new Vector(n);
        result.setValue(n, M.getValue(n + 1));
        for (int i = n - 1; i > 0; i--)
            result.setValue(i, M.getValue(i + 1) - L.getValue(i + 1) * result.getValue(i + 1));
        return result;
    }

    public static Vector unstableSolve(TridiagonalMatrix matrix, Vector rightPart) {
        if (matrix.getSize() != rightPart.getLength() || matrix.getSize() < 2) {
            throw new RuntimeException("Матрица и вектор разных размеров");
        }

        int n = rightPart.getLength();

        Vector d = rightPart;
        Vector c = matrix.getTopCodiagonal();
        Vector b = matrix.getMainDiagonal();
        Vector a = matrix.getBottomCodiagonal();

        Vector y = new Vector(n);
        Vector z = new Vector(n);

        y.setValue(1, 0);
        y.setValue(2, d.getValue(1) / c.getValue(1));
        for (int i = 2; i <= n - 1; ++i) {
            double num = d.getValue(i) - (a.getValue(i) * y.getValue(i - 1)) - (b.getValue(i) * y.getValue(i));
            y.setValue(i + 1, num / c.getValue(i));
        }

        z.setValue(1, 1);
        z.setValue(2, -(b.getValue(1) / c.getValue(1)));
        for (int i = 2; i <= n - 1; ++i) {
            double num = (a.getValue(i) * z.getValue(i - 1)) + (b.getValue(i) * z.getValue(i));
            z.setValue(i + 1, -(num / c.getValue(i)));
        }

        double kNum = d.getValue(n) - a.getValue(n) * y.getValue(n - 1) - b.getValue(n) * y.getValue(n);
        double kDen = a.getValue(n) * z.getValue(n - 1) + b.getValue(n) * z.getValue(n);
        double K = kNum / kDen;
        return y.add(z.multiply(K));
    }
}
