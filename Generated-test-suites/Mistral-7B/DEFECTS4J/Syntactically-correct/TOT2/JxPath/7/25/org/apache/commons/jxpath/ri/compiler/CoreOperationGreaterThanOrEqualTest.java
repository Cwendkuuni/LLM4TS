package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

  @Test
  public void testCoreOperationGreaterThanOrEqualInstance() {
    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(2.0));
    assertInstanceOf(CoreOperationGreaterThanOrEqual.class, operation);
  }

  @Test
  public void testComputeValueWithEqualValues() {
    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(2.0));
    EvalContext context = new EvalContext();
    assertEquals(Boolean.TRUE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithUnequalValues() {
    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
    EvalContext context = new EvalContext();
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  
  // Add more test cases as needed here
  
}