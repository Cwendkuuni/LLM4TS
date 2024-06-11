import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Before
    public void setup() {
        optimizer = mock(BaseOptimizer.class);
        convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        when(optimizer.getEvaluations()).thenReturn(5);
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(5, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        Assert.assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxEvaluationsThrowsException() {
        when(optimizer.getMaxEvaluations()).thenThrow(new UnsupportedOperationException());
        optimizer.getMaxEvaluations();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEvaluationsThrowsException() {
        when(optimizer.getEvaluations()).thenThrow(new UnsupportedOperationException());
        optimizer.getEvaluations();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerThrowsException() {
        when(optimizer.getConvergenceChecker()).thenThrow(new UnsupportedOperationException());
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer = mock(MaxEval.class);
        optimizer.setMaxEvaluations(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(100, maxEvaluations);
    }
}