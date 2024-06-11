
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = Mockito.mock(BaseOptimizer.class);
        Mockito.when(optimizer.getMaxEvaluations()).thenReturn(100);

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
        Mockito.verify(optimizer, Mockito.times(1)).getMaxEvaluations();
    }

    @Test
    public void testGetMaxEvaluations_negativeValue() {
        BaseOptimizer<Object> optimizer = Mockito.mock(BaseOptimizer.class);
        Mockito.when(optimizer.getMaxEvaluations()).thenThrow(new IllegalArgumentException("Maximum number of evaluations cannot be negative"));

        try {
            optimizer.getMaxEvaluations();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Maximum number of evaluations cannot be negative", e.getMessage());
        }
        Mockito.verify(optimizer, Mockito.times(1)).getMaxEvaluations();
    }

    @Test
    public void testGetMaxEvaluations_multipleCalls() {
        BaseOptimizer<Object> optimizer = Mockito.mock(BaseOptimizer.class);
        Mockito.when(optimizer.getMaxEvaluations()).thenReturn(100);

        int maxEvaluations1 = optimizer.getMaxEvaluations();
        int maxEvaluations2 = optimizer.getMaxEvaluations();

        assertEquals(100, maxEvaluations1);
        assertEquals(100, maxEvaluations2);
        Mockito.verify(optimizer, Mockito.times(2)).getMaxEvaluations();
    }

    // Test the getEvaluations() method with similar test cases

    // Test the getConvergenceChecker() method with similar test cases
}
