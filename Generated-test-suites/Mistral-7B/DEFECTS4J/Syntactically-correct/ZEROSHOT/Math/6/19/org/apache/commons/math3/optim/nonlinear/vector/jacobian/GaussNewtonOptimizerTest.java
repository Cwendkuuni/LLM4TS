import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.UnconstrainedMinimizer;
import org.apache.commons.math3.test.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        UnsupportedOperationException exception = null;
        try {
            optimizer.minimize(new RealVector[]{}, new double[]{});
        } catch (Exception e) {
            exception = (UnsupportedOperationException) e;
        }
        Assert.assertNotNull(exception);

        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 2.0});
        double[] targetValues = new double[]{4.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        });
        OptimizationProblem problem = new LeastSquaresProblem(targetValues, startPoint, weightMatrix);
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer(true);
        PointVectorValuePair result = gnOptimizer.minimize(problem);

        RealVector expectedPoint = new ArrayRealVector(new double[]{2.5, 1.5});
        double expectedCost = 0.5;
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(expectedCost, result.getValue(), 1e-6);

        // Test with QR Decomposition
        GaussNewtonOptimizer qrOptimizer = new GaussNewtonOptimizer(false);
        result = qrOptimizer.minimize(problem);
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(expectedCost, result.getValue(), 1e-6);

        // Test with wrong start point
        RealVector startPointWrong = new ArrayRealVector(new double[]{1.0, 3.0});
        GaussNewtonOptimizer wrongStartPointOptimizer = new GaussNewtonOptimizer(true);
        PointVectorValuePair wrongStartPointResult = wrongStartPointOptimizer.minimize(problem);
        assertNull(wrongStartPointResult);

        // Test convergence checker
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1e-4, 1e-4);
        GaussNewtonOptimizer checkerOptimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        result = checkerOptimizer.minimize(problem);
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(expectedCost, result.getValue(), 1e-6);
        assertTrue(convergenceChecker.isConverged(0, previous(result), result));

        // Test with null convergence checker
        GaussNewtonOptimizer nullCheckerOptimizer = new GaussNewtonOptimizer(true, null);
        try {
            nullCheckerOptimizer.minimize(problem);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }

        // Test with singular weight matrix
        RealMatrix singularWeightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        OptimizationProblem singularWeightProblem = new LeastSquaresProblem(targetValues, startPoint, singularWeightMatrix);
        GaussNewtonOptimizer singularWeightOptimizer = new GaussNewtonOptimizer(true);
        try {
            singularWeightOptimizer.minimize(singularWeightProblem);
            Assert.fail();
        } catch (SingularMatrixException e) {
            // expected
        }

        // Test with non-square weight matrix
        RealMatrix nonSquareWeightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 1.0}
        });
        OptimizationProblem nonSquareWeightProblem = new LeastSquaresProblem(targetValues, startPoint, nonSquareWeightMatrix);
        try {
            new GaussNewtonOptimizer(true).minimize(nonSquareWeightProblem);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with convergence after a large number of iterations
        ConvergenceChecker<PointVectorValuePair> slowConvergenceChecker = new SimpleVectorValueChecker(1e-10, 1e-10);
        GaussNewtonOptimizer slowConvergenceOptimizer = new GaussNewtonOptimizer(true, slowConvergenceChecker);
        result = slowConvergenceOptimizer.minimize(problem);
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(expectedCost, result.getValue(), 1e-6);
        assertTrue(slowConvergenceChecker.isConverged(iterations(result), previous(result), result));
    }

    private int iterations(PointVectorValuePair pointVectorValuePair) {
        return ((GaussNewtonOptimizer) pointVectorValuePair.getOptimizer()).getIterationCount();
    }

    private PointVectorValuePair previous(PointVectorValuePair pointVectorValuePair) {
        if (pointVectorValuePair.hasPrevious()) {
            return pointVectorValuePair.previous();
        } else {
            return null;
        }
    }

    private static class LeastSquaresProblem implements OptimizationProblem {

        private final double[] targetValues;
        private final RealVector startPoint;
        private final RealMatrix weightMatrix;

        public LeastSquaresProblem(double[] targetValues, RealVector startPoint, RealMatrix weightMatrix) {
            this.targetValues = targetValues;
            this.startPoint = startPoint;
            this.weightMatrix = weightMatrix;
        }

        @Override
        public RealVector getStartPoint() {
            return startPoint;
        }

        @Override
        public double[] getTarget() {
            return targetValues;
        }

        @Override
        public RealMatrix getWeight() {
            return weightMatrix;
        }

        @Override
        public Funnctor getObjectiveFunction() {
            return new Funnctor() {
                @Override
                public double value(RealVector x) {
                    return MathArrays.subtract(targetValues, weightMatrix.operate(x));
                }

                @Override
                public RealVector gradient(RealVector x) {
                    return weightMatrix.transpose().operate(MathArrays.subtract(targetValues, weightMatrix.operate(x)));
                }
            };
        }
    }
}