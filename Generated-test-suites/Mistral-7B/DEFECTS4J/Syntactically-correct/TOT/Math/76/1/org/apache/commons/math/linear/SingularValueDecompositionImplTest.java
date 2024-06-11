package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the Singular Value Decomposition computation for a random matrix.
     */
    @Test
    public void testSingularValueDecomposition() {
        final int size = 5;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0},
            {2.0, -1.0, 4.0, 5.0, 3.0},
            {3.0, 4.0, -1.0, 2.0, 1.0},
            {4.0, 5.0, 2.0, -1.0, -1.0},
            {5.0, 3.0, 1.0, -1.0, -1.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd.getU());
            assertNotNull(svd.getUT());
            assertNotNull(svd.getS());
            assertNotNull(svd.getSingularValues());
            assertNotNull(svd.getV());
            assertNotNull(svd.getVT());
            final double[] expectedSingularValues = new double[]{5.385164806032448, 2.88675585225332, 0.0, 0.0, 0.0};
            assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the computation of pseudo-inverse for a random matrix.
     */
    @Test
    public void testPseudoInverse() {
        final int size = 4;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {2.0, -1.0, 4.0, 5.0},
            {3.0, 4.0, 2.0, 2.0},
            {4.0, 5.0, 2.0, -1.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            final DecompositionSolver solver = svd.getSolver();
            final double[] b = new double[]{1.0, 2.0, 3.0, 4.0};
            final double[] expected = new double[]{0.6153846153846154, 0.2776923076923077,
                    0.1815785714285714, 0.1192028985714285};
            final RealVector x = solver.solve(new ArrayRealVector(b));
            final double[] xData = x.toVector();
            assertArrayEquals(expected, xData, 1e-12);
            final double[] a = new double[]{1.0, 2.0, 3.0, 4.0};
            final double[] expectedProduct = new double[]{5.0, 11.0, 11.0, 13.0};
            final double[] product = new double[a.length];
            matrix.operate(new Array2DRowRealMatrix(new double[][]{x}), product);
            assertArrayEquals(expectedProduct, product, 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the rank of a random matrix.
     */
    @Test
    public void testRank() {
        final int size = 5;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0},
            {2.0, -1.0, 4.0, 5.0, 3.0},
            {3.0, 4.0, -1.0, 2.0, 1.0},
            {4.0, 5.0, 2.0, -1.0, -1.0},
            {5.0, 3.0, 1.0, -1.0, -1.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            final int rank = svd.getRank();
            assertEquals("Unexpected rank", 5, rank);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the Singular Value Decomposition computation for a singular matrix.
     */
    @Test
    public void testSingularValueDecompositionSingular() {
        final int size = 3;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd.getU());
            assertNotNull(svd.getUT());
            assertNotNull(svd.getS());
            assertNotNull(svd.getSingularValues());
            assertNotNull(svd.getV());
            assertNotNull(svd.getVT());
            final double[] expectedSingularValues = new double[]{0.0, 0.0, 0.0};
            assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the computation of pseudo-inverse for a singular matrix.
     */
    @Test
    public void testPseudoInverseSingular() {
        final int size = 3;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            final DecompositionSolver solver = svd.getSolver();
            final double[] b = new double[]{1.0, 2.0, 3.0};
            try {
                solver.solve(new ArrayRealVector(b));
                fail("Exception expected");
            } catch (IllegalStateException e) {
                // expected
            }
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the computation of condition number for a random matrix.
     */
    @Test
    public void testConditionNumber() {
        final int size = 5;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0},
            {2.0, -1.0, 4.0, 5.0, 3.0},
            {3.0, 4.0, -1.0, 2.0, 1.0},
            {4.0, 5.0, 2.0, -1.0, -1.0},
            {5.0, 3.0, 1.0, -1.0, -1.0},
        });
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            final double conditionNumber = svd.getConditionNumber();
            final double[] singularValues = svd.getSingularValues();
            final double singularValue0 = singularValues[0];
            final double singularValueLast = singularValues[singularValues.length - 1];
            final double expectedConditionNumber = singularValue0 / singularValueLast;
            assertEquals("Unexpected condition number", expectedConditionNumber, conditionNumber, 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the Singular Value Decomposition computation for a diagonal matrix.
     */
    @Test
    public void testSingularValueDecompositionDiagonal() {
        final int size = 4;
        final double[] diagonal = new double[]{1.0, 2.0, 3.0, 4.0};
        final RealMatrix matrix = new Array2DRowRealMatrix(diagonal);
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd.getU());
            assertNotNull(svd.getUT());
            assertNotNull(svd.getS());
            assertNotNull(svd.getSingularValues());
            assertNotNull(svd.getV());
            assertNotNull(svd.getVT());
            final double[] expectedSingularValues = new double[]{Statistics.max(diagonal), 0.0, 0.0, 0.0};
            assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the computation of pseudo-inverse for a diagonal matrix.
     */
    @Test
    public void testPseudoInverseDiagonal() {
        final int size = 4;
        final double[] diagonal = new double[]{1.0, 2.0, 3.0, 4.0};
        final RealMatrix matrix = new Array2DRowRealMatrix(diagonal);
        try {
            final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            final DecompositionSolver solver = svd.getSolver();
            final double[] b = new double[]{1.0, 2.0, 3.0, 4.0};
            final double[] expected = new double[]{1.0 / (2.0 * 3.0 * 4.0),
                    2.0 / (3.0 * 4.0 * 4.0),
                    3.0 / (4.0 * 4.0),
                    1.0 / 4.0};
            final RealVector x = solver.solve(new ArrayRealVector(b));
            final double[] xData = x.toVector();
            assertArrayEquals(expected, xData, 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}