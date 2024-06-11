import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(3));
 JXPathContext ctx = new JXPathContext();
 ctx.setValues("a[1]", 3.0, "a[2]", 5.0);
 assertFalse(expression.evaluate(ctx).getValueBoolean());

 ctx.setValues("a[1]", 5.0, "a[2]", 3.0);
 assertTrue(expression.evaluate(ctx).getValueBoolean());
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(5));
 JXPathContext ctx = new JXPathContext();
 ctx.setValues("a[1]", 5.0, "a[2]", 5.0);
 assertTrue(expression.evaluate(ctx).getValueBoolean());

 CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(6));
 JXPathContext ctx2 = new JXPathContext();
 ctx2.setValues("a[1]", 5.0, "a[2]", 6.0);
 assertTrue(expression2.evaluate(ctx2).getValueBoolean());

 CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(3));
 JXPathContext ctx3 = new JXPathContext();
 ctx3.setValues("a[1]", 5.0, "a[2]", 3.0);
 assertFalse(expression3.evaluate(ctx3).getValueBoolean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null, new CoreNumber(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments2() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(3), null);
 }

}