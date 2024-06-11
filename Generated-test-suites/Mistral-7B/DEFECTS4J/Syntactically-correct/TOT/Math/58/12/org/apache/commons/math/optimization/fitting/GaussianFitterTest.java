package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointsValuePair;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit() throws OutOfRangeException {
        // Test case for fitting a Gaussian function to a set of observations
        double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        GaussianFitter fitter = new GaussianFitter(new AmosTEnvironment().getLevenbergMarquardtOptimizer());
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }
        PointsValuePair result = fitter.fit();

        // Check that the fitting was successful
        assertEquals(3, result.getValue().length);
        double[] params = result.getValue();
        assertTrue(params[0] > 0.0);
        assertTrue(params[1] >= xData[0]);
        assertTrue(params[1] <= xData[xData.length - 1]);
        assertTrue(params[2] > 0.0);

        // Test the fitted function against the original data
        for (int i = 0; i < xData.length; i++) {
            double expectedY = Gaussian.THEORY.value(xData[i], params[0], params[1], params[2]);
            double actualY = fitter.value(xData[i], params);
            assertEquals(expectedY, actualY, 1e-6);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullData() {
        // Test case for fitting a Gaussian function to a null set of observations
        GaussianFitter fitter = new GaussianFitter(new AmosTEnvironment().getLevenbergMarquardtOptimizer());
        fitter.addObservedPoints((WeightedObservedPoint[]) null);
        fitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData() {
        // Test case for fitting a Gaussian function to an empty set of observations
        GaussianFitter fitter = new GaussianFitter(new AmosTEnvironment().getLevenbergMarquardtOptimizer());
        fitter.addObservedPoints(new WeightedObservedPoint[0]);
        fitter.fit();
    }

    @Test(expected = OutOfRangeException.class)
    public void testNonMonotonicData() throws OutOfRangeException {
        // Test case for fitting a Gaussian function to a set of observations with non-monotonic x values
        double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 3.0 };
        double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 531026.0 };

        GaussianFitter fitter = new GaussianFitter(new AmosTEnvironment().getLevenbergMarquardtOptimizer());
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }
        try {
            fitter.fit();
            fail("An OutOfRangeException should have been thrown.");
        } catch (OutOfRangeException e) {
            //Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeParameter() {
        // Test case for fitting a Gaussian function with negative initial guess values
        GaussianFitter fitter = new GaussianFitter(new AmosTEnvironment().getLevenbergMarquardtOptimizer());
        double[] initialGuess = { -1.0, 0.0, 0.0 };
        try {
            fitter.fit(initialGuess);
            fail("A NotStrictlyPositiveException should have been thrown.");
        } catch (NotStrictlyPositiveException e) {
            //Expected
        }
    }
}