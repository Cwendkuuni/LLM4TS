import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // basic test
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);

        // edge case: relative tolerance is zero
        try {
            new BrentOptimizer(0.0, 0.001, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // edge case: absolute tolerance is negative
        try {
            new BrentOptimizer(0.01, -0.001, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        // error scenario: ConvergenceChecker is null
        try {
            new BrentOptimizer(0.01, 0.001, null);
        } catch (NullPointerException e) {
            // expected
        }
    }
}