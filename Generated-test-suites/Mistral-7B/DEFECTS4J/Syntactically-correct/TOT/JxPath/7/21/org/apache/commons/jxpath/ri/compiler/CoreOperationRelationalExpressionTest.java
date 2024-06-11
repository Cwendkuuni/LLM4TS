import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperation left = new CoreNumberExpression(5);
 CoreOperation right = new CoreNumberExpression(3);
 CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(left, right);
 JXPathContext context = new JXPathContext();
 context.setValues("a", new Object[] { 5, 3, 7, 10 });
 boolean result = expression.evaluateBoolean(context);
 assertTrue(result);
 }

 @Test
 public void testLessThan() {
 CoreOperation left = new CoreNumberExpression(3);
 CoreOperation right = new CoreNumberExpression(5);
 CoreOperationRelationalExpression expression = new CoreLessThanExpression(left, right);
 JXPathContext context = new JXPathContext();
 context.setValues("a", new Object[] { 5, 3, 7, 10 });
 boolean result = expression.evaluateBoolean(context);
 assertFalse(result);
 }

 @Test
 public void testNotEqual() {
 CoreOperation left = new CoreNumberExpression(5);
 CoreOperation right = new CoreNumberExpression(5);
 CoreOperationRelationalExpression expression = new CoreNotEqualExpression(left, right);
 JXPathContext context = new JXPathContext();
 context.setValues("a", new Object[] { 5, 3, 7, 10 });
 boolean result = expression.evaluateBoolean(context);
 assertFalse(result);
 }

 @Test
 public void testError_NullArguments() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
 try {
 expression.evaluateBoolean(new JXPathContext());
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

}