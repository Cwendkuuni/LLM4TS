import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    @Test
    public void testConstructorWithRelAbsAndChecker() {
        // Test valid input
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        assertNotNull(optimizer);

        // Test invalid input: rel < MIN_RELATIVE_TOLERANCE
        try {
            new BrentOptimizer(0.005d, 0.0d, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test invalid input: abs <= 0.0
        try {
            new BrentOptimizer(0.1d, -0.01d, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}