import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithRelAndAbs() {
        // ...
    }

    @Test
    public void testBrentOptimizerWithRelAbsAndChecker() {
        // ...
    }

    @Test
    public void testDoOptimize() {
        // ...
    }

    // edge cases and exception handling tests
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRel() {
        double rel = 1e-10;
        double abs = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbs() {
        double rel = 0.01;
        double abs = -1.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBrentOptimizerWithNullFunction() {
        double rel = 0.01;
        double abs = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        UnivariateFunction function = null;
        optimizer.optimize(function);
    }

    @Test(expected = NoSolutionException.class)
    public void testBrentOptimizerWithNonConvergingFunction() {
        double rel = 0.01;
        double abs = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        UnivariateFunction function = new CustomNonConvergingFunction();
        optimizer.optimize(function);
    }

    @Test
    public void testBrentOptimizerWithMultipleMinima() {
        double rel = 0.01;
        double abs = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        UnivariateFunction function = new QuarticFunction();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // check the optimization result is one of the minima
        double expectedMinima1X = -1.5;
        double expectedMinima1Fx = 4.0;
        double expectedMinima2X = 1.5;
        double expectedMinima2Fx = 4.0;
        double resultX = result.getPoint().getValue();
        double resultFx = result.getValue();

        if (resultX > expectedMinima1X && resultX < expectedMinima2X) {
            assertEquals(expectedMinima1Fx, resultFx, 0.001);
        } else if (resultX < expectedMinima1X && resultX > expectedMinima2X) {
            assertEquals(expectedMinima2Fx, resultFx, 0.001);
        } else {
            fail("Unexpected optimization result: (" + resultX + ", " + resultFx + ")");
        }
    }

    // ...
}