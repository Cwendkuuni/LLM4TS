
package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition);

        double[][] nonPositiveData = {{1.0, -1.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, -6.0}};
        RealMatrix nonPositiveMatrix = MatrixUtils.createRealMatrix(nonPositiveData);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(nonPositiveMatrix));
    }

    @Test
    public void testConstructorWithSmall() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition);

        double[][] nonPositiveData = {{1.0, -1.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, -6.0}};
        RealMatrix nonPositiveMatrix = MatrixUtils.createRealMatrix(nonPositiveData);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(nonPositiveMatrix, small));
    }

    @Test
    public void testGetRootMatrixAndRank() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(1.0), 0.0}, {0.714285714, 1.0 / Math.sqrt(5.0)}, {0.285714286, 0.577350269}});
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);

        int expectedRank = 3;
        int rank = decomposition.getRank();
        assertEquals(expectedRank, rank);

        double[][] nonPositiveData = {{1.0, -1.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, -6.0}};
        RealMatrix nonPositiveMatrix = MatrixUtils.createRealMatrix(nonPositiveData);
        RectangularCholeskyDecomposition nonPositiveDecomposition = new RectangularCholeskyDecomposition(nonPositiveMatrix);
        int rank2 = nonPositiveDecomposition.getRank();
        assertEquals(0, rank2);
    }
}