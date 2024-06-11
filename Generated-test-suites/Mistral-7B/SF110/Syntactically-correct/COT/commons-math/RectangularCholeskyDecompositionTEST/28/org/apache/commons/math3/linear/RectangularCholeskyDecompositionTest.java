package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.SingularValueDelegator;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.408248290463863, 0.0}, {0.0, 0.408248290463863, 0.408248290463863}});
        SingularValueDelegator singularValueDelegator = new SingularValueDelegator(expectedRootMatrix);
        for (int i = 0; i < rootMatrix.getRowDimension(); i++) {
            double[] row = rootMatrix.getRowVector(i);
            double[] expectedRow = singularValueDelegator.getColumnVector(i);
            for (int j = 0; j < rootMatrix.getColumnDimension(); j++) {
                assertEquals(Precision.DOUBLE_EPSILON, row[j], expectedRow[j], 1e-10);
            }
        }
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}, {4.0, 6.0, 7.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(4, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decompression = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decompression.getRootMatrix();
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.408248290463863, 0.0}, {0.0, 0.408248290463863, 0.408248290463863}});
        SingularValueDelegator singularValueDelegator = new SingularValueDelegator(expectedRootMatrix);
        for (int i = 0; i < rootMatrix.getRowDimension(); i++) {
            double[] row = rootMatrix.getRowVector(i);
            double[] expectedRow = singularValueDelegator.getColumnVector(i);
            for (int j = 0; j < rootMatrix.getColumnDimension(); j++) {
                assertEquals(Precision.DOUBLE_EPSILON, row[j], expectedRow[j], 1e-10);
            }
        }
    }
}