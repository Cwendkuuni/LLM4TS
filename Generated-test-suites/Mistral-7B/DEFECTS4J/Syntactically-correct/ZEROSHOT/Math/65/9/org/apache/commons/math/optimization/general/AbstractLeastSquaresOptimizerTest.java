package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSparseMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize() should not be called directly");
                return null;
            }
        };

        // Test default settings
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());

        // Test setting maximum iterations
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());

        // Test setting convergence checker
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        assertEquals(new SimpleVectorialValueChecker(1e-6), optimizer.getConvergenceChecker());

        // Test setting maximum evaluations
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());

        // Test updateJacobian() method
        UnivariateFunction function = PolynomialFunctions.QUADRATIC;
        RealMatrix jacobian = new Array2DRowRealMatrix(new double[][]{{function.valueDerivative(1.0),
                                                                          function.valueDerivative(2.0)},
                                                                       {function.valueDerivative(1.0),
                                                                        function.valueDerivative(2.0)}});
        try {
            optimizer.updateJacobian();
            assertArrayEquals(jacobian.getData(), optimizer.jacobian);
        } catch (FunctionEvaluationException e) {
            fail("updateJacobian() should not throw an exception");
        }

        // Test updateResidualsAndCost() method
        double[] target = new double[]{function.value(1.0), function.value(2.0)};
        double[] weights = new double[]{1.0, 1.0};
        double[] point = new double[]{1.5, 1.5};
        double[] residuals = new double[]{0.0, 0.0};
        try {
            optimizer.updateResidualsAndCost();
            assertEquals(Math.sqrt(2 * (Math.pow(target[0] - point[0], 2) + Math.pow(target[1] - point[1], 2))),
                         optimizer.cost, 1e-6);
            assertArrayEquals(target, optimizer.objective);
            assertArrayEquals(residuals, optimizer.residuals);
        } catch (FunctionEvaluationException e) {
            fail("updateResidualsAndCost() should not throw an exception");
        }

        // Test getRMS() method
        double[] target2 = new double[]{0.1, 0.1, 0.1, 0.1, 0.1};
        double[] weights2 = new double[]{1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0};
        double[] point2 = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        double[] residuals2 = new double[]{-0.02, 0.01, -0.03, 0.04, 0.01};
        try {
            optimizer.setMaxEvaluations(10);
            optimizer.optimize(new PolynomialFunctions.QuadraticFunction(), target2, weights2, point2);
            optimizer.updateResidualsAndCost();
            assertEquals(Math.sqrt(0.003125), optimizer.getRMS(), 1e-6);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("getRMS() should not throw an exception");
        }

        // Test getChiSquare() method
        try {
            optimizer.setMaxEvaluations(10);
            optimizer.optimize(new PolynomialFunctions.QuadraticFunction(), target2, weights2, point2);
            optimizer.updateResidualsAndCost();
            assertEquals(5.0 * (Math.pow(residuals2[0], 2) / weights2[0] + Math.pow(residuals2[1], 2) / weights2[1] +
                                  Math.pow(residuals2[2], 2) / weights2[2] + Math.pow(residuals2[3], 2) / weights2[3] +
                                  Math.pow(residuals2[4], 2) / weights2[4]), optimizer.getChiSquare(), 1e-6);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("getChiSquare() should not throw an exception");
        }

        // Test getCovariances() method
        try {
            optimizer.setMaxEvaluations(15);
            optimizer.optimize(new PolynomialFunctions.QuadraticFunction(), target2, weights2, point2);
            optimizer.updateJacobian();
            RealMatrix covariances = optimizer.getCovariances();
            RealMatrix expectedCovariances = new RealArray2DSparseMatrix(new double[][]{{0.003333333333333333, 0.0,
                                                                                           0.0, 0.0, 0.0},
                                                                                           {0.0, 0.03333333333333333,
                                                                                            0.0, 0.0, 0.0},
                                                                                           {0.0, 0.0, 0.03333333333333333,
                                                                                            0.0, 0.0},
                                                                                           {0.0, 0.0, 0.0, 0.003333333333333333,
                                                                                            0.0},
                                                                                           {0.0, 0.0, 0.0, 0.0, 0.013333333333333333}});
            RealMatrix expectedInverse = new LUDecompositionImpl(expectedCovariances).getSolver().getInverse();
            RealMatrix inverse = new LUDecompositionImpl(covariances).getSolver().getInverse();
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    assertEquals(expectedInverse.getEntry(i, j), inverse.getEntry(i, j), 1e-6);
                }
            }
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("getCovariances() should not throw an exception");
        }

        // Test guessParametersErrors() method
        try {
            optimizer.setMaxEvaluations(15);
            optimizer.optimize(new PolynomialFunctions.QuadraticFunction(), target2, weights2, point2);
            optimizer.updateJacobian();
            double[] errors = optimizer.guessParametersErrors();
            double[] expectedErrors = new double[]{Math.sqrt(0.003333333333333333) / Math.sqrt(5.0),
                                                   Math.sqrt(0.003333333333333333) / Math.sqrt(5.0),
                                                   Math.sqrt(0.003333333333333333) / Math.sqrt(5.0),
                                                   Math.sqrt(0.003333333333333333) / Math.sqrt(5.0),
                                                   Math.sqrt(0.013333333333333333) / Math.sqrt(5.0)};
            assertArrayEquals(expectedErrors, errors, 1e-6);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("guessParametersErrors() should not throw an exception");
        }

    }
}