import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.math3.optim.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithValidInputs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, new DefaultConvergenceChecker(100));
        // Add assertions here to test the functionality of the constructor
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, new DefaultConvergenceChecker(100));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01, new DefaultConvergenceChecker(100));
    }

    //...
}