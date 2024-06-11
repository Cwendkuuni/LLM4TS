package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;

public class CoreOperationNotEqualTest {

 private Expression arg1, arg2;
 private CoreOperationNotEqual notEqual;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = new Path("$.a");
 arg2 = new Path("$.b");
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 context = new EvalContext();
 }

 @Test
 public void testComputeValue_whenArg1AndArg2AreEqual_thenReturnFalse() {
 Object result = notEqual.computeValue(context);
 assertTrue((Boolean) result);

 context.setValue("a", new Integer(1));
 context.setValue("b", new Integer(1));
 result = notEqual.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValue_whenArg1AndArg2AreNotEqual_thenReturnTrue() {
 Object result = notEqual.computeValue(context);
 assertFalse((Boolean) result);

 context.setValue("a", new Integer(1));
 context.setValue("b", new Integer(2));
 result = notEqual.computeValue(context);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGetSymbol() {
 assertEquals("!=", notEqual.getSymbol());
 }

 @Test
 public void testCoreOperationNotEqual_whenArg1IsInstanceOfCoreOperationCompare_thenReturnNewInstance() {
 CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
 CoreOperationNotEqual newNotEqual = (CoreOperationNotEqual) notEqual.clone();
 CoreOperationNotEqual newCompare = (CoreOperationNotEqual) compare.clone();

 notEqual = new CoreOperationNotEqual(compare.getArg1(), compare.getArg2());
 newNotEqual = (CoreOperationNotEqual) newCompare.clone();

 assertNotSame(notEqual, newNotEqual);
 }

 @After
 public void tearDown() {
 arg1 = null;
 arg2 = null;
 notEqual = null;
 context = null;
 }
}