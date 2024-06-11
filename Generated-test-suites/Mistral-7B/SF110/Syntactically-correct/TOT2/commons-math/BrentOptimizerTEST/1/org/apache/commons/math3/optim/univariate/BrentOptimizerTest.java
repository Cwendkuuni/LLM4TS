import org.apache.commons.math3.optim.univariate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testBrentOptimizerConstructionWithValidArguments() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
    assertNotNull(optimizer);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testBrentOptimizerConstructionWithInvalidRelativeTolerance() {
    try {
      new BrentOptimizer(0.01, 1.0);
    } catch (NumberIsTooSmallException e) {
      assertEquals(0.01, e.getMinimum(), 0.0);
      assertEquals(0.0001, e.getDesired(), 0.0);
    }
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testBrentOptimizerConstructionWithInvalidAbsoluteTolerance() {
    try {
      new BrentOptimizer(0.1, -1.0);
    } catch (NotStrictlyPositiveException e) {
      assertEquals(-1.0, e.getValue());
    }
  }
}