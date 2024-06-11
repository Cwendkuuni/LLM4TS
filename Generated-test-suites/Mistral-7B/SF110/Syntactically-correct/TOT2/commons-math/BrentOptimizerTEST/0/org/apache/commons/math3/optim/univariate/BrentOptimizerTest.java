import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.junit.Test;
import org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        // Valid input
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        Assert.assertNotNull(optimizer);

        // Invalid relative tolerance
        try {
            new BrentOptimizer(-0.1, 0.01, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Invalid absolute tolerance
        try {
            new BrentOptimizer(0.1, -0.01, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // ConvergenceChecker is null
        try {
            new BrentOptimizer(0.1, 0.01, null);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}