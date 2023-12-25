package org.example;



/**@Author Kirill Apasov
 *  Класс содержит методы для решения систем
 *  линейных уравнений.
 */
class Solve {

    //Метод для решения системы линейных уравнений с трехдиагональной матрицей методом прогонки.
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
    //Метод для решения СЛАУ, неустойчивым методом
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
        y.setValue(2, c.getValue(1) != 0 ? d.getValue(1) / c.getValue(1) : Double.NaN);

        for (int i = 2; i <= n - 1; ++i) {
            double den = c.getValue(i) - a.getValue(i) * b.getValue(i - 1);

            if (den == 0) {
                throw new RuntimeException("Деление на ноль в прямом проходе");
            }

            double num = d.getValue(i) - (a.getValue(i) * y.getValue(i - 1)) - (b.getValue(i) * y.getValue(i));
            y.setValue(i + 1, num / den);
        }

        z.setValue(1, 1);
        z.setValue(2, c.getValue(1) != 0 ? -(b.getValue(1) / c.getValue(1)) : Double.NaN);

        for (int i = 2; i <= n - 1; ++i) {
            double den = c.getValue(i) - a.getValue(i) * b.getValue(i - 1);

            if (den == 0) {
                throw new RuntimeException("Деление на ноль в прямом проходе");
            }

            double num = (a.getValue(i) * z.getValue(i - 1)) + (b.getValue(i) * z.getValue(i));
            z.setValue(i + 1, -(num / den));
        }

        double kNum = d.getValue(n) - a.getValue(n) * y.getValue(n - 1) - b.getValue(n) * y.getValue(n);
        double kDen = c.getValue(n) - a.getValue(n) * b.getValue(n - 1);

        if (kDen == 0) {
            throw new RuntimeException("Деление на ноль в прямом проходе");
        }

        double K = kNum / kDen;
        return y.add(z.multiply(K));
    }



}
