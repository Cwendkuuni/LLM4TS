import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.CoreOperationNotEqualArgs;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqual_BothNull_ReturnsTrue() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(new NullExpression(), new NullExpression());
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 EvalContext context = new EvalContext();
 assertNull(coreOperationNotEqual.computeValue(context));
 assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqual_NullAndObject_ReturnsTrue() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(new NullExpression(), new StringExpression("test"));
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 EvalContext context = new EvalContext();
 assertNull(coreOperationNotEqual.computeValue(context));
 assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqual_ObjectAndNull_ReturnsTrue() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(new StringExpression("test"), new NullExpression());
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 EvalContext context = new EvalContext();
 assertNotNull(coreOperationNotEqual.computeValue(context));
 assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(context));
 assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqual_BothObjects_ReturnsFalse() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(new StringExpression("test1"), new StringExpression("test2"));
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 EvalContext context = new EvalContext();
 assertNotNull(coreOperationNotEqual.computeValue(context));
 assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
 assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_InvalidArguments() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(new NullExpression(), null);
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_InvalidArguments2() {
 CoreOperationNotEqualArgs args = new CoreOperationNotEqualArgs(null, new StringExpression("test"));
 CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(args.getArg1(), args.getArg2());
 }
}