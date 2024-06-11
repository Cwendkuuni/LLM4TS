package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.Transformer;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link SingularValueDecompositionImpl}.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the SingularValueDecomposition constructor with a random matrix.
     * @throws Exception if the constructor fails
     */
    @Test
    public void testConstructorWithRandomMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedu = new Array2DRowRealMatrix(
                new double[][]{
                        {0.18752783, -0.75169818, 0.42261908},
                        {-0.58196904, -0.11922033, 0.81649662},
                        {0.79577472, 0.41415034, 0.14213713}
                }
        );
        RealMatrix expecteds = new Array2DRowRealMatrix(
                new double[][]{
                        {3.16227766, 0.0, 0.0},
                        {0.0, 2.58819045, 0.0},
                        {0.0, 0.0, 1.41421371}
                }
        );
        RealMatrix expectedv = new Array2DRowRealMatrix(
                new double[][]{
                        {-0.61542378, -0.78579797, 0.13215233},
                        {0.77519489, 0.08366727, 0.61542378},
                        {0.28865729, -0.65093234, 0.73438887}
                }
        );
        assertEquals(expectedu, u);
        assertEquals(expecteds, s);
        assertEquals(expectedv, v);
    }

    /**
     * Test the SingularValueDecomposition constructor with a singular matrix.
     * @throws Exception if the constructor fails
     */
    @Test
    public void testConstructorWithSingularMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 2},
                        {2, 2, 2},
                        {2, 2, 2}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedu = new Array2DRowRealMatrix(
                new double[][]{
                        {0.37315792, -0.51038808, 0.8115155},
                        {0.8174791, 0.44551343, -0.42862048},
                        {-0.44551343, 0.8115155, 0.37315792}
                }
        );
        RealMatrix expecteds = new Array2DRowRealMatrix(
                new double[][]{
                        {3.53553391, 0.0, 0.0},
                        {0.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0}
                }
        );
        RealMatrix expectedv = new Array2DRowRealMatrix(
                new double[][]{
                        {-0.45363512, 0.81915226, -0.40824829},
                        {-0.81915226, -0.45363512, 0.40824829},
                        {0.40824829, 0.45363512, 0.81915226}
                }
        );
        assertEquals(expectedu, u);
        assertEquals(expecteds, s);
        assertEquals(expectedv, v);
    }

    /**
     * Test the getRank method with a non-square matrix.
     * @throws Exception if the method fails
     */
    @Test
    public void testGetRankWithNonSquareMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    /**
     * Test the getRank method with a square matrix.
     * @throws Exception if the method fails
     */
    @Test
    public void testGetRankWithSquareMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, 3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    /**
     * Test the Solver with a non-square matrix.
     * @throws Exception if the solver fails
     */
    @Test
    public void testSolverWithNonSquareMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        DecompositionSolver solver = svd.getSolver();
        RealVector x = solver.solve(b);
        RealMatrix expectedx = new Array2DRowRealMatrix(
                new double[][]{
                        {-0.04657103, -0.17171253, 0.65192713},
                        {0.02115272, 0.07395127, -0.65192713}
                }
        );
        assertArrayEquals(expectedx.getData(), x.toArray(), 1e-10);
    }

    /**
     * Test the Solver with a square matrix.
     * @throws Exception if the solver fails
     */
    @Test
    public void testSolverWithSquareMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, 3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        DecompositionSolver solver = svd.getSolver();
        RealVector x = solver.solve(b);
        RealMatrix expectedx = new Array2DRowRealMatrix(
                new double[][]{
                        {0.16666667, 0.00828041, -0.02422118},
                        {-0.00828041, 0.16666667, -0.02422118},
                        {0.02422118, 0.02422118, 0.33333333}
                }
        );
        assertArrayEquals(expectedx.getData(), x.toArray(), 1e-10);
    }

    /**
     * Test the Solver with a singular matrix.
     * @throws Exception if the solver fails
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithSingularMatrix()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 2},
                        {2, 2, 2},
                        {2, 2, 2}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1, 2, 2});
        DecompositionSolver solver = svd.getSolver();
        solver.solve(b); // this should throw an exception
    }

    /**
     * Test the getCovariance method with a non-positive singular value.
     * @throws Exception if the method fails
     */
    @Test
    public void testgetCovarianceWithNonPositiveSingularValue()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, -3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedCov = new Array2DRowRealMatrix(
                new double[][]{
                        {0.0, 0.0, 0.0},
                        {0.0, 11.0, 0.0},
                        {0.0, 0.0, 9.0}
                }
        );
        RealMatrix cov = svd.getCovariance(0);
        assertArrayEquals(expectedCov.getData(), cov.getData(), 1e-10);
    }

    /**
     * Test the getCovariance method with a positive singular value.
     * @throws Exception if the method fails
     */
    @Test
    public void testgetCovarianceWithPositiveSingularValue()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, 3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedCov = new Array2DRowRealMatrix(
                new double[][]{
                        {0.0, 0.0, 0.0},
                        {0.0, 2.0, 0.0},
                        {0.0, 0.0, 1.0}
                }
        );
        RealMatrix cov = svd.getCovariance(1);
        assertArrayEquals(expectedCov.getData(), cov.getData(), 1e-10);
    }

    /**
     * Test the getNorm method.
     * @throws Exception if the method fails
     */
    @Test
    public void testGetNorm()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, 3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(14.0) * Math.sqrt(3.0), svd.getNorm(), 1e-10);
    }

    /**
     * Test the getConditionNumber method.
     * @throws Exception if the method fails
     */
    @Test
    public void testGetConditionNumber()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 2, 3},
                        {2, 2, 3},
                        {3, 3, 3}
                }
        );
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(14.0) / Math.sqrt(3.0), svd.getConditionNumber(), 1e-10);
    }

}