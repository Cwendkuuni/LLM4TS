import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(10), new ConstantExpression(5));
 JXPathContext context = new JXPathContext(new Object[] {new Integer(15), new Integer(10)});
 assertTrue(expression.evaluate(context));
 }

 @Test
 public void testGreaterThanWithInvalidTypes() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new StringExpression("10"), new ConstantExpression(5.5));
 JXPathContext context = new JXPathContext(new Object[] {"abc", new Double(5.5)});
 assertFalse(expression.evaluate(context));
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(10), new ConstantExpression(10));
 JXPathContext context = new JXPathContext(new Object[] {new Integer(10), new Integer(10)});
 assertTrue(expression.evaluate(context));
 }

 @Test
 public void testGreaterThanOrEqualWithInvalidTypes() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression("10"), new ConstantExpression(10.5));
 JXPathContext context = new JXPathContext(new Object[] {"abc", new Double(10.5)});
 assertFalse(expression.evaluate(context));
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(10));
 JXPathContext context = new JXPathContext(new Object[] {new Integer(5), new Integer(10)});
 assertFalse(expression.evaluate(context));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(10), new ConstantExpression(10));
 JXPathContext context = new JXPathContext(new Object[] {new Integer(10), new Integer(10)});
 assertTrue(expression.evaluate(context));
 }

 @Test
 public void testLessThanOrEqualWithInvalidTypes() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression("10"), new ConstantExpression(10.5));
 JXPathContext context = new JXPathContext(new Object[] {"abc", new Double(10.5)});
 assertFalse(expression.evaluate(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArguments() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null, new ConstantExpression(10));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSecondArgument() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(10), null);
 }
}