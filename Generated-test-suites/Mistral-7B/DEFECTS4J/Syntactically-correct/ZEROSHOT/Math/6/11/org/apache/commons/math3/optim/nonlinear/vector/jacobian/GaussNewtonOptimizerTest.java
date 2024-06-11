import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonNegativeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.leastsq.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LSEException;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LSEModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[][] testData;
    private double[] testTarget;

    @Before
    public void setup() {
        testData = new double[][]{{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}};
        testTarget = new double[]{1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
    }

    @Test
    public void testGaussNewtonOptimizer() {
        LSEModel model = new LSEModel(testData, testTarget);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        try {
            PointValuePair result = optimizer.optimize(model, initialGuess, new MaxEval(1000));
            SimplePointValuePair simpleResult = (SimplePointValuePair) result;
            PointValueFunction pointValueFunction = model.getValue();
            double[] point = simpleResult.getPoint();

            assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
            assertEquals(testTarget, point, 0.001);
            assertEquals(pointValueFunction.value(point), 0.0, 0.001);
        } catch (LSEException e) {
            System.out.println(e.getMessage());
            fail("LSEException occurred during optimization");
        }
    }

    @Test
    public void testGaussNewtonOptimizerWithConstraints() {
        double[][] testData = {{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}};
        double[] testTarget = new double[]{1.0, 1.0};
        double[] lowerBounds = new double[]{0.0, 0.0};
        double[] upperBounds = new double[]{10.0, 10.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        try {
            LSEModel model = new LSEModel(testData, testTarget);
            InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
            LinearConstraint linearConstraint = new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
            NonNegativeConstraint nonNegativeConstraint = new NonNegativeConstraint(new double[]{1.0, 1.0});

            PointValuePair result = optimizer.optimize(model, initialGuess, new MaxEval(1000), linearConstraint, nonNegativeConstraint);
            SimplePointValuePair simpleResult = (SimplePointValuePair) result;
            PointValueFunction pointValueFunction = model.getValue();
            double[] point = simpleResult.getPoint();

            assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
            assertEquals(testTarget, point, 0.001);
            assertEquals(pointValueFunction.value(point), 0.0, 0.001);
            assertTrue(linearConstraint.isSatisfied(point));
            assertTrue(nonNegativeConstraint.isSatisfied(point));
        } catch (LSEException e) {
            System.out.println(e.getMessage());
            fail("LSEException occurred during optimization");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizerNullData() {
        double[][] testData = null;
        double[] testTarget = new double[]{1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LSEModel model = new LSEModel(testData, testTarget);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        optimizer.optimize(model, initialGuess, new MaxEval(1000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizerNullTarget() {
        double[][] testData = new double[][]{{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}};
        double[] testTarget = null;
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LSEModel model = new LSEModel(testData, testTarget);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        optimizer.optimize(model, initialGuess, new MaxEval(1000));
    }

    @Test
    public void testGaussNewtonOptimizerWithNoWeights() {
        double[][] testData = new double[][]{{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}};
        double[] testTarget = new double[]{1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LSEModel model = new LSEModel(testData, testTarget);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        try {
            PointValuePair result = optimizer.optimize(model, initialGuess, new MaxEval(1000), null);
            SimplePointValuePair simpleResult = (SimplePointValuePair) result;
            PointValueFunction pointValueFunction = model.getValue();
            double[] point = simpleResult.getPoint();

            assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
            assertEquals(testTarget, point, 0.001);
            assertEquals(pointValueFunction.value(point), 0.0, 0.001);
        } catch (LSEException e) {
            System.out.println(e.getMessage());
            fail("LSEException occurred during optimization");
        }
    }

    @Test
    public void testGaussNewtonOptimizerWithWeights() {
        double[][] testData = new double[][]{{1.0, 2.0, 1.0},
                {4.0, 5.0, 2.0},
                {7.0, 8.0, 3.0}};
        double[] testTarget = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LSEModel model = new LSEModel(testData, testTarget, weights);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        try {
            PointValuePair result = optimizer.optimize(model, initialGuess, new MaxEval(1000));
            SimplePointValuePair simpleResult = (SimplePointValuePair) result;
            PointValueFunction pointValueFunction = model.getValue();
            double[] point = simpleResult.getPoint();

            assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
            assertEquals(testTarget, point, 0.001);
            assertEquals(pointValueFunction.value(point), 0.0, 0.001);
        } catch (LSEException e) {
            System.out.println(e.getMessage());
            fail("LSEException occurred during optimization");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizerWithInvalidWeights() {
        double[][] testData = new double[][]{{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}};
        double[] testTarget = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 2.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LSEModel model = new LSEModel(testData, testTarget, weights);
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});

        optimizer.optimize(model, initialGuess, new MaxEval(1000));
    }
}