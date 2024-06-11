import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.univariate.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link BaseOptimizer} interface.
 *
 * @param <PAIR> Type of the point/objective pair.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;
    private int maxEvaluations;

    @Before
    public void setup() {
        optimizer = new BaseMockOptimizer<>();
        convergenceChecker = new SimpleConvergenceChecker();
        maxEvaluations = 10;
        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(new SimpleBoundingBox(0.0, 1.0), new NoFunction(), null);
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new SimpleBoundingBox(0.0, 1.0), new NoFunction(), null);
        assertEquals(2, optimizer.getEvaluations());

        optimizer.optimize(new SimpleBoundingBox(0.0, 1.0), new NoFunction(), null);
        assertEquals(4, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceCheckerWithUnsupportedChecker() {
        optimizer.setConvergenceChecker(new UnsupportedConvergenceChecker());
    }

    @Test
    public void testOptimizeWithDefaultConvergenceChecker() {
        optimizer.optimize(new SimpleBoundingBox(0.0, 1.0), new NoFunction(), null);
        List<PAIR> results = optimizer.getSolutions();
        assertTrue(results.isEmpty());
        assertEquals(maxEvaluations, optimizer.getEvaluations());
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        convergenceChecker = new CustomConvergenceChecker();
        optimizer.setConvergenceChecker(convergenceChecker);

        optimizer.optimize(new SimpleBoundingBox(0.0, 1.0), new NoFunction(), null);
        List<PAIR> results = optimizer.getSolutions();
        assertTrue(results.isEmpty());
        assertEquals(maxEvaluations, optimizer.getEvaluations());

        convergenceChecker.assertConverged(optimizer);
    }
}

// BaseMockOptimizer is a mock implementation of BaseOptimizer for testing purpose.
class BaseMockOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int evaluations;

    @Override
    public int getMaxEvaluations() {
        return MaxEval.UNLIMITED;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
        // Empty implementation
    }

    @Override
    public List<PAIR> optimize(BoundingBox bounds, Function function, Object initialGuess) {
        evaluations += maxEvaluations;
        return new ArrayList<>();
    }

    @Override
    public List<PAIR> getSolutions() {
        return new ArrayList<>();
    }
}

// SimpleConvergenceChecker is a simple convergence checker that always considers the optimization as converged.
class SimpleConvergenceChecker implements ConvergenceChecker<PAIR> {
    @Override
    public boolean isConverged(BaseOptimizer<? extends PAIR> optimizer) {
        return true;
    }
}

// CustomConvergenceChecker is a custom convergence checker for testing purpose.
class CustomConvergenceChecker implements ConvergenceChecker<PAIR> {
    private int convergedEvaluations;

    @Override
    public boolean isConverged(BaseOptimizer<? extends PAIR> optimizer) {
        convergedEvaluations = optimizer.getEvaluations();
        return convergedEvaluations >= maxEvaluations / 2;
    }

    public void assertConverged(BaseOptimizer<? extends PAIR> optimizer) {
        assertTrue(isConverged(optimizer));
        assertEquals(maxEvaluations, optimizer.getEvaluations());
    }
}

// NoFunction is a function with no effect for testing purpose.
class NoFunction implements Function {
    @Override
    public double value(double x) {
        return 0.0;
    }

    @Override
    public void gradient(double x, double[] grad) {
        // Empty implementation
    }

    @Override
    public void gradient(double x, double[] grad, double[] jacobian) {
        // Empty implementation
    }

    @Override
    public void valueAndGradient(double x, double[] grad) {
        // Empty implementation
    }

    @Override
    public void valueAndGradient(double x, double[] grad, double[] jacobian) {
        // Empty implementation
    }
}

// UnsupportedConvergenceChecker is an unsupported convergence checker for testing the exception handling.
class UnsupportedConvergenceChecker implements ConvergenceChecker<PAIR> {
    // Empty implementation
}