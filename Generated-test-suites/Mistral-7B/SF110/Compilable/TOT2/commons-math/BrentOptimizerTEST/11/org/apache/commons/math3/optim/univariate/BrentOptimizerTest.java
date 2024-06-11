package org.apache.commons.math3.optim.univariate;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithRelAbsAndChecker() {
        // Test valid inputs
        BrentOptimizer optimizer = new BrentOptimizer(1.2, 0.01, null);
        assertNotNull(optimizer);

        // Test invalid input: rel < MIN_RELATIVE_TOLERANCE
        try {
            new BrentOptimizer(0.1, 0.01, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test invalid input: abs <= 0.0
        try {
            new BrentOptimizer(1.2, 0.0, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}