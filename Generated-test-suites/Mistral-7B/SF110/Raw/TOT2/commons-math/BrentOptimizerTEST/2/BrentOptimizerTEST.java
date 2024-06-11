import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Test with valid arguments
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, null);
        assertNotNull(optimizer);

        // Test with invalid relative tolerance
        try {
            new BrentOptimizer(-1.0, 1.0, null);
            fail("Should throw NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test with invalid absolute tolerance
        try {
            new BrentOptimizer(1.0, -1.0, null);
            fail("Should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with null ConvergenceChecker
        try {
            new BrentOptimizer(1.0, 1.0, null);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add test cases for other methods here
}