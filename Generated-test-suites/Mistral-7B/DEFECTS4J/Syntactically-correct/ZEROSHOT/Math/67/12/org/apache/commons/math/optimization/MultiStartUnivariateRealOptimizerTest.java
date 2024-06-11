package org.apache.commons.math.optimization.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction mockFunction;
    private UnivariateOptimizer mockOptimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator mockRandomGenerator;

    @Before
    public void setUp() {
        mockFunction = mock(UnivariateRealFunction.class);
        mockOptimizer = mock(UnivariateOptimizer.class);
        mockRandomGenerator = mock(RandomGenerator.class);

        when(mockOptimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new PointValuePair(0.5, 0.25));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, mockRandomGenerator);
    }

    @Test
    public void testGetFunctionValue() {
        assertEquals(0.25, multiStartOptimizer.getFunctionValue(), 0.0);
    }

    @Test
    public void testGetResult() {
        assertEquals(0.5, multiStartOptimizer.getResult(), 0.0);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertNull(multiStartOptimizer.getAbsoluteAccuracy());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResetAbsoluteAccuracy() {
        multiStartOptimizer.resetAbsoluteAccuracy();
    }

    @Test
    public void testGetIterationCount() {
        assertEquals(0, multiStartOptimizer.getIterationCount());
    }

    @Test
    public void testGetMaxIterationCount() {
        multiStartOptimizer.setMaxIterations(10);
        assertEquals(10, multiStartOptimizer.getMaxIterationCount());
    }

    @Test
    public void testGetMaxEvaluations() {
        multiStartOptimizer.setMaxEvaluations(5);
        assertEquals(5, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertNull(multiStartOptimizer.getRelativeAccuracy());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResetRelativeAccuracy() {
        multiStartOptimizer.resetRelativeAccuracy();
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        multiStartOptimizer.setAbsoluteAccuracy(1e-5);
    }

    @Test
    public void testSetMaxIterations() {
        multiStartOptimizer.setMaxIterations(5);
        verify(mockOptimizer).setMaxIterations(5);
    }

    @Test
    public void testSetMaxEvaluations() {
        multiStartOptimizer.setMaxEvaluations(3);
        verify(mockOptimizer).setMaxEvaluations(3);
    }

    @Test
    public void testSetRelativeAccuracy() {
        multiStartOptimizer.setRelativeAccuracy(1e-5);
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        multiStartOptimizer.optimize(mockFunction, GoalType.MINIMIZE,
                                      new SimpleBounds(0.0, 1.0));
        double[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        // the order of the optima is not specified
        // but they should all have been found by the optimizer
        for (int i = 0; i < 3; i++) {
            assertEquals(0.5, optima[i], 0.0);
        }
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        multiStartOptimizer.optimize(mockFunction, GoalType.MINIMIZE,
                                      new SimpleBounds(0.0, 1.0));
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        // the order of the optima values is not specified
        // but they should all have been found by the optimizer
        for (int i = 0; i < 3; i++) {
            assertEquals(0.25, optimaValues[i], 0.0);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaWhenNoOptimizationHasBeenPerformed() {
        multiStartOptimizer.getOptima();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesWhenNoOptimizationHasBeenPerformed() {
        multiStartOptimizer.getOptimaValues();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWhenNoOptimaFound() throws ConvergenceException {
        when(mockOptimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 1.0))
                .thenThrow(new ConvergenceException("No convergence reached"));

        multiStartOptimizer.optimize(mockFunction, GoalType.MINIMIZE, new SimpleBounds(0.0, 1.0));
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        double[] expectedOptima = new double[] { 0.5, 0.5, 0.5 };
        double[] expectedOptimaValues = new double[] { 0.25, 0.25, 0.25 };

        // this optimization should find the same optimum for each start
        multiStartOptimizer.optimize(mockFunction, GoalType.MINIMIZE,
                                      new SimpleBounds(0.0, 1.0));

        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        assertArrayEquals(expectedOptima, optima);
        assertArrayEquals(expectedOptimaValues, optimaValues);

        verify(mockOptimizer, MaxEval.times(3)).optimize(mockFunction, GoalType.MINIMIZE,
                new SimpleBounds(0.0, 1.0));
    }

}