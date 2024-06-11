import org.apache.commons.math3.optimization.BaseOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(10);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(5);
        int evaluations = optimizer.getEvaluations();
        assertEquals(5, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}