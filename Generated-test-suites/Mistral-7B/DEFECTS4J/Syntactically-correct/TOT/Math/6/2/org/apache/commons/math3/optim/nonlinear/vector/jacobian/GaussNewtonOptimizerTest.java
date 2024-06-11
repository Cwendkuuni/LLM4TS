package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;
    private double[] targetValues;
    private double[] expectedPoint;
    private double[] expectedResiduals;

    @Before
    public void setUp() {
        weightMatrix = new QRDecomposition(new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}})).getSolver();
        targetValues = new double[]{1.0, 1.0};
        expectedPoint = new double[]{1.5, 2.5};
        expectedResiduals = new double[]{0.0, 0.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
    }

    @Test
    public void testOptimize_SimpleVectorValueChecker() {
        optimizer.setObjectiveFunction(new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[2];
                gradient[0] = 2.0 * (x[0] - 1.0);
                gradient[1] = 2.0 * (x[1] - 1.0);
                return gradient;
            }
        });
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        PointVectorValuePair result = optimizer.optimize();
        assertEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(expectedResiduals, result.getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence() {
        optimizer.setObjectiveFunction(new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0), 0.5) / 10.0;
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[2];
                gradient[0] = 2.0 * (x[0] - 1.0) / 10.0;
                gradient[1] = 2.0 * (x[1] - 1.0) / 10.0;
                return gradient;
            }
        });
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        optimizer.optimize();
    }

    @Test
    public void testOptimize_LUDecomposition() {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setObjectiveFunction(new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[2];
                gradient[0] = 2.0 * (x[0] - 1.0);
                gradient[1] = 2.0 * (x[1] - 1.0);
                return gradient;
            }
        });
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        PointVectorValuePair result = optimizer.optimize();
        assertEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(expectedResiduals, result.getValue(), 0.001);
    }

    @Test
    public void testOptimize_NullArguments() {
        try {
            new GaussNewtonOptimizer(null);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer.optimize(null);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimize_EmptyTarget() {
        optimizer.setObjectiveFunction(new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        });
        optimizer.setStartPoint(new double[0]);
        optimizer.setWeight(new RealMatrix());
        PointVectorValuePair result = optimizer.optimize();
        assertNull(result);
    }

    interface ObjectiveFunction {
        double value(double[] x);

        double[] gradient(double[] x);
    }
}