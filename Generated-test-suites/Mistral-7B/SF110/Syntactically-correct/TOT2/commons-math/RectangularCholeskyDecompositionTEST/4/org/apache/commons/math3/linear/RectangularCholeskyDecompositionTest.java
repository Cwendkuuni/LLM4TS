import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 0);
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i < j) {
                    matrix.setEntry(i, j, -1.0);
                }
            }
        }
        new RectangularCholeskyDecomposition(matrix);
    }
}