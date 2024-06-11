package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.RuntimeContext;
import org.apache.commons.jxpath.ri.info.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.junit.Test;

public class CoreOperationLessThanTest {

  @Test
  public void testComputeValue() {
    CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.0), new LiteralExpression(2.0));
    InfoSet infoSet = InfoSetUtil.createInfoSet();
    EvalContext context = new EvalContext(new RuntimeContext(), infoSet);
    Object result = coreOperationLessThan.computeValue(context);
    assertEquals(Boolean.FALSE, result);

    coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.0), new LiteralExpression(1.5));
    context = new EvalContext(new RuntimeContext(), infoSet);
    result = coreOperationLessThan.computeValue(context);
    assertEquals(Boolean.TRUE, result);

    coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(Double.NaN), new LiteralExpression(1.0));
    context = new EvalContext(new RuntimeContext(), infoSet);
    try {
      result = coreOperationLessThan.computeValue(context);
      fail("Expected Exception: ArithmeticException");
    } catch (ArithmeticException e) {
      // Expected
    }

    coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.0), new LiteralExpression(null));
    context = new EvalContext(new RuntimeContext(), infoSet);
    try {
      result = coreOperationLessThan.computeValue(context);
      fail("Expected Exception: ClassCastException");
    } catch (ClassCastException e) {
      // Expected
    }

    coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(null), new LiteralExpression(1.0));
    context = new EvalContext(new RuntimeContext(), infoSet);
    try {
      result = coreOperationLessThan.computeValue(context);
      fail("Expected Exception: ClassCastException");
    } catch (ClassCastException e) {
      // Expected
    }

    coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression("string"), new LiteralExpression(1.0));
    context = new EvalContext(new RuntimeContext(), infoSet);
    try {
      result = coreOperationLessThan.computeValue(context);
      fail("Expected Exception: ClassCastException");
    } catch (ClassCastException e) {
      // Expected
    }
  }
}