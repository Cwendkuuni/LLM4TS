import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.ConvergenceChecker;

@RunWith(PowerMockRunner.class)
public class BrentOptimizerTest {

    @Test
    public void testConstructorWithValidArguments() {
        double rel = 0.01;
        double abs = 1.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = Mockito.spy(new ConvergenceChecker<UnivariatePointValuePair>() {});
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        PowerMockito.verifyNew(BrentOptimizer.class, rel, abs, checker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeTolerance() {
        double rel = 0.0;
        double abs = 1.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = Mockito.spy(new ConvergenceChecker<UnivariatePointValuePair>() {});
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteTolerance() {
        double rel = 0.01;
        double abs = 0.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = Mockito.spy(new ConvergenceChecker<UnivariatePointValuePair>() {});
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }
}