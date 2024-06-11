import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.RuntimeContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

  @Test
  public void testComputeValue() {
    double value1 = 5.5;
    double value2 = 7.5;
    EvalContext context = new EvalContext(new RuntimeContext());

    CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
        new ConstantExpression(value1), new ConstantExpression(value2));

    Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

    assertTrue(result);
  }
}