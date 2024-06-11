package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testValidConstruction() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.5, 1.0, 0.0}, {0.25, 0.5, 1.0}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstruction() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}, {0.0, 0.0, -1.0}});
        new RectangularCholeskyDecomposition(inputMatrix); //Exception should be thrown here
    }
}