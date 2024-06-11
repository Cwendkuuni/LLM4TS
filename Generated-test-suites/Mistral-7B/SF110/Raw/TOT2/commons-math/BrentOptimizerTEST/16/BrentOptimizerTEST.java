import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithRelAbsAndChecker() {
        // Test valid constructor
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = new GoalTypeChecker(GoalType.MINIMIZE);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertEquals(rel, optimizer.getRelativeThreshold());
        assertEquals(abs, optimizer.getAbsoluteThreshold());
        assertNotNull(optimizer.getConvergenceChecker());
        
        // Test constructor with invalid rel
        try {
            new BrentOptimizer(-0.01, abs, checker);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
        
        // Test constructor with invalid abs
        try {
            new BrentOptimizer(rel, -0.0, checker);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        
        // Test constructor with null checker
        try {
            new BrentOptimizer(rel, abs, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add test cases for other methods here
}