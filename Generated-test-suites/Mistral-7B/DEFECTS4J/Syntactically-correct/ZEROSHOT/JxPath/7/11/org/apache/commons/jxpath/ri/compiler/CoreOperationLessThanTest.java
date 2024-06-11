package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.IntInfo;
import org.apache.commons.jxpath.ri.spi.Expression;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;

public class CoreOperationLessThanTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationLessThan coreOperationLessThan;

 @org.junit.Before
 public void setUp() {
 arg1 = TestUtils.compileExpression("#{1.0}");
 arg2 = TestUtils.compileExpression("#{2.0}");
 coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
 }

 @org.junit.Test
 public void testLessThan() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Double result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be false", Boolean.FALSE, result);

 arg1 = TestUtils.compileExpression("#{0.5}");
 arg2 = TestUtils.compileExpression("#{1.5}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be false", Boolean.FALSE, result);

 arg1 = TestUtils.compileExpression("#{1.0}");
 arg2 = TestUtils.compileExpression("#{1.5}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be false", Boolean.FALSE, result);

 arg1 = TestUtils.compileExpression("#{1.0}");
 arg2 = TestUtils.compileExpression("#{1.0}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be false", Boolean.FALSE, result);

 arg1 = TestUtils.compileExpression("#{1.0}");
 arg2 = TestUtils.compileExpression("#{0.5}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be true", Boolean.TRUE, result);

 arg1 = TestUtils.compileExpression("#{IntInfo.INT_MIN + 1}");
 arg2 = TestUtils.compileExpression("#{IntInfo.INT_MIN}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be true", Boolean.TRUE, result);
 }

 @org.junit.Test
 public void testLessThanWithNulls() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 arg1 = TestUtils.compileExpression("#{null}");
 arg2 = TestUtils.compileExpression("#{null}");

 Double result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertNull("Result should be null", result);

 arg1 = TestUtils.compileExpression("#{null}");
 arg2 = TestUtils.compileExpression("#{1.0}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be false", Boolean.FALSE, result);

 arg1 = TestUtils.compileExpression("#{1.0}");
 arg2 = TestUtils.compileExpression("#{null}");

 result = (Double) coreOperationLessThan.computeValue(context);
 Assert.assertEquals("Result should be true", Boolean.TRUE, result);
 }
}