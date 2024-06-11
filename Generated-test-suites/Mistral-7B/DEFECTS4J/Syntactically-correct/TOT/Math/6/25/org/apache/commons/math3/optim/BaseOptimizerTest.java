package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link BaseOptimizer} interface.
 *
 * @param <PAIR> Type of the point/objective pair.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Point, Number>> extends AbstractTest {

    private static ConvergenceChecker<PAIR> defaultConvergenceChecker;
    private static BaseOptimizer<PAIR> optimizer;

    @BeforeAll
    public static void setUp() {
        defaultConvergenceChecker = new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR currentValue, PAIR previousValue, int iteration) {
                return Math.abs(currentValue.getValue().doubleValue() - previousValue.getValue().doubleValue()) < 1e-6;
            }
        };
        optimizer = new NelderMeadSimplex<PointValuePair>(new SimpleBounds(new Point(0.0), new Point(1.0)), 5, 10, GoalType.MINIMIZE, defaultConvergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        ObjectiveFunction<Point> testFunction = new UnivariateFunction() {
            @Override
            public Number value(Point point) {
                return Math.sin(point.getDimension() * Math.PI) * Math.exp(-point.getX()[0] * point.getX()[0]);
            }
        };
        optimizer.optimize(testFunction, new Point(0.0), null, new MaxEval(50));

        evaluations = optimizer.getEvaluations();
        assertEquals(51, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(defaultConvergenceChecker, convergenceChecker);
    }

    @Test
    public void testNegativeMaxEvaluations() {
        assertThrows(IllegalArgumentException.class, () -> new BaseOptimizer<PAIR>(new SimpleBounds(new Point(0.0), new Point(1.0)), -5, 10, GoalType.MINIMIZE, defaultConvergenceChecker));
    }

    @Test
    public void testNullConvergenceChecker() {
        assertThrows(NullPointerException.class, () -> new BaseOptimizer<PAIR>(new SimpleBounds(new Point(0.0), new Point(1.0)), 5, 10, GoalType.MINIMIZE, null));
    }

}