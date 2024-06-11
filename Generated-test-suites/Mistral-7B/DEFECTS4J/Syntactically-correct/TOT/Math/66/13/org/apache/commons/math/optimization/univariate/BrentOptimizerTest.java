package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, MINIMIZE, -5.0, 5.0, 2.0);
        assertEquals(result.getPoint(), -1.0, 1e-7);
        assertEquals(result.getValue(), -2.0075, 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximization() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(result.getPoint(), Math.E, 1e-7);
        assertEquals(result.getValue(), Math.E, 1e-7);
    }

    @Test
    public void testBrentOptimizerErrorCases() {
        BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(UnsupportedOperationException.class, () -> optimizer.doOptimize());

        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaxEvaluations(-1));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaximalIterationCount(-1));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(-1));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setRelativeAccuracy(-1));

        UnivariateFunction f = null;
        assertThrows(NullPointerException.class, () -> optimizer.optimize(f, MINIMIZE, 0.0, 1.0, 0.0));

        optimizer.setMaxEvaluations(0);
        assertThrows(MaxEvalExceededException.class, () -> optimizer.optimize(new UnivariateFunction() {}, MINIMIZE, 0.0, 1.0, 0.0));
    }
}