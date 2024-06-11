package org.apache.commons.math.optimization.tests;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 0, 1});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimulatedAnnealing(), 10, RandomUtils.createGaussRandom());
        optimizer.setMaxEvaluations(100);
        optimizer.setMaxIterations(1000);

        PointValuePair bestPoint = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
        double bestFunctionValue = bestPoint.getValue();

        List<PointValuePair> allPoints = new ArrayList<>();
        for (int i = 0; i < optimizer.getOptima().length; i++) {
            allPoints.add(new PointValuePair(optimizer.getOptima()[i], optimizer.getOptimaValues()[i]));
        }

        PointValuePair secondBestPoint = null;
        double secondBestFunctionValue = Double.MAX_VALUE;
        for (PointValuePair pointValuePair : allPoints) {
            if (Precision.isClose(pointValuePair.getValue(), bestFunctionValue, 0.001) &&
                    (secondBestFunctionValue > pointValuePair.getValue())) {
                secondBestPoint = pointValuePair;
                secondBestFunctionValue = pointValuePair.getValue();
            }
        }

        Assert.assertNotNull(secondBestPoint);
        Assert.assertFalse(secondBestPoint.equals(bestPoint));
        Assert.assertTrue(Precision.isClose(secondBestFunctionValue, bestFunctionValue, 0.001));

        // Test with optimizer that doesn't converge
        optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearch(), 10, RandomUtils.createGaussRandom());
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
            Assert.fail();
        } catch (ConvergenceException e) {
            // expected
        }

        Assert.assertArrayEquals(optimizer.getOptima(), new double[0]);
        Assert.assertArrayEquals(optimizer.getOptimaValues(), new double[0]);
    }
}