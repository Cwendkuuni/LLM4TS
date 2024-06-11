import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 Object result = op.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 arg2 = new LiteralExpression(2.0);
 result = op.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 arg1 = new LiteralExpression(0.5);
 result = op.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 arg1 = new LiteralExpression(1.5);
 arg2 = new LiteralExpression(0.5);
 result = op.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testTypeMismatch() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression("5");
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 op.computeValue(context); // should throw ClassCastException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(null);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 op.computeValue(context); // should throw IllegalArgumentException
 }
}