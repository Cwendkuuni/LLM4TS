package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.PointValuePair;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.SimpleBounds;
import org.apache.commons.math.optimization.UnivariateFunctionUtils;
import org.apache.commons.math.random.RandomDataGenerator;
import org.apache.commons.math.random.UniformRealDistribution;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {
    private static final double EPSILON = 1e-8;
    private static final int MAX_EVALUATIONS = 10;
    private static final int NUM_STARTS = 3;

    private UnivariateOptimizer optimizer;
    private BaseUnivariateRealOptimizer<UnivariateFunction> optimizerWrapper;
    private UnivariateFunction function;

    public MultiStartUnivariateRealOptimizerTest(UnivariateOptimizer optimizer) {
        this.optimizer = optimizer;
        this.optimizerWrapper = new MultiStartUnivariateRealOptimizer<UnivariateFunction>(
                optimizerWrapper = mock(BaseUnivariateRealOptimizer.class), NUM_STARTS,
                new RandomDataGenerator()) {
            @Override
            protected int getMaxEvaluations() {
                return MAX_EVALUATIONS;
            }
        };
        this.function = new PolynomialFunction(2, 1, new double[] {-1.0, 1.0, 1.0, -1.0});
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            new Object[] {new GoalTypeMinimizer(GoalTypeConstraint.NONE)},
            new Object[] {new GoalTypeMaximizer(GoalTypeConstraint.NONE)},
            new Object[] {new GoalTypeMinimizer(GoalTypeConstraint.BOUNDS)}
        });
    }

    @Test
    public void testMultiStartOptimize() throws Exception {
        when(optimizerWrapper.optimize(function, GoalType.VALUE, new SimpleBounds(0.0, 1.0)))
                .thenThrow(new ConvergenceException("test"));

        when(optimizerWrapper.optimize(function, GoalType.VALUE, new SimpleBounds(0.0, 1.0)))
                .thenReturn(new PointValuePair(0.5, 0.25));

        when(optimizerWrapper.optimize(function, GoalType.VALUE, new SimpleBounds(0.0, 1.0)))
                .thenReturn(new PointValuePair(0.75, 0.03));

        when(optimizerWrapper.optimize(function, GoalType.VALUE, new SimpleBounds(0.0, 1.0)))
                .thenThrow(new MaxCountExceededException(MAX_EVALUATIONS));

        when(optimizerWrapper.optimize(function, GoalType.VALUE, new SimpleBounds(0.0, 1.0)))
                .thenThrow(new ZeroException());

        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function, GoalType.VALUE,
                0.0, 1.0);

        assertEquals(NUM_STARTS, optima.length);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(0.5, 0.25),
                new UnivariateRealPointValuePair(0.75, 0.03),
                null
        }, optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNoOptimumYet() throws Exception {
        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function, GoalType.VALUE,
                0.0, 1.0);
        assertNull(optima);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergenceWithAnyStartPoint() throws Exception {
        optimizerWrapper.optimize(function, GoalType.VALUE, 0.0, 1.0);
        assertArrayEquals(new UnivariateRealPointValuePair[NUM_STARTS], optimizerWrapper.getOptima());
        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function, GoalType.VALUE,
                0.0, 1.0);
        assertArrayEquals(new UnivariateRealPointValuePair[NUM_STARTS], optima);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergenceWithAnyStartPointWithDifferentFunction() throws Exception {
        UnivariateFunction function2 = new PolynomialFunction(2, 1, new double[] {1.0, -1.0, 1.0, -1.0});
        optimizerWrapper.optimize(function, GoalType.VALUE, 0.0, 1.0);
        optimizerWrapper.optimize(function, GoalType.VALUE, 0.0, 1.0);
        optimizerWrapper.optimize(function, GoalType.VALUE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function2, GoalType.VALUE,
                0.0, 1.0);
        assertArrayEquals(new UnivariateRealPointValuePair[NUM_STARTS], optima);
    }

    @Test
    public void testOptimaSorted() throws Exception {
        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function, GoalType.VALUE,
                0.0, 1.0);

        UnivariateRealPointValuePair best = optima[0];
        Comparator<UnivariateRealPointValuePair> comparator =
                new Comparator<UnivariateRealPointValuePair>() {
                    @Override
                    public int compare(UnivariateRealPointValuePair o1, UnivariateRealPointValuePair o2) {
                        double v1 = o1.getValue();
                        double v2 = o2.getValue();
                        return Precision.compareDoubles(v1, v2, EPSILON);
                    }
                };
        Arrays.sort(optima, comparator);
        assertEquals(best.getValue(), optima[0].getValue());
    }
}