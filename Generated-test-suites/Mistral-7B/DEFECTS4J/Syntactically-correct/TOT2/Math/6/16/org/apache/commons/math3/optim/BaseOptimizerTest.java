// GetMaxEvaluationsTest.java
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optimization.BaseOptimizer;

public class GetMaxEvaluationsTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> baseOptimizer = new YourImplementationOfBaseOptimizer(); // replace with your implementation
        int maxEvaluations = baseOptimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100; // expected value based on your knowledge of the implementation
        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }
}