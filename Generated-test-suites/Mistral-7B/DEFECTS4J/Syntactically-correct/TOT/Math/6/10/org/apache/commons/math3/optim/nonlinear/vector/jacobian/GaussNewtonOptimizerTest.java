package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        try {
            new GaussNewtonOptimizer();
        } catch (Exception e) {
            fail("Default constructor should not throw any exception.");
        }
    }

    /**
     * Test the constructor with LU decomposition.
     */
    @Test
    public void testLUConstructor() {
        try {
            ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
            assertNotNull(optimizer);
        } catch (Exception e) {
            fail("LU constructor should not throw any exception.");
        }
    }

    /**
     * Test the constructor with QR decomposition.
     */
    @Test
    public void testQRConstructor() {
        try {
            ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
            assertNotNull(optimizer);
        } catch (Exception e) {
            fail("QR constructor should not throw any exception.");
        }
    }

    /**
     * Test the optimize method with LU decomposition and a simple example.
     */
    @Test
    public void testOptimizeLU() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.optimize();

        double[] expectedPoint = {1.0 / 3.0, 1.0 / 2.0, 1.0 / 3.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};

        assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        assertArrayEquals(expectedObjective, result.getValue(), 0.001);
    }

    /**
     * Test the optimize method with QR decomposition and a simple example.
     */
    @Test
    public void testOptimizeQR() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.optimize();

        double[] expectedPoint = {1.0 / 3.0, 1.0 / 2.0, 1.0 / 3.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};

        assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        assertArrayEquals(expectedObjective, result.getValue(), 0.001);
    }

    /**
     * Test the optimize method with an incorrect convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullChecker() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        // Setting the convergence checker to null.
        ConvergenceChecker<PointVectorValuePair> checker = null;

        ConvergenceChecker<PointVectorValuePair> oldChecker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, oldChecker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        optimizer.setConvergenceChecker(checker);
        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect target values and weights.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullTargetOrWeights() {
        double[] targetValues = null;
        double[] weights = null;

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);

        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(new double[3]);

        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullStartPoint() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));

        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjectiveFunction() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);

        optimizer.setObjectiveFunction(null);
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullWeight() {
        double[] targetValues = {1, 2, 3};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));

        optimizer.setWeight(null);
        optimizer.setStartPoint(startPoint);

        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullConvergenceChecker() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = null;
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        optimizer.optimize();
    }

    /**
     * Test the optimize method with incorrect useLU flag.
     */
    @Test
    public void testOptimizeIncorrectUseLU() {
        double[] targetValues = {1, 2, 3};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0, 0, 0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer("incorrect", checker);
        optimizer.setObjectiveFunction(new LeastSquaresProblemFunction(targetValues));
        optimizer.setWeight(new DiagonalMatrix(weights));
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.optimize();
            fail("Should throw an exception with an incorrect useLU flag.");
        } catch (NullPointerException e) {
            // Expected exception.
        }
    }

    /**
     * Test the least squares problem function.
     */
    private static class LeastSquaresProblemFunction implements AbstractLeastSquaresOptimizer.ObjectiveFunction {

        private final double[] targetValues;

        public LeastSquaresProblemFunction(double[] targetValues) {
            this.targetValues = targetValues;
        }

        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < targetValues.length; i++) {
                double residual = targetValues[i] - computeResidual(point, i);
                sum += residual * residual;
            }
            return sum;
        }

        @Override
        public double residual(int index, double[] point) {
            return targetValues[index] - computeResidual(point, index);
        }

        private double computeResidual(double[] point, int index) {
            RealMatrix weightMatrix = new DiagonalMatrix(new double[] {
                    1.0, 1.0, 1.0
            });
            RealMatrix jacobian = new Array2DRowRealMatrix(new double[][] {
                    { 1, 1, 1 },
                    { 1, 2, 1 },
                    { 1, 1, 2 }
            });

            double[] x = new double[] {point[0], point[1], point[2]};
            RealMatrix xVector = new ArrayRealVector(x, false);

            RealMatrix jacobianTranspose = jacobian.transpose();
            RealMatrix JTWJ = jacobianTranspose.multiply(weightMatrix).multiply(jacobian);
            RealMatrix X = xVector.multiply(xVector.transpose());

            RealMatrix inverse = new LUDecomposition(JTWJ).getSolver().solve(X);
            double[] solution = inverse.operate(new ArrayRealVector(new double[]{0, 0, 1}));
            return x[index] - solution[0];
        }
    }
}