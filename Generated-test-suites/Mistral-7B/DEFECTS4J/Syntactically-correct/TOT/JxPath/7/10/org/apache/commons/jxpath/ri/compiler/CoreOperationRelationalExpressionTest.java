import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(10), new CoreNumber(5));
 JXPathContext context = new JXPathContext();
 context.setValues("num1:[10], num2:[5]");
 boolean result = expression.evaluate(context);
 assertFalse("Expected less than or equal to, but greater than", result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(10), new CoreNumber(10));
 JXPathContext context = new JXPathContext();
 context.setValues("num1:[10], num2:[10]");
 boolean result = expression.evaluate(context);
 assertTrue("Expected greater than or equal to, but less than", result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(10));
 JXPathContext context = new JXPathContext();
 context.setValues("num1:[5], num2:[10]");
 boolean result = expression.evaluate(context);
 assertTrue("Expected less than, but greater than or equal to", !result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(10), new CoreNumber(10));
 JXPathContext context = new JXPathContext();
 context.setValues("num1:[10], num2:[10]");
 boolean result = expression.evaluate(context);
 assertTrue("Expected less than or equal to, but greater than", !result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNumberOfArguments() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(10), new CoreNumber(5), new CoreNumber(7));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidComparisonType() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreString("hello"), new CoreNumber(5));
 }
}